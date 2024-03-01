package ca.mcmaster.se2aa4.island.team306;

public class Map {
    private Tile tiles[][];
    private Coords base;
    private ParsedResult result;
    
    public Map(){
        this.base = new Coords(0, 0);
        this.result = null;
    }

    public void updateStatus(ParsedResult result){
       this.result = result;
    }

    
}