package a.mcmaster.se2aa4.island.team306;
import ca.mcmaster.se2aa4.island.team306.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhotoTest {

    @Test
    public void testDecision(){
        Decision decision = PhotoScanner.getDecision();
        assertEquals(decision.getType(), DecisionType.PHOTO);
        assertNull(decision.getDirection());
    }

    @Test
    public void testLogic(){
        Drone drone = new Drone(500, Direction.EAST);
        Map map = new Map(drone, new ReportGenerator());
        DecisionQueue queue = new DecisionQueue();
        GameTracker tracker = new GameTracker(drone, map, queue);
        PhotoScanner photo = new PhotoScanner(map, tracker);

        assertFalse(photo.scan());

        // End setup state
        String falseRadar = "{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }";
        ParsedResult result = ParsedResult.builder(Radar.SCAN_EAST).populate(falseRadar).build();
        map.updateStatus(result);
        queue.clear();
        tracker.update();

        // Find island logic
        assertEquals(GameState.FIND_ISLAND, tracker.getState());
        assertFalse(photo.scan());


        String falsePhoto = "{\"cost\": 2, \"extras\": { \"biomes\": [\"BEACH\"], \"creeks\": [], \"sites\": [\"id\"]}, \"status\": \"OK\"}";
        result = ParsedResult.builder(PhotoScanner.getDecision()).populate(falsePhoto).build();
        map.updateStatus(result);
        assertFalse(photo.scan());

        // Get to the next state
        tracker.update();

        drone.move(Direction.SOUTH);
        assertTrue(photo.scan());
        falsePhoto = "{\"cost\": 2, \"extras\": { \"biomes\": [\"FOREST\"], \"creeks\": [], \"sites\": [\"id\"]}, \"status\": \"OK\"}";
        result = ParsedResult.builder(PhotoScanner.getDecision()).populate(falsePhoto).build();
        map.updateStatus(result);
        assertFalse(photo.scan());

        // Get to the next state
        tracker.completeLoop();

        drone.move(Direction.SOUTH);
        assertTrue(photo.scan());
        falsePhoto = "{\"cost\": 2, \"extras\": { \"biomes\": [\"DESERT\"], \"creeks\": [], \"sites\": [\"id\"]}, \"status\": \"OK\"}";
        result = ParsedResult.builder(PhotoScanner.getDecision()).populate(falsePhoto).build();
        map.updateStatus(result);
        assertFalse(photo.scan());

        // Get to next state
        tracker.completeLoop();

        drone.move(Direction.SOUTH);
        assertTrue(photo.scan());
        falsePhoto = "{\"cost\": 2, \"extras\": { \"biomes\": [\"DESERT\"], \"creeks\": [], \"sites\": [\"id\"]}, \"status\": \"OK\"}";
        result = ParsedResult.builder(PhotoScanner.getDecision()).populate(falsePhoto).build();
        map.updateStatus(result);
        assertFalse(photo.scan());

        // Get to next state
        tracker.succeedMission();
        drone.move(Direction.EAST);
        assertFalse(photo.scan());

        // Fail, no scan
        tracker.failMission();
        drone.move(Direction.NORTH);
        assertFalse(photo.scan());
    }

}
