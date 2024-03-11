package ca.mcmaster.se2aa4.island.team306;

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
                MapValue goal = MapValue.CREEK; // Switch to emergency site after MVP
                return this.map.findNearestTile(goal) != null;
            case GameState.BRANCH:
                return true; // Switch to has found closest creek
            default:
                return false;
        }
    }

    public void update(){
        if (shouldProgress()){
            progressState();
        }
    }

}
