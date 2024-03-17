package ca.mcmaster.se2aa4.island.team306;

public class Mover {
    private final Drone drone;
    private final Map map;
    private final DecisionQueue queue;
    private final GameTracker tracker;
    private final Direction START_ORIENT;

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
    }

    private boolean shouldMove(){
        switch(tracker.getState()){
            case SETUP:
            case SUCCESS:
            case FAILURE:
                return false;
            case FIND_ISLAND:
            case SEARCH:
            case BRANCH:
            default:
                return true;

        }
    }

    public boolean move(){
        if (shouldMove()){
            Direction d = goTowards();
            if (d == drone.getHeading().getBackwards()){
                return false;
            }
            Coords pos = drone.getPosition();
            Direction facing = drone.getHeading();
            if(START_ORIENT == facing) {
                if (map.checkCoords(pos.step(facing.getRight())) == MapValue.OUT_OF_RANGE) {
                    Direction direction = facing.getLeft();
                    queue.enqueue(deriveTurn(direction));
                    direction = direction.getLeft();
                    queue.enqueue(deriveTurn(direction));
                    direction = direction.getRight();
                    queue.enqueue(deriveTurn(direction));
                } else {
                    Direction direction = facing.getRight();
                    queue.enqueue(deriveTurn(direction));
                    direction = direction.getRight();
                    queue.enqueue(deriveTurn(direction));
                    direction = direction.getLeft();
                    queue.enqueue(deriveTurn(direction));
                }
            }
            return true;
        }
        return false;
    }

    public Direction goTowards(){
        switch(tracker.getState()){
            case FIND_ISLAND:
                return START_ORIENT;
            case SEARCH:
                Coords pos = drone.getPosition();
                Direction facing = drone.getHeading();
                if(map.checkCoords(pos.step(facing).step(facing)) == MapValue.OUT_OF_RANGE){
                    return START_ORIENT;
                }
                return facing; // The queue will prevent forever loop
            default:
                return START_ORIENT;
        }
    }

    public Decision deriveDecision() {
        Direction drxn = this.goTowards();
        return deriveDecision(drxn);
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
