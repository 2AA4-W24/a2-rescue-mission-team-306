package ca.mcmaster.se2aa4.island.team306;

public class Mover {
    private Drone drone;
    private Map map;

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

    public Mover(){}

    private boolean shouldMove(){
        return false;
    }

    public boolean move(){
        if (shouldMove()){
            Direction d = goTowards();
            if (d == drone.getHeading().getBackwards()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public Direction goTowards(){
        return Direction.SOUTH;
    }

    public Decision deriveDecision(){
        Direction drxn = this.goTowards();
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
