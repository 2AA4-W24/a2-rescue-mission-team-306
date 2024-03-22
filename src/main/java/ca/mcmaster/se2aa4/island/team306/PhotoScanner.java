package ca.mcmaster.se2aa4.island.team306;

public class PhotoScanner implements Scanner{
    private static final Decision DECISION = new Decision(DecisionType.PHOTO);
    private GameTracker tracker;
    private Map map;

    /**
     * Constructs a new PhotoScanner object.
     *
     * @param map The map to be scanned.
     * @param tracker The game tracker to be used.
     */
    public PhotoScanner(Map map, GameTracker tracker){
        this.map = map;
        this.tracker = tracker;
    }

    /**
     * Scans the environment based on the current game state.
     *
     * @return true if scanning is needed based on the game state, false otherwise.
     */
    public boolean scan(){
        return switch (tracker.getState()) {
            case GameState.FOLLOW_COAST_OUTSIDE, GameState.FOLLOW_COAST_INSIDE, GameState.SEARCH ->
                // Scan unless current value is already scanned
                    !map.currentValue().scanned();
            default -> false;
        };
    }

    /**
     * Gets the decision associated with photo scanning.
     *
     * @return The decision object for photo scanning.
     */
    public static Decision getDecision(){
        return DECISION;
    }
}
