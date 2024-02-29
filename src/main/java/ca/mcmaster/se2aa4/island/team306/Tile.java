package ca.mcmaster.se2aa4.island.team306;

public class Tile {

    private MapValue type;
    private Coords location;
    private String id; 

    public Tile(MapValue type, Coords location, String id){
        this.type = type;
        this.location = location;
        this.id = id;
    }

    public Tile(MapValue type, Coords location){
        this.type = type;
        this.location = location;
        this.id = null;
    }

    public MapValue getType(){
        return this.type;
    }

    public Coords getLocation(){
        return this.location;
    }

    public String getID(){
        return this.id;
    }
}
