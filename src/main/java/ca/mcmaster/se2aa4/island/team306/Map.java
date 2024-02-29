package ca.mcmaster.se2aa4.island.team306;

public class Map {
    private Tile tiles[][];
    private Coords base;
    private ParsedResult results;
    
    public Map(){
        this.base = new Coords(0, 0);
        this.results = null;
    }

    public void updateStatus(ParsedResult result){
       this.results = result;
    }

    
}
