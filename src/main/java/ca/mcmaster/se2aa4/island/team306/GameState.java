package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum GameState {
    SETUP, // Know our initial position in the map
    SEARCH, // Grid-search until emergency site is found
    BRANCH, // Branch around the emergency site to find the nearest creek
    SUCCESS, // Mission was a success, abort
    FAILURE; // Mission was a failure, abort

    public GameState next(){
        switch(this){
            case SETUP:
                return SEARCH;
            case SEARCH:
                return BRANCH;
            case BRANCH:
                return SUCCESS;
            default:
                // No next state for success or failure
                return this;
        }
    }

    public void findNearestCreek(Drone drone, Map map, Mover mover) {
        Coords dronePosition = drone.getPosition();
        Direction startOrientation = drone.getHeading();
        List<Direction> spiralDirections = generateSpiralDirections();
        for (Direction direction : spiralDirections) {
            Coords nextPosition = dronePosition.step(direction);
            MapValue nextTile = map.checkCoords(nextPosition);
            if (nextTile == MapValue.CREEK) {
                DecisionType decisionType = DecisionType.PHOTO;
                ParsedResult result = new ParsedResultBuilder(new Decision(decisionType, direction))
                        .populate("{\"cost\":0,\"extras\":{\"creeks\":[\"nearest_creek_id\"]}}")
                        .build();
                drone.updateResult(result);
                return;
            }
            mover.move();
        }
    }
    
    private List<Direction> generateSpiralDirections() {
        List<Direction> directions = new ArrayList<>();
        directions.add(Direction.EAST);
        directions.add(Direction.NORTH);
        directions.add(Direction.WEST);
        directions.add(Direction.SOUTH);
        int step = 1;
        while (true) {
            for (int i = 0; i < step; i++) directions.add(Direction.EAST);
            for (int i = 0; i < step; i++) directions.add(Direction.NORTH);
            step++;
            for (int i = 0; i < step; i++) directions.add(Direction.WEST);
            for (int i = 0; i < step; i++) directions.add(Direction.SOUTH);
            step++;
        }
    }
    
}
