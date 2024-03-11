package ca.mcmaster.se2aa4.island.team306;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Mover {
    private Drone drone;
    public Map map;
    private Queue<Direction> path;
    
    public Mover(Drone drone, Map map){
        this.drone = drone;
        this.map = map;
        this.path = new LinkedList<>();
    }

    public boolean move(ParsedResult result) {
        Coords pos = drone.getPosition();
        Coords front = pos.step(drone.getHeading());
        Coords right = pos.step(drone.getHeading().getRight());
        Coords left = pos.step(drone.getHeading().getLeft());

        Coords nearestGround = map.findNearestTile(MapValue.GROUND);
        if (nearestGround == null) {
            if (map.checkCoords(left) == null && map.checkCoords(right) == null && map.checkCoords(front) == null) {
                return false;
            }
        }
        return true;
    }

    public Queue<Direction> goTowards(Coords coords) {
        Coords nearestGround = map.findNearestTile(MapValue.GROUND);
        Queue<Direction> path = new LinkedList<>();

        if (nearestGround != null) {
            Path pathfinder = new Path(drone.getPosition(), nearestGround, drone.getHeading());
            path = pathfinder.findPath();
        } else {
            path.add(drone.getHeading());
        }

        return path;
    }


    
}


