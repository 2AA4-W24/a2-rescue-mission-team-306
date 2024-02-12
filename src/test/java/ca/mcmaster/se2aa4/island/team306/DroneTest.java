package ca.mcmaster.se2aa4.island.team306;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        drone.updateEnergy("{ \"cost\": 3, \"extras\": {}, \"status\": \"OK\" }");
        assertEquals(drone.energy, 96);
        drone.updateEnergy("{ \"cost\": 0, \"extras\": {}, \"status\": \"OK\" }");
        assertEquals(drone.energy, 96);
    }

    @Test
    public void testMove(){
        drone.move(Direction.EAST);
        assertEquals(drone.heading, Direction.EAST);
        assertEquals(drone.position.x, 1);
        assertEquals(drone.position.y, 0);
        drone.move(Direction.NORTH);
        assertEquals(drone.heading, Direction.NORTH);
        assertEquals(drone.position.y, 1);
        assertEquals(drone.position.x, 2);
    }
}
