package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {
    @Test
    public void verifyLogic(){
        Drone drone = new Drone(500, Direction.EAST);
        SpiralMap map = new SpiralMap(drone, new CreekReportGenerator());
        DecisionQueue queue = new DecisionQueue();
        SpiralGameTracker tracker = new SpiralGameTracker(drone, map, queue);

        assertEquals(SpiralGameState.SETUP, tracker.getState());

        // Artificially "end" setup
        // "Complete" setup phase
        String falseRadar = "{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }";
        ParsedResult result = ParsedResult.builder(SpiralRadar.SCAN_EAST).populate(falseRadar).build();
        map.updateStatus(result);
        queue.clear();
        // Before update, stay in current state
        assertEquals(SpiralGameState.SETUP, tracker.getState());
        tracker.update();

        // Assert in find island
        assertEquals(SpiralGameState.FIND_ISLAND, tracker.getState());

        // Artificially end find island
        drone.move(Direction.EAST);
        drone.move(Direction.SOUTH);
        int j = 2;
        falseRadar = String.format("{ \"cost\": 1, \"extras\": { \"range\": %d, \"found\": \"GROUND\" }, \"status\": \"OK\" }", j);
        result = ParsedResult.builder(SpiralRadar.SCAN_SOUTH).populate(falseRadar).build();
        map.updateStatus(result);
        for (int i = 0; i < j+1; i++){
            drone.move(Direction.SOUTH);
        }
        assertEquals(map.currentValue(),  SpiralMapValue.GROUND);
        assertTrue(map.getTileAt(drone.getPosition()).getType().isLand());
        // Before update, stay in current state
        assertEquals(SpiralGameState.FIND_ISLAND, tracker.getState());
        tracker.update();

        // Assert in follow coast outside
        assertEquals(SpiralGameState.FOLLOW_COAST_OUTSIDE, tracker.getState());

        // End find follow coast outside
        tracker.completeLoop();
        // Before update, stay in current state
        assertEquals(SpiralGameState.FOLLOW_COAST_OUTSIDE, tracker.getState());
        tracker.update();

        // Assert in follow coast inside
        assertEquals(SpiralGameState.FOLLOW_COAST_INSIDE, tracker.getState());

        // End find coast inside
        tracker.completeLoop();
        // Before update, stay in current state
        assertEquals(SpiralGameState.FOLLOW_COAST_INSIDE, tracker.getState());
        tracker.update();


        // Assert in search phase
        assertEquals(SpiralGameState.SEARCH, tracker.getState());

        // End search phase
        tracker.succeedMission();
        assertEquals(SpiralGameState.SUCCESS, tracker.getState());

        // Fail mission
        tracker.failMission();
        assertEquals(SpiralGameState.FAILURE, tracker.getState());

        // Back to success
        tracker.succeedMission();
        assertEquals(SpiralGameState.SUCCESS, tracker.getState());

    }
}
