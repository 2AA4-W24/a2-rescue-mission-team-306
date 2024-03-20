package ca.mcmaster.se2aa4.island.team306;

public class Mover {
    private final Drone drone;
    private final Map map;
    private final DecisionQueue queue;
    private final GameTracker tracker;
    private final Direction START_ORIENT;
    private Direction towards;

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
    }

    /**
     * Determines whether the drone should make a movement decision based on the current game state.
     *
     * @return true if the drone should make a movement decision, false otherwise
     */
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

    /**
     * Executes a movement decision for the drone based on the current game state and environmental conditions.
     *
     * @return true if the drone successfully executes a movement decision, false otherwise
     */
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

    /**
     * Determines the direction towards which the drone should move based on the current game state and environment.
     *
     * @return the direction towards which the drone should move
     */
    public Direction goTowards(){
        switch(tracker.getState()){
            case FIND_ISLAND:
                if(map.findNearestTile(MapValue.GROUND) != null){
                    Coords land = map.findNearestTile(MapValue.GROUND);
                    Path path = new Path(drone.getPosition(), land, drone.getHeading(), map);
                    DecisionQueue pathQueue = path.findPath();
                    Decision first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                return START_ORIENT;
            case SEARCH:
                Coords pos = drone.getPosition();
                Direction facing = drone.getHeading();
                Path path;
                MapValue up_right = map.checkCoords(pos.step(facing.getRight()).step(facing)),
                    back_left = map.checkCoords(pos.step(facing.getBackwards()).step(facing.getLeft())),
                    back_right = map.checkCoords(pos.step(facing.getBackwards()).step(facing.getRight())),
                    up_left = map.checkCoords(pos.step(facing.getLeft()).step(facing)),
                    right = map.checkCoords(pos.step(facing.getRight())),
                    forward = map.checkCoords(pos.step(facing)),
                    left = map.checkCoords(pos.step(facing.getLeft()));
                MapValue curr = map.currentValue();

                if(right.scanned() && forward.scanned() && left.scanned() && up_right.scanned() && up_left.scanned() && back_right.scanned() && back_left.scanned()){
                    if (map.findNearestTile(MapValue.EMERGENCY_SITE) != null){
                        tracker.succeedMission();
                    }
                }
                
                
                if(right == MapValue.GROUND||(curr.isLand()&&!right.isLand())){
                    path = new Path(pos, pos.step(facing.getRight()), facing, map);
                    DecisionQueue pathQueue = path.findPath();
                    Decision first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                if(forward == MapValue.GROUND){
                    path = new Path(pos, pos.step(facing), facing, map);
                    DecisionQueue pathQueue = path.findPath();
                    Decision first_step = pathQueue.dequeue();
                    queue.enqueue(pathQueue);
                    return first_step.getDirection();
                }
                
                path = new Path(pos, pos.step(facing.getLeft()), facing, map);
                DecisionQueue pathQueue = path.findPath();
                Decision first_step = pathQueue.dequeue();
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
    
    /**
     * Derives a turning decision based on the specified direction.
     *
     * @param drxn the direction to turn towards
     * @return the turning decision corresponding to the specified direction
     * @throws NullPointerException if the specified direction is null
     */
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
