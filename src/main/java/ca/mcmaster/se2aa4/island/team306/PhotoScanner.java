package ca.mcmaster.se2aa4.island.team306;

public class PhotoScanner implements Scanner{
    private static final Decision DECISION = new Decision(DecisionType.PHOTO);
    private GameTracker tracker;
    private Map map;

    public PhotoScanner(Map map, GameTracker tracker){
        this.map = map;
        this.tracker = tracker;
    }

    public boolean scan(){
        switch(tracker.getState()){
            case GameState.SEARCH:
                // Current (end of radar batch) or previous (already on land)
                return map.currentValue() == MapValue.GROUND || 
                    map.previousValue() == MapValue.GROUND;
            case GameState.BRANCH:
                // No radar in branch state; photo is only option
                return true;
            default:
                return false;
        }
    }

    public static Decision getDecision(){
        return DECISION;
    }
}
