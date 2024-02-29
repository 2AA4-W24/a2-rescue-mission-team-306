package a.mcmaster.se2aa4.island.team306;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.BeforeEach;

public class DroneTest {
    private Drone drone;

    @BeforeEach 
    public void setUp(){
        drone = Explorer.drone;
    }

    @Test
    public void testUpdateStatus(){
        
        ParsedResult result = new ParsedResult(Direction.NORTH, Decision.FLY_FORWARD,
         "{ \"cost\": 3, \"extras\": {}, \"status\": \"OK\" }");
        drone.updateResult(result);
        assertEquals(drone.getEnergy(), 9997);

        result = new ParsedResult(Direction.SOUTH, Decision.ABORT,
         "{ \"cost\": 0, \"extras\": {}, \"status\": \"OK\" }");
        drone.updateResult(result);
        assertEquals(drone.getEnergy(), 9997);
    }

    @Test
    public void testMove(){
        drone.move(Direction.EAST);
        assertEquals(drone.getHeading(), Direction.EAST);
        assertEquals(drone.getPosition().x, 1);
        assertEquals(drone.getPosition().y, 0);
        drone.move(Direction.NORTH);
        assertEquals(drone.getHeading(), Direction.NORTH);
        assertEquals(drone.getPosition().y, 1);
        assertEquals(drone.getPosition().x, 2);
    }
}
