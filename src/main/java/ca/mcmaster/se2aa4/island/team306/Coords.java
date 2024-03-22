package ca.mcmaster.se2aa4.island.team306;

public class Coords {
    public final int x;
    public final int y;

    public Coords(int x, int y){
        this.x = x; 
        this.y = y;
    }

    /**
     * Calculates the Euclidean distance between this Coords object and another Coords object.
     *
     * @param other The other Coords object.
     * @return The distance between the two Coords objects.
     */
    public double distance(Coords other){
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }

    /**
     * Returns a new Coords object with the specified offsets from this Coords object.
     *
     * @param xOffset The offset in the x-direction.
     * @param yOffset The offset in the y-direction.
     * @return The new Coords object with the specified offsets.
     */
    public Coords offset(int xOffset, int yOffset){
        if (xOffset == 0 && yOffset == 0) {
            return this;
        }
        return new Coords(this.x + xOffset, this.y + yOffset);
    }

    /**
     * Checks if this Coords object is equal to another object.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o){
        if (this == o)  {
            return true;
        }
        if (!(o instanceof Coords c)){
            return false;
        }
        return c.x == this.x && c.y == this.y;
    }

    /**
     * Returns the hash code value for this Coords object.
     *
     * @return The hash code value for this Coords object.
     */
    @Override
    public int hashCode(){
        return 65537 * this.x + this.y;
    }

    /**
     * Moves this Coords object one step in the specified direction.
     *
     * @param heading The direction to move.
     * @return The new Coords object after moving in the specified direction.
     */
    public Coords step(Direction heading) {
        return switch (heading) {
            case Direction.NORTH -> offset(0, 1);
            case Direction.WEST -> offset(-1, 0);
            case SOUTH -> offset(0, -1);
            case EAST -> offset(1, 0);
        };
    }


    /**
     * Returns a string representation of this Coords object.
     *
     * @return The string representation of this Coords object.
     */
    @Override
    public String toString(){
        return String.format("(%d, %d)", this.x, this.y);
    }
}