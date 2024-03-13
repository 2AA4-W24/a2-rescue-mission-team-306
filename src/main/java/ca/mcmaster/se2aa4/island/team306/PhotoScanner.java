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
            case GameState.SEARCH ->
                // Current (end of radar batch) or previous (already on land)
                    map.currentValue() == MapValue.GROUND ||
                            map.previousValue().isLand();
            case GameState.BRANCH ->
                // Scan unless tile is known (radar is unavailable in branch state)
                    map.currentValue() == MapValue.UNKNOWN || map.currentValue() == MapValue.GROUND;
            default -> false;
        };
    }

    public static Decision getDecision(){
        return DECISION;
    }
}
