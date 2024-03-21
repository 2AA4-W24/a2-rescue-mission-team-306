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
        switch(this){
            case SETUP:
                return FIND_ISLAND;
            case FIND_ISLAND:
                return FOLLOW_COAST_OUTSIDE;
            case FOLLOW_COAST_OUTSIDE:
                return FOLLOW_COAST_INSIDE;
            case FOLLOW_COAST_INSIDE:
                return SEARCH;
            case SEARCH:
                return SUCCESS;
            default:
                // No next state for success or failure
                return this;
        }
    }
}
