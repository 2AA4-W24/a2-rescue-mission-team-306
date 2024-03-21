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
    private DecisionQueue scanQueue;

    public Radar(Drone drone, Map map, DecisionQueue queue, GameTracker tracker){ //constructor
        this.drone = drone;
        this.map = map;
        this.queue = queue;
        this.tracker = tracker;
        this.towards = drone.getHeading();
        this.scanQueue = new DecisionQueue();
    }

    /**
     * Performs radar scanning based on the current game state.
     *
     * @return true if scanning is needed based on the game state, false otherwise.
     */
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
                scanQueue.clear();
                    if(map.checkCoords(left) == MapValue.UNKNOWN){
                        this.towards = initHeading.getLeft();
                        scanQueue.enqueue(deriveDecision());
                    }
                    if(map.checkCoords(forward) == MapValue.UNKNOWN){
                        this.towards = initHeading;
                        scanQueue.enqueue(deriveDecision());
                    }
                    if(map.checkCoords(right) == MapValue.UNKNOWN){
                        this.towards = initHeading.getRight();
                        scanQueue.enqueue(deriveDecision());
                    }
                    if(scanQueue.isEmpty()){
                        return false;
                    }
                    this.towards = scanQueue.dequeue().getDirection();
                    queue.enqueue(scanQueue);
                    return true;
                        
            default:
                return false;
        }
    }

    /**
     * Returns the direction towards which the radar is scanning.
     *
     * @return The direction towards which the radar is scanning.
     */
    public Direction scanTowards(){
        return this.towards;
    }

    /**
     * Derives the decision associated with the current scanning direction.
     *
     * @return The decision object for radar scanning in the current direction.
     */
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
