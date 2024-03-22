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
        return switch (this) {
            case NORTH -> 'N';
            case SOUTH -> 'S';
            case EAST -> 'E';
            case WEST -> 'W';
            default -> throw new AssertionError();
        };
    }

    /**
     * Creates a Direction enum from its character representation.
     *
     * @param c The character representing the direction.
     * @return The Direction enum corresponding to the character.
     * @throws IllegalArgumentException if the character is not recognized.
     */
    public static Direction fromChar(char c){
        return switch (c) {
            case 'N', 'n' -> NORTH;
            case 'W', 'w' -> WEST;
            case 'S', 's' -> SOUTH;
            case 'E', 'e' -> EAST;
            default -> throw new IllegalArgumentException("Unrecognized character abbreviation");
        };
    }

    /**
     * Gets the direction to the left of the current direction.
     *
     * @return The direction to the left.
     * @throws AssertionError if the direction is not recognized.
     */
    public Direction getLeft() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    /**
     * Gets the direction opposite to the current direction.
     *
     * @return The opposite direction.
     * @throws AssertionError if the direction is not recognized.
     */
    public Direction getBackwards() {
        return switch (this) {
            case NORTH -> SOUTH;
            case WEST -> EAST;
            case SOUTH -> NORTH;
            case EAST -> WEST;
        };
    }

    /**
     * Gets the direction to the right of the current direction.
     *
     * @return The direction to the right.
     * @throws AssertionError if the direction is not recognized.
     */
    public Direction getRight() {
        return switch (this) {
            case NORTH -> EAST;
            case WEST -> NORTH;
            case SOUTH -> WEST;
            case EAST -> SOUTH;
        };
    }
}
