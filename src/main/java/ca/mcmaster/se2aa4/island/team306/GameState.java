package ca.mcmaster.se2aa4.island.team306;

public enum GameState {
    SETUP, // Know our initial position in the map
    FIND_ISLAND, // Locate and travel to island
    SEARCH, // Grid-search island until emergency site is found
    BRANCH, // Branch around the emergency site to find the nearest creek
    SUCCESS, // Mission was a success, abort
    FAILURE; // Mission was a failure, abort

    public GameState next(){
        switch(this){
            case SETUP:
                return FIND_ISLAND;
            case FIND_ISLAND:
                return SEARCH;
            case SEARCH:
                return BRANCH;
            case BRANCH:
                return SUCCESS;
            default:
                // No next state for success or failure
                return this;
        }
    }
}
