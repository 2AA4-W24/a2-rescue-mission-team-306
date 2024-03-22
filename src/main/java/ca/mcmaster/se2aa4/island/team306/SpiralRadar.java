package ca.mcmaster.se2aa4.island.team306;

public class SpiralRadar implements RadarScanner{
    public static final SpiralDecision SCAN_NORTH = 
        new SpiralDecision(SpiralDecisionType.RADAR, Direction.NORTH);
    public static final SpiralDecision SCAN_EAST = 
        new SpiralDecision(SpiralDecisionType.RADAR, Direction.EAST);
    public static final SpiralDecision SCAN_SOUTH = 
        new SpiralDecision(SpiralDecisionType.RADAR, Direction.SOUTH);
    public static final SpiralDecision SCAN_WEST = 
        new SpiralDecision(SpiralDecisionType.RADAR, Direction.WEST);

    private final Drone drone;
    private final GameMap map;
    private final DecisionQueue queue;
    private final GameTracker tracker;
    private Direction towards;
    private final DecisionQueue scanQueue;

    public SpiralRadar(Drone drone, GameMap map, DecisionQueue queue, GameTracker tracker){ //constructor
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
            case SpiralGameState.SETUP:
                // Queue the left and right
                this.towards = initHeading.getLeft();
                this.queue.enqueue(deriveDecision());
                this.towards = initHeading.getRight();
                this.queue.enqueue(deriveDecision());

                // Return straight for the decider
                this.towards = initHeading;
                return true;
            case SpiralGameState.FIND_ISLAND:
            case SpiralGameState.FOLLOW_COAST_OUTSIDE:
            case SpiralGameState.FOLLOW_COAST_INSIDE:
            case SpiralGameState.SEARCH:
                scanQueue.clear();
                    if(map.checkCoords(left) == SpiralMapValue.UNKNOWN){
                        this.towards = initHeading.getLeft();
                        scanQueue.enqueue(deriveDecision());
                    }
                    if(map.checkCoords(forward) == SpiralMapValue.UNKNOWN){
                        this.towards = initHeading;
                        scanQueue.enqueue(deriveDecision());
                    }
                    if(map.checkCoords(right) == SpiralMapValue.UNKNOWN){
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
    public SpiralDecision deriveDecision(){
       return deriveDecision(scanTowards());
    }

    public static SpiralDecision deriveDecision(Direction direction){
        return switch (direction) {
            case Direction.NORTH -> SCAN_NORTH;
            case Direction.EAST -> SCAN_EAST;
            case Direction.SOUTH -> SCAN_SOUTH;
            case Direction.WEST -> SCAN_WEST;
        };
    }

    
}
