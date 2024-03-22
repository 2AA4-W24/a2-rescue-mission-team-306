package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class AborterTest {
    private Aborter aborter;
    private Drone drone;

    private GameTracker tracker;

    @BeforeEach
    public void setup(){
        drone = new Drone(500, Direction.EAST);
        Map map = new Map(drone, null);
        tracker = new GameTracker(drone, map, null);
        aborter = new Aborter(drone, map, tracker);
    }

    @Test
    public void testDecision(){
        // Properties
        Decision abortDecision = Aborter.getDecision();
        assertNull(abortDecision.getDirection());
        assertEquals(abortDecision.getType(), DecisionType.ABORT);

        // Consistency
        assertEquals(Aborter.getDecision(), abortDecision);
    }

    @Test
    public void testOngoingLogic(){
        // Do not abort initially
        assertFalse(aborter.abort());
        // Move the drone, but not dangerously out of range
        drone.updateResult(ParsedResult.builder(Mover.FLY_EAST).populate(
                "{ \"cost\": 100, \"extras\": {}, \"status\": \"OK\" }").build());
        // The show goes on; don't abort
        assertFalse(aborter.abort());
        String photoJson = "{\"cost\": 200, \"extras\": { \"biomes\": [\"GLACIER\", \"ALPINE\"], \"creeks\": [], \"sites\": []}, \"status\": \"OK\"}";
        drone.updateResult(ParsedResult.builder(PhotoScanner.getDecision()).populate(photoJson).build());
        // The show goes on; don't abort
        assertFalse(aborter.abort());

        String radarJson = "{ \"cost\": 190, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }";
        drone.updateResult(ParsedResult.builder(Radar.SCAN_EAST).populate(radarJson).build());
        // Too close not to abort; just abort
        assertTrue(aborter.abort());
    }

    @Test
    public void testSuccess(){
        assertFalse(aborter.abort());
        tracker.succeedMission();
        assertTrue(aborter.abort());
    }

    @Test
    public void testFailure(){
        assertFalse(aborter.abort());
        tracker.failMission();
        assertTrue(aborter.abort());
    }
}
