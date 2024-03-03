package ca.mcmaster.se2aa4.island.team306;

import java.security.KeyException;
import java.util.HashMap;

public class Map {
    private HashMap<Coords, Tile> mapping;
    private Drone drone;
    private ParsedResult result;
    
    public Map(Drone drone){
        this.mapping = new HashMap<>();
        Coords start = drone.getStartPosition();
        this.mapping.put(start, new Tile(MapValue.OCEAN, start));
        this.drone = drone;
        this.result = null;
    }

    public void updateStatus(ParsedResult result){
       this.result = result;
       this.updateTiles();
    }

    private void updateTiles(){
        Decision resultDecision = this.result.getDecision();
        if (resultDecision == Decision.RADAR || resultDecision == Decision.PHOTO){
            Coords pos = this.drone.getPosition();
            Direction dir = this.result.getDirection();
            if (resultDecision == Decision.RADAR){
                for (int i = 0; i < this.result.getRange(); i++){
                    pos = pos.step(dir);
                    this.mapping.put(pos, new Tile(MapValue.OCEAN, pos));
                }
                if (result.hasLand()){
                    pos = pos.step(dir);
                    this.mapping.put(pos, new Tile(MapValue.GROUND, pos));
                }
            }
            else {
                this.mapping.put(pos, new Tile(
                    result.getValues().get(0), pos, result.getID()));
            }
        }
    }

    public Tile tileAt(Coords coords){
        if (this.mapping.containsKey(coords)){
            return this.mapping.get(coords);
        }
        throw new IllegalArgumentException();
    }

    public Tile currentTile(){
        return tileAt(this.drone.getPosition());
    }

    
}