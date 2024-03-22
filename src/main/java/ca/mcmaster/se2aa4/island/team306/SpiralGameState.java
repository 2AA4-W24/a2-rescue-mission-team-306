package ca.mcmaster.se2aa4.island.team306;

public enum SpiralGameState implements GameState{
    SETUP, // Know our initial position in the map
    FIND_ISLAND, // Locate and travel to island
    FOLLOW_COAST_OUTSIDE,
    FOLLOW_COAST_INSIDE,
    SEARCH, // Grid-search island until emergency site is found
    SUCCESS, // Mission was a success, abort
    FAILURE; // Mission was a failure, abort

    /**
     * Returns the next state in the sequence of states.
     *
     * @return The next state in the sequence, or the same state if no next state is defined.
     */
    public SpiralGameState next(){
        return switch (this) {
            case SETUP -> FIND_ISLAND;
            case FIND_ISLAND -> FOLLOW_COAST_OUTSIDE;
            case FOLLOW_COAST_OUTSIDE -> FOLLOW_COAST_INSIDE;
            case FOLLOW_COAST_INSIDE -> SEARCH;
            case SEARCH -> SUCCESS;
            default ->
                // No next state for success or failure
                    this;
        };
    }
}
