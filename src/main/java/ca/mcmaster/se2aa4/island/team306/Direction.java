package ca.mcmaster.se2aa4.island.team306;

public enum Direction {
    NORTH,
    WEST,
    SOUTH,
    EAST;

    /**
     * Converts the direction to its corresponding character representation.
     *
     * @return The character representation of the direction.
     */
    public char toChar(){
        switch(this){
            case NORTH:
                return 'N';
            case SOUTH:
                return 'S';
            case EAST:
                return 'E';
            case WEST:
                return 'W';
            default:
                throw new AssertionError();
        }
    }

    /**
     * Creates a Direction enum from its character representation.
     *
     * @param c The character representing the direction.
     * @return The Direction enum corresponding to the character.
     * @throws IllegalArgumentException if the character is not recognized.
     */
    public static Direction fromChar(char c){
        switch(c){
            case 'N':
            case 'n':
                return NORTH;
            case 'W':
            case 'w':
                return WEST;
            case 'S':
            case 's':
                return SOUTH;
            case 'E':
            case 'e':
                return EAST;
            default:
                throw new IllegalArgumentException("Unrecognized character abbreviation");
        }
    }

    /**
     * Gets the direction to the left of the current direction.
     *
     * @return The direction to the left.
     * @throws AssertionError if the direction is not recognized.
     */
    public Direction getLeft() {
        switch(this){
            case NORTH:
                return WEST;
            case WEST:
                return SOUTH;
            case SOUTH:
                return EAST;
            case EAST:
                return NORTH;
        }
        throw new AssertionError();
    }

    /**
     * Gets the direction opposite to the current direction.
     *
     * @return The opposite direction.
     * @throws AssertionError if the direction is not recognized.
     */
    public Direction getBackwards() {
        switch(this){
            case NORTH:
                return SOUTH;
            case WEST:
                return EAST;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
        }
        throw new AssertionError();
    }

    /**
     * Gets the direction to the right of the current direction.
     *
     * @return The direction to the right.
     * @throws AssertionError if the direction is not recognized.
     */
    public Direction getRight() {
        switch(this){
            case NORTH:
                return EAST;
            case WEST:
                return NORTH;
            case SOUTH:
                return WEST;
            case EAST:
                return SOUTH;
        }
        throw new AssertionError();
    }
}
