package ca.mcmaster.se2aa4.island.team306;

public class GameTracker {
    private GameState state;
    private final Drone drone;
    private final Map map;
    private final DecisionQueue queue;
    private boolean complete_loop_1, complete_loop_2;
    private Coords initial;


    public GameTracker(Drone drone, Map map, DecisionQueue queue){
        this.drone = drone;
        this.map = map;
        this.queue = queue;
        this.state = GameState.SETUP;
        complete_loop_1 = false;
        complete_loop_2 = false;
    }

    public GameState getState(){
        return this.state;
    }

    private void progressState(){

        this.state = this.state.next();
        if (this.state == GameState.SUCCESS){
            succeedMission();
        }
    }

    public void completeLoop(){
        if(state == GameState.FOLLOW_COAST_OUTSIDE){
            complete_loop_1 = true;
        }else{
            complete_loop_2 = true;
        }
    }

    public void succeedMission(){
        this.state = GameState.SUCCESS;
    }

    public void failMission(){
        this.state = GameState.FAILURE;
    }

    private boolean shouldProgress(){
        Coords pos = drone.getPosition();
        Direction heading = drone.getHeading();
        boolean rightCheck = pos.step(heading.getRight()).equals(initial);
        boolean forwardCheck = pos.step(heading).equals(initial);
        boolean LeftCheck = pos.step(heading.getLeft()).equals(initial);
        
        switch (state) {
            case GameState.SETUP:
                return this.queue.isEmpty() && this.map.nextValue() != MapValue.UNKNOWN;
            case GameState.FIND_ISLAND:
                MapValue current = map.checkCoords(drone.getPosition());
                return current.isLand();
            case GameState.FOLLOW_COAST_OUTSIDE:
                return complete_loop_1;
            case GameState.FOLLOW_COAST_INSIDE:
                 return complete_loop_2;
            case GameState.SEARCH:
                Coords site = map.findNearestTile(MapValue.EMERGENCY_SITE);
                return site != null;
            default:
                return false;
        }
    }


    public void update(){
        if (shouldProgress()) {
            progressState();
        }
    }

}
