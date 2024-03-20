package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map {
    private final java.util.Map<Coords, Tile> tiles;
    private final Coords base;
    private final Drone drone;
    private final java.util.Map<Direction, Integer> bounds;

    private final ReportGenerator generator;
    
    public Map(Drone drone, ReportGenerator generator){
        this.base = new Coords(0, 0);
        this.tiles = new HashMap<>();
        this.drone = drone;
        this.bounds = new HashMap<>();
        this.generator = generator;
        initBounds();
    }

    private void initBounds(){
        Direction backwards = this.drone.getHeading().getBackwards();
        int value = (backwards == Direction.NORTH || backwards == Direction.SOUTH) ?
                this.base.y : this.base.x;
        this.bounds.put(backwards, value);
    }

    public void updateStatus(ParsedResult result){
        Coords pos = drone.getPosition();
        Direction drxn = result.getDirection();
        List<MapValue> values = result.getValues();
        String id = result.getID();
        if (result.getType() == DecisionType.RADAR){
            for (MapValue value : values) {
                pos = pos.step(drxn);
                addTile(new Tile(value, pos));
            }
        }else if(result.getType() == DecisionType.PHOTO){
            MapValue value = values.getFirst();
            MapValue prev = checkCoords(pos);
            if(prev == MapValue.GROUND && value == MapValue.SCANNED_OCEAN){
                value = MapValue.REGULAR_LAND;
            }else if(prev == MapValue.OCEAN && value == MapValue.REGULAR_LAND){
                value = MapValue.SCANNED_OCEAN;
            }
            addTile(new Tile(value, pos, id));
        }

        updateBounds(result);
    }

    public void updateBounds(ParsedResult result){
        if (result.getType() != DecisionType.RADAR){
            return;
        }
        Direction direction = result.getDirection();
        if (result.foundLand() || this.bounds.containsKey(direction)) {
            return;
        }
        int range = result.getRange();
        Coords boundsLoc = drone.getPosition();
        for (int i = 0; i < range; i++){
            boundsLoc = boundsLoc.step(direction);
        }
        int value = direction == Direction.NORTH || direction == Direction.SOUTH ? boundsLoc.y : boundsLoc.x;
        this.bounds.put(direction, value);
    }

    private void addTile(Tile tile){
        tiles.put(tile.getLocation(), tile);
    }

    private boolean inRange(Coords loc){
        Integer northBox = bounds.get(Direction.NORTH);
        Integer eastBox = bounds.get(Direction.EAST);
        Integer southBox = bounds.get(Direction.SOUTH);
        Integer westBox = bounds.get(Direction.WEST);

        if (northBox != null && loc.y > northBox){
            return false;
        }
        if (eastBox != null && loc.x > eastBox){
            return false;
        }
        if (southBox != null && loc.y < southBox){
            return false;
        }
        return westBox == null || loc.x >= westBox;
    }

    public MapValue checkCoords(Coords loc){
        Tile tile = tiles.get(loc);
        if (tile == null || tile.getType() == MapValue.UNKNOWN){
            MapValue value = inRange(loc) ? MapValue.UNKNOWN : MapValue.OUT_OF_RANGE;
            if (tile == null || value == MapValue.OUT_OF_RANGE) {
                tiles.put(loc, new Tile(value, loc));
                tile = tiles.get(loc);
            }
        }
        return tile.getType();

    }

    public Coords findNearestTile(MapValue val){
        List<Coords> matches = findTile(val);
        if(matches.isEmpty()){
            return null;
        }

        Coords closest = matches.getFirst();
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

    public MapValue previousValue(){
        Coords prev = drone.getPosition().step(drone.getHeading().getBackwards());
        return checkCoords(prev);
    }

    public MapValue currentValue(){
        Coords current = drone.getPosition();
        return checkCoords(current);
    }

    public MapValue nextValue(){
        Coords next = drone.getPosition().step(drone.getHeading());
        return checkCoords(next);
    }

    public Coords getBase(){
        return this.base;
    }

    public Integer getBound(Direction direction){
        return bounds.get(direction);
    }
    
    public Tile getTileAt(Coords coords) {
        return tiles.get(coords);
    }

    public void setReportCreek(){
        Tile creek = null;
        double min_distance = Double.POSITIVE_INFINITY;
        if(findNearestTile(MapValue.CREEK) == null){
            return;
        }
        if(findNearestTile(MapValue.EMERGENCY_SITE) == null){
            creek = getTileAt(findNearestTile(MapValue.CREEK));
            generator.setCreekId(creek.getID());
            return;
        }
        Coords site = findNearestTile(MapValue.EMERGENCY_SITE);
        List<Coords> creeks = findTile(MapValue.CREEK);
        for(Coords creekCheck: creeks){
            if(site.distance(creekCheck)<min_distance){
                min_distance = site.distance(creekCheck);
                creek = getTileAt(creekCheck);
            }
        }

        generator.setCreekId(creek.getID());
    }
}