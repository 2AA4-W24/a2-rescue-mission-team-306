package ca.mcmaster.se2aa4.island.team306;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mover {
    private final Drone drone;
    private final Map map;
    private final DecisionQueue queue;
    private final GameTracker tracker;
    private final Direction START_ORIENT;
    private Direction towards;
    private Coords initial_land;
    private Coords initial_water;
    private int start;
    private Coords start_pos;
    private final Logger logger = LogManager.getLogger();

    public static final Decision FLY_NORTH = 
        new Decision(DecisionType.FLY_FORWARD, Direction.NORTH);
    public static final Decision FLY_EAST = 
        new Decision(DecisionType.FLY_FORWARD, Direction.EAST);
    public static final Decision FLY_SOUTH = 
        new Decision(DecisionType.FLY_FORWARD, Direction.SOUTH);
    public static final Decision FLY_WEST = 
        new Decision(DecisionType.FLY_FORWARD, Direction.WEST);

    public static final Decision TURN_NORTH = 
        new Decision(DecisionType.TURN, Direction.NORTH);
    public static final Decision TURN_EAST = 
        new Decision(DecisionType.TURN, Direction.EAST);
    public static final Decision TURN_SOUTH = 
        new Decision(DecisionType.TURN, Direction.SOUTH);
    public static final Decision TURN_WEST = 
        new Decision(DecisionType.TURN, Direction.WEST);

    public Mover(Drone drone, Map map, DecisionQueue queue, GameTracker tracker){
        this.drone = drone;
        this.map = map;
        this.queue = queue;
        this.tracker = tracker;
        this.START_ORIENT = drone.getHeading();
        this.towards = null;
        start = 0;
    }

    private boolean shouldMove(){
        switch(tracker.getState()){
            case SETUP:
            case SUCCESS:
            case FAILURE:
                return false;
            case FIND_ISLAND:
            case SEARCH:
            default:
                return true;

        }
    }

    public boolean move(){
        if (shouldMove()){
            towards = goTowards();
            if (towards == drone.getHeading().getBackwards()){
                return false;
            }
            return true;
        }
        return false;
    }

    public Direction goTowards(){
        Coords pos = drone.getPosition();
        Direction facing = drone.getHeading();
        Path path;
        MapValue right = map.checkCoords(pos.step(facing.getRight())),
                forward = map.checkCoords(pos.step(facing)),
                left = map.checkCoords(pos.step(facing.getLeft()));
        DecisionQueue pathQueue;
        Decision first_step;
        switch(tracker.getState()){
            case FIND_ISLAND:
                if(map.findNearestTile(MapValue.GROUND) != null){
                    Coords land = map.findNearestTile(MapValue.GROUND);
                    initial_land = land;
                    path = new Path(drone.getPosition(), land, drone.getHeading(), map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                return START_ORIENT;
            case FOLLOW_COAST_OUTSIDE:
                logger.info("OUTSIDE");
                if(pos.equals(initial_land)){
                    if(left == MapValue.OCEAN){
                        initial_water = pos.step(facing.getLeft());
                    }else if(forward == MapValue.OCEAN){
                        initial_water = pos.step(facing);
                    }else{
                        initial_water = pos.step(facing.getRight());
                    }
                }
                if(pos.equals(initial_water)){
                    if(start > 1){
                        start = 0;
                        path = new Path(pos, initial_land, facing, map);
                        pathQueue = path.findPath();
                        first_step = pathQueue.dequeue();
                        queue.enqueue(pathQueue);
                        tracker.completeLoop();
                        return first_step.getDirection();
                    }
                    start++;
                }
                if(left == MapValue.OCEAN || left == MapValue.SCANNED_OCEAN){
                    path = new Path(pos, pos.step(facing.getLeft()), facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                if(forward == MapValue.OCEAN || forward == MapValue.SCANNED_OCEAN){
                    path = new Path(pos, pos.step(facing), facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                
                if(right == MapValue.OCEAN || right == MapValue.SCANNED_OCEAN){
                    path = new Path(pos, pos.step(facing.getRight()), facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection(); 
                }

                path = new Path(pos, pos.step(facing.getBackwards()), facing, map);
                pathQueue = path.findPath();
                first_step = pathQueue.dequeue();
                queue.enqueue(pathQueue);
                return first_step.getDirection(); 

            case FOLLOW_COAST_INSIDE:
            logger.info("INSIDE");
                if(pos.equals(start_pos)){
                    tracker.completeLoop();
                }
                if(start == 0){
                    start_pos = pos;
                    start++;
                }
                if(right.isLand()){
                    path = new Path(pos, pos.step(facing.getRight()), facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                if(forward.isLand()){
                    path = new Path(pos, pos.step(facing), facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                
                if(left.isLand()){
                    path = new Path(pos, pos.step(facing.getLeft()), facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }

                path = new Path(pos, pos.step(facing.getBackwards()), facing, map);
                pathQueue = path.findPath();
                first_step = pathQueue.dequeue();
                queue.enqueue(pathQueue);
                return first_step.getDirection();
            case SEARCH:
                logger.info("SEARCHING");
                if(!right.scanned()){
                    path = new Path(pos, pos.step(facing.getRight()), facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                if(!forward.scanned()){
                    path = new Path(pos, pos.step(facing), facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                path = new Path(pos, pos.step(facing.getLeft()), facing, map);
                pathQueue = path.findPath();
                first_step = pathQueue.dequeue();
                queue.enqueue(pathQueue);
                return first_step.getDirection();
                
            default:
                return START_ORIENT;
        }
    }

    public Decision deriveDecision() {
        return deriveDecision(towards);
    }

    private Decision deriveDecision(Direction drxn){
        return deriveDecision(drxn, drone.getHeading());
    }


    private Decision deriveDecision(Direction drxn, Direction heading){
        if(drxn == heading){
           return deriveFly(drxn);
        }
        else {
            return deriveTurn(drxn);
        }
    }

    public Decision deriveFly(Direction drxn){
        switch(drxn){
            case Direction.NORTH:
                return FLY_NORTH;
            case Direction.EAST:
                return FLY_EAST;
            case Direction.SOUTH:
                return FLY_SOUTH;
            case Direction.WEST:
                return FLY_WEST;
            default:
                throw new NullPointerException();
        }
    }

    public Decision deriveTurn(Direction drxn){
        switch(drxn){
            case Direction.NORTH:
                return TURN_NORTH;
            case Direction.EAST:
                return TURN_EAST;
            case Direction.SOUTH:
                return TURN_SOUTH;
            case Direction.WEST:
                return TURN_WEST;
            default:
                throw new NullPointerException();
        }
    }



}
