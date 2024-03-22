package ca.mcmaster.se2aa4.island.team306;

public enum Direction {
    NORTH,
    WEST,
    SOUTH,
    EAST;

    public char toChar(){
        return switch (this) {
            case NORTH -> 'N';
            case SOUTH -> 'S';
            case EAST -> 'E';
            case WEST -> 'W';
            default -> throw new AssertionError();
        };
    }

    public static Direction fromChar(char c){
        return switch (c) {
            case 'N', 'n' -> NORTH;
            case 'W', 'w' -> WEST;
            case 'S', 's' -> SOUTH;
            case 'E', 'e' -> EAST;
            default -> throw new IllegalArgumentException("Unrecognized character abbreviation");
        };
    }

    public Direction getLeft() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    public Direction getBackwards() {
        return switch (this) {
            case NORTH -> SOUTH;
            case WEST -> EAST;
            case SOUTH -> NORTH;
            case EAST -> WEST;
        };
    }

    public Direction getRight() {
        return switch (this) {
            case NORTH -> EAST;
            case WEST -> NORTH;
            case SOUTH -> WEST;
            case EAST -> SOUTH;
        };
    }
}
