package ca.mcmaster.se2aa4.island.team306;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class GameTracker {
    private GameState state;
    private Drone drone;
    private Map map;
    private DecisionQueue queue;

    public GameTracker(Drone drone, Map map, DecisionQueue queue){
        this.drone = drone;
        this.map = map;
        this.queue = queue;
        this.state = GameState.SETUP;
    }

    public GameState getState(){
        return this.state;
    }

    private void progressState(){

        this.state = this.state.next();
        if (this.state == GameState.BRANCH){
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
            case GameState.SEARCH:
                MapValue goal = MapValue.EMERGENCY_SITE; // Switch to emergency site after MVP
                return this.map.findNearestTile(goal) != null;
            case GameState.BRANCH:
                return true; // Switch to has found the closest creek
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
