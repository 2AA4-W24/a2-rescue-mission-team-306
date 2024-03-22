package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoverTest {
    @Test
    public void testDeriveFly(){
        assertEquals(Mover.FLY_NORTH, Mover.deriveFly(Direction.NORTH));
        assertEquals(Mover.FLY_EAST, Mover.deriveFly(Direction.EAST));
        assertEquals(Mover.FLY_SOUTH, Mover.deriveFly(Direction.SOUTH));
        assertEquals(Mover.FLY_WEST, Mover.deriveFly(Direction.WEST));
        boolean nullFlyExists = true;
        try {
            Mover.deriveFly(null);
        } catch(NullPointerException e){
            nullFlyExists = false;
        }
        if (nullFlyExists){
            Assertions.fail("Null fly forward decision exists");
        }
    }
    
    @Test
    public void testDeriveTurn(){
        assertEquals(Mover.TURN_NORTH, Mover.deriveTurn(Direction.NORTH));
        assertEquals(Mover.TURN_EAST, Mover.deriveTurn(Direction.EAST));
        assertEquals(Mover.TURN_SOUTH, Mover.deriveTurn(Direction.SOUTH));
        assertEquals(Mover.TURN_WEST, Mover.deriveTurn(Direction.WEST));
        boolean nullTurnExists = true;
        try {
            Mover.deriveTurn(null);
        } catch(NullPointerException e){
            nullTurnExists = false;
        }
        if (nullTurnExists){
            Assertions.fail("Null fly forward decision exists");
        }
    }

    @Test
    public void testMoveLogic(){
        Drone drone = new Drone(500, Direction.EAST);
        Map map = new Map(drone, new ReportGenerator());
        DecisionQueue queue = new DecisionQueue();
        GameTracker tracker = new GameTracker(drone, map, queue);
        Mover mover = new Mover(drone, map, queue, tracker);

        // In setup phase, don't move
        assertEquals(GameState.SETUP, tracker.getState());
        assertFalse(mover.move());

        // "Complete" setup phase
        String falseRadar = "{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }";
        ParsedResult result = ParsedResult.builder(Radar.SCAN_EAST).populate(falseRadar).build();
        map.updateStatus(result);
        queue.clear();
        tracker.update();

        // In find island phase, move
        assertEquals(GameState.FIND_ISLAND, tracker.getState());
        assertTrue(mover.move());

        // "Complete" find island phase
        drone.move(Direction.EAST);
        drone.move(Direction.SOUTH);
        int j = 4;
        falseRadar = String.format("{ \"cost\": 1, \"extras\": { \"range\": %d, \"found\": \"GROUND\" }, \"status\": \"OK\" }", j);
        result = ParsedResult.builder(Radar.SCAN_SOUTH).populate(falseRadar).build();
        map.updateStatus(result);
        for (int i = 0; i < j+1; i++){
            drone.move(Direction.SOUTH);
        }
        assertEquals(map.currentValue(),  MapValue.GROUND);
        assertTrue(map.getTileAt(drone.getPosition()).getType().isLand());
        tracker.update();

        // In follow coast outside phase, move
        assertEquals(GameState.FOLLOW_COAST_OUTSIDE, tracker.getState());
        assertTrue(mover.move());

        // Complete follow coast outside phase
        tracker.completeLoop();
        tracker.update();

        // In follow coast inside, move
        assertEquals(GameState.FOLLOW_COAST_INSIDE, tracker.getState());
        assertTrue(mover.move());

        // Complete follow coast inside phase
        tracker.completeLoop();
        tracker.update();

        // Cannot test search state due to heap space

        // Succeed mission, do not move in success
        tracker.succeedMission();
        assertEquals(GameState.SUCCESS, tracker.getState());
        assertFalse(mover.move());

        // Fail mission, do not move in failure
        tracker.failMission();
        assertEquals(GameState.FAILURE, tracker.getState());
        assertFalse(mover.move());

    }
}
