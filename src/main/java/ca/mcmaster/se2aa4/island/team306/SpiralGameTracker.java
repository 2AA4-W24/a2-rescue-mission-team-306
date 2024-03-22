package ca.mcmaster.se2aa4.island.team306;

public class SpiralGameTracker implements GameTracker{
    private GameState state;
    private final Drone drone;
    private final GameMap map;
    private final DecisionQueue queue;
    private boolean complete_loop_1;
    private boolean complete_loop_2;


    /**
     * Constructs a GameTracker object with the given drone, map, and decision queue.
     *
     * @param drone The drone object used for exploration.
     * @param map The map object representing the island.
     * @param queue The decision queue containing the sequence of decisions to execute.
     */
    public SpiralGameTracker(Drone drone, GameMap map, DecisionQueue queue){
        this.drone = drone;
        this.map = map;
        this.queue = queue;
        this.state = SpiralGameState.SETUP;
        complete_loop_1 = false;
        complete_loop_2 = false;
    }

    /**
     * Retrieves the current state of the exploration game.
     *
     * @return The current state of the game.
     */
    public GameState getState(){
        return this.state;
    }

    private void progressState(){

        this.state = this.state.next();
        if (this.state == SpiralGameState.SUCCESS){
            succeedMission();
        }
    }

    public void completeLoop(){
        if(state == SpiralGameState.FOLLOW_COAST_OUTSIDE){
            complete_loop_1 = true;
        }else{
            complete_loop_2 = true;
        }
    }

    public void succeedMission(){
        this.state = SpiralGameState.SUCCESS;
    }

    public void failMission(){
        this.state = SpiralGameState.FAILURE;
    }

    private boolean shouldProgress(){

        return switch (state) {
            case SpiralGameState.SETUP -> this.queue.isEmpty() && this.map.nextValue() != SpiralMapValue.UNKNOWN;
            case SpiralGameState.FIND_ISLAND -> {
                MapValue current = map.checkCoords(drone.getPosition());
                yield current.isLand();
            }
            case SpiralGameState.FOLLOW_COAST_OUTSIDE -> complete_loop_1;
            case SpiralGameState.FOLLOW_COAST_INSIDE -> complete_loop_2;
            case SpiralGameState.SEARCH -> {
                Coords site = map.findNearestTile(SpiralMapValue.EMERGENCY_SITE);
                yield site != null;
            }
            default -> false;
        };
    }


    public void update(){
        if (shouldProgress()) {
            progressState();
        }
    }

}
