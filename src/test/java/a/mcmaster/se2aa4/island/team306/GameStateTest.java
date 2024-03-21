package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {
    @Test
    public void verifyLogic(){
        Drone drone = new Drone(500, Direction.EAST);
        Map map = new Map(drone, new ReportGenerator());
        DecisionQueue queue = new DecisionQueue();
        GameTracker tracker = new GameTracker(drone, map, queue);

        assertEquals(GameState.SETUP, tracker.getState());

        // Artificially "end" setup
        // "Complete" setup phase
        String falseRadar = "{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }";
        ParsedResult result = ParsedResult.builder(Radar.SCAN_EAST).populate(falseRadar).build();
        map.updateStatus(result);
        queue.clear();
        // Before update, stay in current state
        assertEquals(GameState.SETUP, tracker.getState());
        tracker.update();

        // Assert in find island
        assertEquals(GameState.FIND_ISLAND, tracker.getState());

        // Artificially end find island
        drone.move(Direction.EAST);
        drone.move(Direction.SOUTH);
        int j = 2;
        falseRadar = String.format("{ \"cost\": 1, \"extras\": { \"range\": %d, \"found\": \"GROUND\" }, \"status\": \"OK\" }", j);
        result = ParsedResult.builder(Radar.SCAN_SOUTH).populate(falseRadar).build();
        map.updateStatus(result);
        for (int i = 0; i < j+1; i++){
            drone.move(Direction.SOUTH);
        }
        assertEquals(map.currentValue(),  MapValue.GROUND);
        assertTrue(map.getTileAt(drone.getPosition()).getType().isLand());
        // Before update, stay in current state
        assertEquals(GameState.FIND_ISLAND, tracker.getState());
        tracker.update();

        // Assert in follow coast outside
        assertEquals(GameState.FOLLOW_COAST_OUTSIDE, tracker.getState());

        // End find follow coast outside
        tracker.completeLoop();
        // Before update, stay in current state
        assertEquals(GameState.FOLLOW_COAST_OUTSIDE, tracker.getState());
        tracker.update();

        // Assert in follow coast inside
        assertEquals(GameState.FOLLOW_COAST_INSIDE, tracker.getState());

        // End find coast inside
        tracker.completeLoop();
        // Before update, stay in current state
        assertEquals(GameState.FOLLOW_COAST_INSIDE, tracker.getState());
        tracker.update();


        // Assert in search phase
        assertEquals(GameState.SEARCH, tracker.getState());

        // End search phase
        tracker.succeedMission();
        assertEquals(GameState.SUCCESS, tracker.getState());

        // Fail mission
        tracker.failMission();
        assertEquals(GameState.FAILURE, tracker.getState());

        // Back to success
        tracker.succeedMission();
        assertEquals(GameState.SUCCESS, tracker.getState());

    }
}
