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
}
