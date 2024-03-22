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
        return switch (tracker.getState()) {
            case GameState.FOLLOW_COAST_OUTSIDE, GameState.FOLLOW_COAST_INSIDE, GameState.SEARCH ->
                // Scan unless current value is already scanned
                    !map.currentValue().scanned();
            default -> false;
        };
    }

    public static Decision getDecision(){
        return DECISION;
    }
}
