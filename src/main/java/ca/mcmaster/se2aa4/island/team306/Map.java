package ca.mcmaster.se2aa4.island.team306;

import java.util.HashMap;

public class Map {
    HashMap<Coords, Tile> tiles;
    Coords base;
    
    public Map(Coords base) {
        this.base = base;
        this.tiles = new HashMap<>();
    }

    public void updateStatus(String results) {
        
    }

    public void addTile(MapValue type, Coords pos, String id) {
        Tile tile = new Tile(type, pos, id);
        tiles.put(pos, tile);
    }

    public void addTile(MapValue type, Coords pos) {
        Tile tile = new Tile(type, pos);
        tiles.put(pos, tile);
    }

}
