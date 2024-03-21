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
        switch (tracker.getState()){
            case GameState.SEARCH:
                // Current (end of radar batch) or previous (already on land)
                    if (!map.currentValue().scanned()){
                        return true;
                    }
                    return map.currentValue() == MapValue.UNKNOWN && map.previousValue().isLand();
            default:
                return false;
        }
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
