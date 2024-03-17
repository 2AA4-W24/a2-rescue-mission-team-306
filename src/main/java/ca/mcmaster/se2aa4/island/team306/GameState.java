package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.List;

public enum GameState {
    SETUP, // Know our initial position in the map
    FIND_ISLAND, // Locate and travel to island
    SEARCH, // Grid-search island until emergency site is found
    BRANCH, // Branch around the emergency site to find the nearest creek
    SUCCESS, // Mission was a success, abort
    FAILURE; // Mission was a failure, abort

    private static final int BATCH_SIZE = 10; // Adjust batch size as needed
    private List<Direction> spiralBatch;
    private int spiralIndex;

    public GameState next(){
        switch(this){
            case SETUP:
                return FIND_ISLAND;
            case FIND_ISLAND:
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

    public void findNearestCreek(Drone drone, Map map, Decider decider, GameTracker tracker) {
        Coords dronePosition = drone.getPosition();

        if (spiralBatch == null || spiralBatch.isEmpty() || spiralIndex >= spiralBatch.size()) {
            spiralBatch = generateSpiral(BATCH_SIZE);
            spiralIndex = 0;
        }

        for (int i = spiralIndex; i < spiralBatch.size(); i++) {
            Direction direction = spiralBatch.get(i);
            decider.getDecisionQueue().enqueue(new Decision(DecisionType.FLY_FORWARD, direction));
            decider.getDecisionQueue().enqueue(new Decision(DecisionType.PHOTO)); 
        }

        while (!decider.getDecisionQueue().isEmpty()) {
            Decision decision = decider.getNewDecision();
            if (decision.getType() == DecisionType.ABORT) {
                tracker.failMission();
                return;
            }
            if (map.checkCoords(dronePosition) == MapValue.CREEK) {
                tracker.succeedMission();
                return;
            }
        }
    }



    private List<Direction> generateSpiral(int size) {
        List<Direction> spiral = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            spiral.add(Direction.NORTH);
            spiral.add(Direction.EAST);
            spiral.add(Direction.SOUTH);
            spiral.add(Direction.WEST);
        }
        return spiral;
    }
}
