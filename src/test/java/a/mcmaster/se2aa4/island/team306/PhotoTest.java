package a.mcmaster.se2aa4.island.team306;
import ca.mcmaster.se2aa4.island.team306.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhotoTest {

    @Test
    public void testDecision(){
        SpiralDecision decision = SpiralPhotoScanner.getDecision();
        assertEquals(decision.getType(), SpiralDecisionType.PHOTO);
        assertNull(decision.getDirection());
    }

    @Test
    public void testLogic(){
        Drone drone = new Drone(500, Direction.EAST);
        SpiralMap map = new SpiralMap(drone, new CreekReportGenerator());
        DecisionQueue queue = new DecisionQueue();
        SpiralGameTracker tracker = new SpiralGameTracker(drone, map, queue);
        SpiralPhotoScanner photo = new SpiralPhotoScanner(map, tracker);

        assertFalse(photo.scan());

        // End setup state
        String falseRadar = "{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }";
        ParsedResult result = ParsedResult.builder(SpiralRadar.SCAN_EAST).populate(falseRadar).build();
        map.updateStatus(result);
        queue.clear();
        tracker.update();

        // Find island logic
        assertEquals(SpiralGameState.FIND_ISLAND, tracker.getState());
        assertFalse(photo.scan());


        String falsePhoto = "{\"cost\": 2, \"extras\": { \"biomes\": [\"BEACH\"], \"creeks\": [], \"sites\": [\"id\"]}, \"status\": \"OK\"}";
        result = ParsedResult.builder(SpiralPhotoScanner.getDecision()).populate(falsePhoto).build();
        map.updateStatus(result);
        assertFalse(photo.scan());

        // Get to the next state
        tracker.update();

        drone.move(Direction.SOUTH);
        assertTrue(photo.scan());
        falsePhoto = "{\"cost\": 2, \"extras\": { \"biomes\": [\"FOREST\"], \"creeks\": [], \"sites\": [\"id\"]}, \"status\": \"OK\"}";
        result = ParsedResult.builder(SpiralPhotoScanner.getDecision()).populate(falsePhoto).build();
        map.updateStatus(result);
        assertFalse(photo.scan());

        // Get to the next state
        tracker.completeLoop();

        drone.move(Direction.SOUTH);
        assertTrue(photo.scan());
        falsePhoto = "{\"cost\": 2, \"extras\": { \"biomes\": [\"DESERT\"], \"creeks\": [], \"sites\": [\"id\"]}, \"status\": \"OK\"}";
        result = ParsedResult.builder(SpiralPhotoScanner.getDecision()).populate(falsePhoto).build();
        map.updateStatus(result);
        assertFalse(photo.scan());

        // Get to next state
        tracker.completeLoop();

        drone.move(Direction.SOUTH);
        assertTrue(photo.scan());
        falsePhoto = "{\"cost\": 2, \"extras\": { \"biomes\": [\"DESERT\"], \"creeks\": [], \"sites\": [\"id\"]}, \"status\": \"OK\"}";
        result = ParsedResult.builder(SpiralPhotoScanner.getDecision()).populate(falsePhoto).build();
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
