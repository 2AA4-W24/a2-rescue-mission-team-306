package ca.mcmaster.se2aa4.island.team306;

import javax.sound.sampled.BooleanControl;

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

    // Constants representing movement decisions
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

    /**
     * Constructs a Mover object with the specified drone, map, decision queue, and game tracker.
     *
     * @param drone   the drone controlled by the mover
     * @param map     the map representing the drone's environment
     * @param queue   the decision queue for storing movement decisions
     * @param tracker the game tracker for monitoring the game state
     */
    public Mover(Drone drone, Map map, DecisionQueue queue, GameTracker tracker){
        this.drone = drone;
        this.map = map;
        this.queue = queue;
        this.tracker = tracker;
        this.START_ORIENT = drone.getHeading();
        this.towards = null;
        start = 0;
    }

    /**
     * Determines whether the drone should make a movement decision based on the current game state.
     *
     * @return true if the drone should make a movement decision, false otherwise
     */
    private boolean shouldMove(){
        return switch (tracker.getState()) {
            case SETUP, SUCCESS, FAILURE -> false;
            default -> true;
        };
    }

    /**
     * Executes a movement decision for the drone based on the current game state and environmental conditions.
     *
     * @return true if the drone successfully executes a movement decision, false otherwise
     */
    public boolean move(){
        if (shouldMove()){
            towards = goTowards();
            return towards != drone.getHeading().getBackwards();
        }
        return false;
    }

    /**
     * Determines the direction towards which the drone should move based on the current game state and environment.
     *
     * @return the direction towards which the drone should move
     */
    public Direction goTowards(){
        Coords pos = drone.getPosition();
        Direction facing = drone.getHeading();
        Path path;
        MapValue right = map.checkCoords(pos.step(facing.getRight())),
                forward = map.checkCoords(pos.step(facing)),
                left = map.checkCoords(pos.step(facing.getLeft())),
                back = map.checkCoords(pos.step(facing.getBackwards())),
                curr = map.checkCoords(pos);
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
                return hugCoastline();

            case FOLLOW_COAST_INSIDE:
            logger.info("INSIDE");
                if(pos.equals(start_pos)){
                    tracker.completeLoop();
                }
                if(start == 0){
                    start_pos = pos;
                    start++;
                }
                return loopInward();
                
            case SEARCH:
                logger.info("SEARCHING");
                Boolean trapped = right.scanned() && left.scanned() && back.scanned() && forward.scanned();
                Coords close_pos;
                if(trapped){
                    if(!curr.isLand()){
                        return loopInward();
                    }
                    close_pos = nearestUnscanned(pos, facing);
                    path = new Path(pos, close_pos, facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                    
                }
                
                if(!(right.scanned())){
                    path = new Path(pos, pos.step(facing.getRight()), facing, map);
                    pathQueue = path.findPath();
                    first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                if(!(forward.scanned())){
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

    private Direction hugCoastline(){
        Coords pos = drone.getPosition();
        Direction facing = drone.getHeading();
        Path path;
        MapValue right = map.checkCoords(pos.step(facing.getRight())),
                forward = map.checkCoords(pos.step(facing)),
                left = map.checkCoords(pos.step(facing.getLeft()));
        DecisionQueue pathQueue;
        Decision first_step;
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
    }

    private Direction loopInward(){
        Coords pos = drone.getPosition();
        Direction facing = drone.getHeading();
        Path path;
        MapValue right = map.checkCoords(pos.step(facing.getRight())),
                forward = map.checkCoords(pos.step(facing)),
                left = map.checkCoords(pos.step(facing.getLeft()));
        DecisionQueue pathQueue;
        Decision first_step;
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
    }

    private Coords nearestUnscanned(Coords pos, Direction facing){
        Coords temp_pos = pos;
        Coords close_pos = pos.step(facing);
        int closest = 1000;
        MapValue check = map.checkCoords(temp_pos);
        int close_check = 0;
        while(check.scanned()){
            temp_pos = temp_pos.step(facing.getRight());
            close_check++;
            check = map.checkCoords(temp_pos);
            if(check == MapValue.OCEAN || check == MapValue.SCANNED_OCEAN){
                break;
            }
            if(check == MapValue.GROUND || check == MapValue.UNKNOWN){
                if(close_check < closest){
                    close_pos = temp_pos;
                    closest = close_check;
                }
            }
        }
        close_check = 0;
        temp_pos = pos;
        while(check.scanned()){
            temp_pos = temp_pos.step(facing);
            close_check++;
            check = map.checkCoords(temp_pos);
            if(check == MapValue.OCEAN || check == MapValue.SCANNED_OCEAN){
                break;
            }
            if(check == MapValue.GROUND || check == MapValue.UNKNOWN){
                if(close_check < closest){
                    close_pos = temp_pos;
                    closest = close_check;
                }
            }
        }
        close_check = 0;
        temp_pos = pos;
        while(check.scanned()){
            temp_pos = temp_pos.step(facing.getLeft());
            close_check++;
            check = map.checkCoords(temp_pos);
            if(check == MapValue.OCEAN || check == MapValue.SCANNED_OCEAN){
                break;
            }
            if(check == MapValue.GROUND || check == MapValue.UNKNOWN){
                if(close_check < closest){
                    close_pos = temp_pos;
                    closest = close_check;
                }
            }
        }
        close_check = 0;
        temp_pos = pos;
        while(check.scanned()){
            temp_pos = temp_pos.step(facing.getBackwards());
            close_check++;
            check = map.checkCoords(temp_pos);
            if(check == MapValue.OCEAN || check == MapValue.SCANNED_OCEAN){
                break;
            }
            if(check == MapValue.GROUND || check == MapValue.UNKNOWN){
                if(close_check < closest){
                    close_pos = temp_pos;
                    closest = close_check;
                }
            }
        }
        return close_pos;
    }

    public Decision deriveDecision() {
        return deriveDecision(towards);
    }

    private Decision deriveDecision(Direction drxn){
        return deriveDecision(drxn, drone.getHeading());
    }

    /**
     * Derives a movement decision for the drone based on the specified direction and current heading.
     *
     * @param drxn    the direction towards which the drone should move
     * @param heading the current heading of the drone
     * @return the movement decision derived from the specified direction and heading
     */
    private Decision deriveDecision(Direction drxn, Direction heading){
        if(drxn == heading){
           return deriveFly(drxn);
        }
        else {
            return deriveTurn(drxn);
        }
    }

    /**
     * Derives a forward movement decision for the drone based on the specified direction.
     *
     * @param drxn the direction towards which the drone should move
     * @return the forward movement decision derived from the specified direction
     * @throws NullPointerException if the specified direction is null
     */
    public static Decision deriveFly(Direction drxn){
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
    
    /**
     * Derives a turning decision based on the specified direction.
     *
     * @param drxn the direction to turn towards
     * @return the turning decision corresponding to the specified direction
     * @throws NullPointerException if the specified direction is null
     */
    public static Decision deriveTurn(Direction drxn){
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
