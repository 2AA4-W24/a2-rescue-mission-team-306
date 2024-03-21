package ca.mcmaster.se2aa4.island.team306;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class GameTracker {
    private GameState state;
    private Drone drone;
    private Map map;
    private DecisionQueue queue;

    /**
     * Constructs a GameTracker object with the given drone, map, and decision queue.
     *
     * @param drone The drone object used for exploration.
     * @param map The map object representing the island.
     * @param queue The decision queue containing the sequence of decisions to execute.
     */
    public GameTracker(Drone drone, Map map, DecisionQueue queue){
        this.drone = drone;
        this.map = map;
        this.queue = queue;
        this.state = GameState.SETUP;
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
        if (this.state == GameState.SUCCESS){
            succeedMission();
        }
    }

    public void succeedMission(){
        this.state = GameState.SUCCESS;
    }

    public void failMission(){
        this.state = GameState.FAILURE;
    }

    private boolean shouldProgress(){
        switch (state) {
            case GameState.SETUP:
                return this.queue.isEmpty() && this.map.nextValue() != MapValue.UNKNOWN;
            case GameState.FIND_ISLAND:
                MapValue current = map.checkCoords(drone.getPosition());
                return current.isLand();
            case GameState.SEARCH:
                // MapValue goal = MapValue.EMERGENCY_SITE; // Switch to emergency site after MVP
                // return this.map.findNearestTile(goal) != null;
            default:
                return false;
        }
    }


    private void testBounds(){
        if (state == GameState.SETUP) {
            Logger logger = LogManager.getLogger();
            for (Direction direction : Direction.values()) {
                Integer boundBox = map.getBound(direction);
                int bound = boundBox != null ? boundBox : Integer.MIN_VALUE;
                logger.info(String.format("%c: %d", direction.toChar(), bound));
            }
        }
    }

    public void update(){
        if (shouldProgress()) {
            // Uncomment to test bounds
            //testBounds();
            progressState();
        }
    }

}
