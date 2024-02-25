package ca.mcmaster.se2aa4.island.team306;

public class Coords {
    public int x;
    public int y;

    public Coords(int x, int y){
        this.x = x; 
        this.y = y;
    }

    public Coords offset(int x, int y){
        if (x == 0 && y == 0) return this; //Save memory if no offset
        return new Coords(this.x + x, this.y + y);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Coords)) return false;

        Coords c = (Coords) o;
        return this.x == c.x && this.y == c.y;
    }

    @Override
    public int hashCode(){
        // Picked larger prime than Objects.hash to avoid hash collisions.
        // Picked 2^16 + 1 for bitshift optomization.
        return 65537 * this.x + this.y;
    }
}