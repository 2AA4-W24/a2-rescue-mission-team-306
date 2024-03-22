package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpiralMap implements GameMap{
    private final Map<Coords, Tile> tiles;
    private final Coords base;
    private final Drone drone;
    private final Map<Direction, Integer> bounds;

    private final CreekReportGenerator generator;
    
    /**
     * Constructs a new map with the given drone and report generator.
     *
     * @param drone     the drone operating on the map
     * @param generator the report generator for generating reports
     */
    public SpiralMap(Drone drone, CreekReportGenerator generator){
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

    /**
     * Updates the status of the map based on the parsed result.
     * 
     * @param result the parsed result containing information about the update
     */
    public void updateStatus(ParsedResult result){
        Coords pos = drone.getPosition();
        Direction drxn = result.getDirection();
        List<MapValue> values = result.getValues();
        String id = result.getID();
        if (result.getType() == SpiralDecisionType.RADAR){
            for (MapValue value : values) {
                pos = pos.step(drxn);
                addTile(new Tile(value, pos));
            }
        }else if(result.getType() == SpiralDecisionType.PHOTO){
            MapValue value = values.getFirst();
            MapValue prev = checkCoords(pos);
            if(prev == SpiralMapValue.GROUND && value == SpiralMapValue.SCANNED_OCEAN){
                value = SpiralMapValue.REGULAR_LAND;
            }else if(prev == SpiralMapValue.OCEAN && value == SpiralMapValue.REGULAR_LAND){
                value = SpiralMapValue.SCANNED_OCEAN;
            }
            addTile(new Tile(value, pos, id));
        }

        updateBounds(result);
    }


    /**
     * Updates the boundaries of the map based on the parsed result.
     * 
     * @param result the parsed result containing information about the update
     */

    private void updateBounds(ParsedResult result){

        if (result.getType() != SpiralDecisionType.RADAR){
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

    /**
     * Checks the map value at the specified coordinates.
     * 
     * @param loc the coordinates to check
     * @return the map value at the specified coordinates
     */
    public MapValue checkCoords(Coords loc){
        Tile tile = tiles.get(loc);
        if (tile == null || tile.getType() == SpiralMapValue.UNKNOWN){
            MapValue value = inRange(loc) ? SpiralMapValue.UNKNOWN : SpiralMapValue.OUT_OF_RANGE;
            if (tile == null || value == SpiralMapValue.OUT_OF_RANGE) {
                tiles.put(loc, new Tile(value, loc));
                tile = tiles.get(loc);
            }
        }
        return tile.getType();

    }

    /**
     * Finds the nearest tile with the specified value.
     * 
     * @param val the value to search for
     * @return the coordinates of the nearest tile with the specified value
     */
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

    /**
     * Sets the report generator's creek ID based on the nearest creek to the emergency site.
     * If no creek is found, the creek ID is not set.
     */
    public void setReportCreek(){
        Tile creek = null;
        double minDistance = Double.POSITIVE_INFINITY;
        if(findNearestTile(SpiralMapValue.CREEK) == null){
            return;
        }
        if(findNearestTile(SpiralMapValue.EMERGENCY_SITE) == null){
            creek = getTileAt(findNearestTile(SpiralMapValue.CREEK));
            generator.setCreekId(creek.getID());
            return;
        }
        // Find the emergency site
        Coords site = findNearestTile(SpiralMapValue.EMERGENCY_SITE);
        List<Coords> creeks = findTile(SpiralMapValue.CREEK);

        // Find the closest creek to the emergency site
        for(Coords creekCheck: creeks){
            if(site.distance(creekCheck)<minDistance){
                minDistance = site.distance(creekCheck);
                creek = getTileAt(creekCheck);
            }
        }



        generator.setCreekId(creek.getID());
    }
}