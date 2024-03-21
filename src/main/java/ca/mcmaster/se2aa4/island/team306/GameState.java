package ca.mcmaster.se2aa4.island.team306;

public enum GameState {
    SETUP, // Know our initial position in the map
    FIND_ISLAND, // Locate and travel to island
    SEARCH, // Grid-search island until emergency site is found
    SUCCESS, // Mission was a success, abort
    FAILURE; // Mission was a failure, abort

    /**
     * Returns the next state in the sequence of states.
     *
     * @return The next state in the sequence, or the same state if no next state is defined.
     */
    public GameState next(){
        switch(this){
            case SETUP:
                return FIND_ISLAND;
            case FIND_ISLAND:
                return SEARCH;
            case SEARCH:
                return SUCCESS;
            default:
                // No next state for success or failure
                return this;
        }
    }
}
