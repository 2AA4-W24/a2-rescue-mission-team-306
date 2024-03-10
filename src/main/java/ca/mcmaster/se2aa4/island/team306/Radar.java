package ca.mcmaster.se2aa4.island.team306;

public class Radar implements Scanner{
    public static final Decision SCAN_NORTH = 
        new Decision(DecisionType.RADAR, Direction.NORTH);
    public static final Decision SCAN_EAST = 
        new Decision(DecisionType.RADAR, Direction.EAST);
    public static final Decision SCAN_SOUTH = 
        new Decision(DecisionType.RADAR, Direction.SOUTH);
    public static final Decision SCAN_WEST = 
        new Decision(DecisionType.RADAR, Direction.WEST);



    public boolean scan(){
        return false;
    }

    public Direction scanTowards(){
        return Direction.SOUTH;
    }

    public Decision deriveDecision(){
       switch(scanTowards()){
            case Direction.NORTH:
                return SCAN_NORTH;
            case Direction.EAST:
                return SCAN_EAST;
            case Direction.SOUTH:
                return SCAN_SOUTH;
            case Direction.WEST:
                return SCAN_WEST;
            default:
                throw new NullPointerException();
       }
    }

    
}
