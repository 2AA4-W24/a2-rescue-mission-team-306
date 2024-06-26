package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    public SpiralMap map;
    public CreekReportGenerator generator;

    public Drone drone;

    @BeforeEach
    public void setup(){
        drone = new Drone(500, Direction.EAST);
        generator = new CreekReportGenerator();
        map = new SpiralMap(drone, generator);
    }

    @Test
    public void testBounds(){
        assertEquals(drone.getPosition().x, map.getBound(Direction.WEST));
        assertNull(map.getBound(Direction.EAST));
        String response = "{ \"cost\": 1, \"extras\": { \"range\": 5, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }";
        map.updateStatus(ParsedResult.builder(SpiralRadar.SCAN_EAST).populate(response).build());
        assertEquals(map.getBound(Direction.EAST), 5);
        response = "{ \"cost\": 1, \"extras\": { \"range\": 10, \"found\": \"GROUND\" }, \"status\": \"OK\" }";
        map.updateStatus(ParsedResult.builder(SpiralRadar.SCAN_NORTH).populate(response).build());
        assertNull(map.getBound(Direction.NORTH));
        response = "{ \"cost\": 1, \"extras\": { \"range\": 20, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }";
        map.updateStatus(ParsedResult.builder(SpiralRadar.SCAN_NORTH).populate(response).build());
        assertEquals(map.getBound(Direction.NORTH), 20);
        response = "{ \"cost\": 1, \"extras\": { \"range\": 15, \"found\": \"GROUND\" }, \"status\": \"OK\" }";
        map.updateStatus(ParsedResult.builder(SpiralRadar.SCAN_NORTH).populate(response).build());
        assertEquals(map.getBound(Direction.NORTH), 20);
    }

    @Test
    public void testOcean(){
        testBounds();

        Tile tile = map.getTileAt(new Coords(5, 0));
        assertEquals(tile.getType(), SpiralMapValue.OCEAN);

        tile = map.getTileAt(new Coords(6, 0));
        assertEquals(tile.getType(), SpiralMapValue.OUT_OF_RANGE);

        assertNull(tile.getID());
        assertEquals(tile.getLocation(), new Coords(6, 0));

        assertEquals(map.previousValue(), SpiralMapValue.OUT_OF_RANGE);
        assertEquals(map.currentValue(), SpiralMapValue.UNKNOWN); // Radar does not scan current tile
        assertEquals(map.nextValue(), SpiralMapValue.OCEAN);

        assertEquals(map.getBase(), new Coords(0, 0)); // Base is always (0, 0)


        assertEquals(map.checkCoords(new Coords(0, 16)), SpiralMapValue.GROUND);
        assertEquals(map.checkCoords(new Coords(0, 21)), SpiralMapValue.OUT_OF_RANGE);
        // Radar result of 15 range --> first 15 are ocean (overwrite)
        assertEquals(map.checkCoords(new Coords(0, 11)), SpiralMapValue.OCEAN);
    }

    @Test
    public void testPhoto(){
        // Creek
        assertEquals(map.checkCoords(map.getBase()), SpiralMapValue.UNKNOWN);
        assertNull(map.findNearestTile(SpiralMapValue.CREEK));
        assertNull(generator.getCreekId());
        String response = "{\"cost\": 2, \"extras\": { \"biomes\": [\"OCEAN\"], \"creeks\": [\"quick-beach\"], \"sites\": []}, \"status\": \"OK\"}";
        map.updateStatus(ParsedResult.builder(SpiralPhotoScanner.getDecision()).populate(response).build());
        assertEquals(map.checkCoords(map.getBase()), SpiralMapValue.CREEK);
        assertEquals(map.findNearestTile(SpiralMapValue.CREEK), map.getBase());
        assertNull(generator.getCreekId());

        Coords c;

        // Ordinary scanned ocean
        for (int i = 0; i < 3; i++){
            drone.move(Direction.EAST);
            c = drone.getPosition();
            assertEquals(map.checkCoords(c), SpiralMapValue.UNKNOWN);
            if (i == 0) {
                assertNull(map.findNearestTile(SpiralMapValue.SCANNED_OCEAN));
            }
            else {
                assertEquals(map.findNearestTile(SpiralMapValue.SCANNED_OCEAN), c.step(Direction.WEST));
            }
            response = "{\"cost\": 2, \"extras\": { \"biomes\": [\"OCEAN\"], \"creeks\": [], \"sites\": []}, \"status\": \"OK\"}";
            map.updateStatus(ParsedResult.builder(SpiralPhotoScanner.getDecision()).populate(response).build());
            assertEquals(map.checkCoords(c), SpiralMapValue.SCANNED_OCEAN);
            assertEquals(map.findNearestTile(SpiralMapValue.SCANNED_OCEAN), c);
        }

        // Regular land
        drone.move(Direction.SOUTH);
        assertNull(map.findNearestTile(SpiralMapValue.REGULAR_LAND));
        assertEquals(map.currentValue(), SpiralMapValue.UNKNOWN);
        response = "{\"cost\": 2, \"extras\": { \"biomes\": [\"BEACH\", \"DESERT\"], \"creeks\": [], \"sites\": []}, \"status\": \"OK\"}";
        map.updateStatus(ParsedResult.builder(SpiralPhotoScanner.getDecision()).populate(response).build());
        assertEquals(map.currentValue(), SpiralMapValue.REGULAR_LAND);


        // Emergency site
        drone.move(Direction.EAST);
        drone.move(Direction.NORTH);
        assertNull(map.findNearestTile(SpiralMapValue.EMERGENCY_SITE));
        assertEquals(map.currentValue(), SpiralMapValue.UNKNOWN);
        response = "{\"cost\": 2, \"extras\": { \"biomes\": [\"FOREST\"], \"creeks\": [], \"sites\": [\"jungle\"]}, \"status\": \"OK\"}";
        map.updateStatus(ParsedResult.builder(SpiralPhotoScanner.getDecision()).populate(response).build());
        assertEquals(map.currentValue(), SpiralMapValue.EMERGENCY_SITE);


        // Distant creek
        for (int i = 0; i < 10; i++) {
            drone.move(Direction.SOUTH);
        }

        assertEquals(map.currentValue(), SpiralMapValue.UNKNOWN);
        response = "{\"cost\": 2, \"extras\": { \"biomes\": [\"OCEAN\"], \"creeks\": [\"moon-beach\"], \"sites\": []}, \"status\": \"OK\"}";
        map.updateStatus(ParsedResult.builder(SpiralPhotoScanner.getDecision()).populate(response).build());
        assertEquals(map.currentValue(), SpiralMapValue.CREEK);
        assertEquals(map.getTileAt(drone.getPosition()).getID(), "moon-beach");
        assertEquals(map.findNearestTile(SpiralMapValue.CREEK), drone.getPosition());

        // Updated creek
        map.setReportCreek();
        assertEquals(generator.getCreekId(), "quick-beach");


        // Assert ids are properly source
        assertEquals(map.getTileAt(map.getBase()).getID(), "quick-beach");
        assertEquals(map.getTileAt(drone.getPosition()).getID(), "moon-beach");


    }
}
