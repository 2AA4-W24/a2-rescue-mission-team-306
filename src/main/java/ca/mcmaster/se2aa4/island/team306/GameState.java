package ca.mcmaster.se2aa4.island.team306;

/*
 * A game state for the Island game engine.
 */
public interface GameState{
    /*
     * Return the next game state, given the current one.
     * 
     * @returns the next game state.
     */
    GameState next();
}