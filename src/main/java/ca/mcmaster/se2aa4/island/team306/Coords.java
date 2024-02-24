package ca.mcmaster.se2aa4.island.team306;

import java.util.Objects;

public class Coords {
    final public int x;
    final public int y;

    public Coords(int x, int y){
        this.x = x; 
        this.y = y;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }else if(o == null || getClass() != o.getClass()){
            return false;
        }else{
            Coords comp = (Coords) o;
            return (comp.x == this.x) && (comp.y == this.y);
        }
    }

    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }
}
