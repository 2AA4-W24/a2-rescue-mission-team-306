package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoverTest {
    @Test
    public void testDeriveFly(){
        assertEquals(SpiralMover.FLY_NORTH, SpiralMover.deriveFly(Direction.NORTH));
        assertEquals(SpiralMover.FLY_EAST, SpiralMover.deriveFly(Direction.EAST));
        assertEquals(SpiralMover.FLY_SOUTH, SpiralMover.deriveFly(Direction.SOUTH));
        assertEquals(SpiralMover.FLY_WEST, SpiralMover.deriveFly(Direction.WEST));
        boolean nullFlyExists = true;
        try {
            SpiralMover.deriveFly(null);
        } catch(NullPointerException e){
            nullFlyExists = false;
        }
        if (nullFlyExists){
            Assertions.fail("Null fly forward decision exists");
        }
    }
    
    @Test
    public void testDeriveTurn(){
        assertEquals(SpiralMover.TURN_NORTH, SpiralMover.deriveTurn(Direction.NORTH));
        assertEquals(SpiralMover.TURN_EAST, SpiralMover.deriveTurn(Direction.EAST));
        assertEquals(SpiralMover.TURN_SOUTH, SpiralMover.deriveTurn(Direction.SOUTH));
        assertEquals(SpiralMover.TURN_WEST, SpiralMover.deriveTurn(Direction.WEST));
        boolean nullTurnExists = true;
        try {
            SpiralMover.deriveTurn(null);
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
        SpiralMap map = new SpiralMap(drone, new CreekReportGenerator());
        DecisionQueue queue = new DecisionQueue();
        SpiralGameTracker tracker = new SpiralGameTracker(drone, map, queue);
        SpiralMover mover = new SpiralMover(drone, map, queue, tracker);

        // In setup phase, don't move
        assertEquals(SpiralGameState.SETUP, tracker.getState());
        assertFalse(mover.move());

        // "Complete" setup phase
        String falseRadar = "{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }";
        ParsedResult result = ParsedResult.builder(SpiralRadar.SCAN_EAST).populate(falseRadar).build();
        map.updateStatus(result);
        queue.clear();
        tracker.update();

        // In find island phase, move
        assertEquals(SpiralGameState.FIND_ISLAND, tracker.getState());
        assertTrue(mover.move());

        // "Complete" find island phase
        drone.move(Direction.EAST);
        drone.move(Direction.SOUTH);
        int j = 4;
        falseRadar = String.format("{ \"cost\": 1, \"extras\": { \"range\": %d, \"found\": \"GROUND\" }, \"status\": \"OK\" }", j);
        result = ParsedResult.builder(SpiralRadar.SCAN_SOUTH).populate(falseRadar).build();
        map.updateStatus(result);
        for (int i = 0; i < j+1; i++){
            drone.move(Direction.SOUTH);
        }
        assertEquals(map.currentValue(),  SpiralMapValue.GROUND);
        assertTrue(map.getTileAt(drone.getPosition()).getType().isLand());
        tracker.update();

        // In follow coast outside phase, move
        assertEquals(SpiralGameState.FOLLOW_COAST_OUTSIDE, tracker.getState());
        assertTrue(mover.move());

        // Complete follow coast outside phase
        tracker.completeLoop();
        tracker.update();

        // In follow coast inside, move
        assertEquals(SpiralGameState.FOLLOW_COAST_INSIDE, tracker.getState());
        assertTrue(mover.move());

        // Complete follow coast inside phase
        tracker.completeLoop();
        tracker.update();

        // Cannot test search state due to heap space

        // Succeed mission, do not move in success
        tracker.succeedMission();
        assertEquals(SpiralGameState.SUCCESS, tracker.getState());
        assertFalse(mover.move());

        // Fail mission, do not move in failure
        tracker.failMission();
        assertEquals(SpiralGameState.FAILURE, tracker.getState());
        assertFalse(mover.move());

    }
}
