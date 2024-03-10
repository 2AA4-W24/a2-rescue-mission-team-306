package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map {
    private HashMap<Coords, Tile> tiles;
    private Coords base;
    private ParsedResult result;
    private Drone drone;
    
    public Map(Drone drone){
        this.base = new Coords(0, 0);
        this.result = null;
        this.tiles = new HashMap<>();
        this.drone = drone;
    }

    public void updateStatus(ParsedResult result){
        this.result = result;
        Coords pos = drone.getPosition();
        Direction drxn = result.getDirection();
        List<MapValue> values = result.getValues();
        if (result.getDecision() == Decision.RADAR){
            for(int i = 0; i<values.size(); i++){
                switch (drxn) {
                    case Direction.NORTH:
                        pos = pos.offset(0, 1);
                        break;
                    case Direction.WEST:
                        pos = pos.offset(-1, 0);
                        break;
                    case Direction.SOUTH:
                        pos = pos.offset(0, -1);
                        break;
                    case Direction.EAST:
                        pos = pos.offset(1, 0);
                        break;
                    default:
                        throw new NullPointerException("Null Direction");
                }

                addTile(new Tile(values.get(i), pos));
            }
        }else if(result.getDecision() == Decision.PHOTO){
            addTile(new Tile(values.get(0), pos));
        }
    }

    private void addTile(Tile tile){
        tiles.put(tile.getLocation(), tile);
    }

    public MapValue checkCoords(Coords loc){
        return tiles.get(loc).getType();
    }

    public Coords findNearestTile(MapValue val){
        List<Coords> matches = findTile(val);
        if(matches.isEmpty()){
            return null;
        }

        Coords closest = matches.get(0);
        Coords pos = drone.getPosition();
        double d1 = pos.distance(closest);
        double d2;

        for(Coords coord: matches){
            d2 = pos.distance(coord);
            if(d2<d1){
                d1 = d2;
                closest = coord;
            }
        }

        return closest;
    }

    private List<Coords> findTile(MapValue val){
        List<Coords> matches = new ArrayList<>();
        for(Tile tile: tiles.values()){
            if(tile.getType() == val){
                matches.add(tile.getLocation());
            }
        }

        return matches;
    }

    public Coords getBase(){
        return this.base;
    }
    
}