package ca.mcmaster.se2aa4.island.team306;

public class Mover {
    private Drone drone;
    private Map map;
    private DecisionQueue queue;
    private GameTracker tracker;
    private Direction start_orient;

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
        this.start_orient = drone.getHeading();
    }

    private boolean shouldMove(){
        return true;
    }

    public boolean move(){
        if (shouldMove()){
            Direction d = goTowards();
            if (d == drone.getHeading().getBackwards()){
                return false;
            }
            return true;
        }
        return false;
    }

    public Direction goTowards(){
        Coords pos = drone.getPosition();
        Direction facing = drone.getHeading();
        if(start_orient == facing){
            if(map.checkCoords(pos.step(facing.getRight())) == MapValue.OUT_OF_RANGE){
                return facing.getLeft();
            }
            return facing.getRight();
        }

        if(facing.getLeft() == start_orient){
            return facing.getLeft();
        }
        return facing.getRight();
    }

    public Decision deriveDecision(){
        Direction drxn = this.goTowards();
        drone.move(goTowards());
        if(drxn == drone.getHeading()){
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
        else {
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


}
