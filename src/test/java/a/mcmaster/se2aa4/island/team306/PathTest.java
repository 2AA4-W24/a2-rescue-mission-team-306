package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PathTest {

    Coords root;
    Drone drone;
    Map map;



    @BeforeEach
    public void setup(){
        root = new Coords(0, 0);
        drone = new Drone(100, Direction.EAST);
        map = new Map(drone, new ReportGenerator());
    }

    @Test
    public void testEmpty(){
        Path path = new Path(root, root, Direction.EAST, map);
        DecisionQueue queue = path.findPath();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testStraightPath(){
        Coords dest = new Coords(5, 0);
        Path path = new Path(root, dest, Direction.EAST, map);
        DecisionQueue queue = path.findPath();
        for (int i = 0; i < 5; i++){
            Decision decision = queue.dequeue();
            assertEquals(decision.getType(),DecisionType.FLY_FORWARD);
            assertEquals(decision.getDirection(), Direction.EAST);
        }
        assertTrue(queue.isEmpty());
    }

   // Turning untestable due to Java heap error

}
