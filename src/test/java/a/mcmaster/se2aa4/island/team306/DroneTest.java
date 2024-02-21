package a.mcmaster.se2aa4.island.team306;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.BeforeEach;

public class DroneTest {
    private Drone drone;

    @BeforeEach 
    public void setUp(){
        Coords start = new Coords(0, 0);
        drone = new Drone(0, 100, start, Direction.EAST);
    }
    @Test
    public void testUpdateStatus(){
        JSONObject obj = new JSONObject("{ \"cost\": 3, \"extras\": {}, \"status\": \"OK\" }");
        RawResults results = new RawResults(obj);
        drone.updateRawResults(results);
        drone.updateEnergy();
        assertEquals(drone.getEnergy(), 97);

        // Usually unsafe (not here)
        obj.put("cost", 0);

        drone.updateEnergy();
        assertEquals(drone.getEnergy(), 97);
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
