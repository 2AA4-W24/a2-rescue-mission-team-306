package ca.mcmaster.se2aa4.island.team306;

public class SpiralPhotoScanner implements PhotoScanner{
    private static final SpiralDecision DECISION = new SpiralDecision(SpiralDecisionType.PHOTO);
    private final GameTracker tracker;
    private final GameMap map;

    /**
     * Constructs a new PhotoScanner object.
     *
     * @param map The map to be scanned.
     * @param tracker The game tracker to be used.
     */
    public SpiralPhotoScanner(GameMap map, GameTracker tracker){
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
            case SpiralGameState.FOLLOW_COAST_OUTSIDE, SpiralGameState.FOLLOW_COAST_INSIDE, SpiralGameState.SEARCH ->
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
    public static SpiralDecision getDecision(){
        return DECISION;
    }
}
