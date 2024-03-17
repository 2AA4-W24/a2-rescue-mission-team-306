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

    private Drone drone;
    private Map map;
    private DecisionQueue queue;
    private GameTracker tracker;
    private Direction towards;

    public Radar(Drone drone, Map map, DecisionQueue queue, GameTracker tracker){
        this.drone = drone;
        this.map = map;
        this.queue = queue;
        this.tracker = tracker;
        this.towards = drone.getHeading();
    }



    public boolean scan(){
        Direction initHeading = drone.getHeading();
        Coords pos = drone.getPosition();
        Coords right = pos.step(initHeading.getRight());
        Coords left = pos.step(initHeading.getLeft());
        Coords forward = pos.step(initHeading);
        switch(this.tracker.getState()){
            case GameState.SETUP:
                // Queue the left and right
                this.towards = initHeading.getLeft();
                this.queue.enqueue(deriveDecision());
                this.towards = initHeading.getRight();
                this.queue.enqueue(deriveDecision());

                // Return straight for the decider
                this.towards = initHeading;
                return true;
            case GameState.FIND_ISLAND:
            case GameState.SEARCH:
                    if(map.checkCoords(left) == MapValue.UNKNOWN){
                        this.towards = initHeading.getLeft();
                        return true;
                    }
                    if(map.checkCoords(forward) == MapValue.UNKNOWN){
                        this.towards = initHeading;
                        return true;
                    }
                    if(map.checkCoords(right) == MapValue.UNKNOWN){
                        this.towards = initHeading.getRight();
                        return true;
                    }
                    return false;
            default:
                return false;
        }
    }

    public Direction scanTowards(){
        return this.towards;
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
