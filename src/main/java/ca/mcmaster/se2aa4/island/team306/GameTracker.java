package ca.mcmaster.se2aa4.island.team306;

public class GameTracker {
    private GameState state;

    public GameTracker(){
        this.state = GameState.SETUP;
    }

    public GameState getState(){
        return this.state;
    }

    public void progressState(){
        this.state = this.state.next();
    }

    public void succeedMission(){
        this.state = GameState.SUCCESS;
    }

    public void failMission(){
        this.state = GameState.FAILURE;
    }

}
