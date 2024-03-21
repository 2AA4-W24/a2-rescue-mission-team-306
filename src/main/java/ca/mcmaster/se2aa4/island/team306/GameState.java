package ca.mcmaster.se2aa4.island.team306;

public enum GameState {
    SETUP, // Know our initial position in the map
    FIND_ISLAND, // Locate and travel to island
    FOLLOW_COAST_OUTSIDE,
    FOLLOW_COAST_INSIDE,
    SEARCH, // Grid-search island until emergency site is found
    SUCCESS, // Mission was a success, abort
    FAILURE; // Mission was a failure, abort

    public GameState next(){
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
