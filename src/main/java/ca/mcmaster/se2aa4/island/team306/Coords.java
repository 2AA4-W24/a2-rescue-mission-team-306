package ca.mcmaster.se2aa4.island.team306;

public class Coords {
    public final int x;
    public final int y;

    public Coords(int x, int y){
        this.x = x; 
        this.y = y;
    }

    public double distance(Coords other){
        return Math.sqrt(Math.pow((other.x - this.x), 2) + Math.pow((other.y - this.y), 2));
    }

    public Coords offset(int xOffset, int yOffset){
        if (xOffset == 0 && yOffset == 0) return this;
        return new Coords(this.x + xOffset, this.y + yOffset);
    }

    @Override
    public boolean equals(Object o){
        if (this == o)  return true;
        if (!(o instanceof Coords)){
            return false;
        }
        Coords c = (Coords) o;
        return c.x == this.x && c.y == this.y;
    }

    @Override
    public int hashCode(){
        return 65537 * this.x + this.y;
    }

    public Coords step(Direction direction){
        switch(direction){
            case NORTH:
                return this.offset(0, 1);
            case SOUTH:
                return this.offset(0, -1);
            case EAST:
                return this.offset(1, 0);
            case WEST:
                return this.offset(-1, 0);
            default:
                throw new NullPointerException();
        }
    }
}