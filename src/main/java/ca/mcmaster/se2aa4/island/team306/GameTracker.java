package ca.mcmaster.se2aa4.island.team306;

public interface GameTracker {
    GameState getState();
    void succeedMission();
    void failMission();
    void update();
}
