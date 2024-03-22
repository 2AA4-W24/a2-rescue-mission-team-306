package ca.mcmaster.se2aa4.island.team306;

public class Tile {

    private MapValue type;
    private Coords location;
    private String id; 

    /**
     * Constructs a tile with the specified type, location, and ID.
     *
     * @param type     The type of the tile.
     * @param location The location of the tile on the map.
     * @param id       The unique identifier of the tile.
     */
    public Tile(MapValue type, Coords location, String id){
        this.type = type;
        this.location = location;
        this.id = id;
    }

    /**
     * Constructs a tile with the specified type and location.
     *
     * @param type     The type of the tile.
     * @param location The location of the tile on the map.
     */
    public Tile(MapValue type, Coords location){
        this.type = type;
        this.location = location;
        this.id = null;
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile.
     */
    public MapValue getType(){
        return this.type;
    }

    /**
     * Gets the location of the tile on the map.
     *
     * @return The location of the tile.
     */
    public Coords getLocation(){
        return this.location;
    }

    /**
     * Gets the unique identifier of the tile.
     *
     * @return The unique identifier of the tile.
     */
    public String getID(){
        return this.id;
    }
}