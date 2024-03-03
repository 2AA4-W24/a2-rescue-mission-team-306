package ca.mcmaster.se2aa4.island.team306;

public enum Direction {
    NORTH,
    WEST,
    SOUTH,
    EAST;

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

    public Direction getLeft(){
        switch(this){
            case NORTH:
                return WEST;
            case EAST:
                return NORTH;
            case SOUTH:
                return EAST;
            case WEST:
                return SOUTH;
            default:
                throw new NullPointerException();
        }
    }

    public Direction getRight(){
        switch(this){
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            default:
                throw new NullPointerException();
        }
    }

    public Direction getBackwards(){
        switch(this){
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            default:
                throw new NullPointerException();
        }
    }
}
