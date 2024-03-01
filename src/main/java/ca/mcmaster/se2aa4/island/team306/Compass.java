package ca.mcmaster.se2aa4.island.team306;

public class Compass {
    public Compass() {

    }
    public Direction turnLeft(Direction dir) {
        swtich(dir){
            case NORTH:
                return Direction.WEST;
            case SOUTH:
                return Direction.EAST;
            case EAST:
                return Direction.NORTH;
            case WEST:
                return Direction.SOUTH;
            default:
                return dir;
        }
    }
    public void turnRight(Direction dir) {
    
    }
}
