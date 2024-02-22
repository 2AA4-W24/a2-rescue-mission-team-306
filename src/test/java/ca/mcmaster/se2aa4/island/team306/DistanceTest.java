package ca.mcmaster.se2aa4.island.team306;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceTest {
    @Test
    public void testCalculateDistance() {
        // Define coordinates for testing
        Coords c1 = new Coords(0, 0);
        Coords c2 = new Coords(3, 4);

        // Define the expected distance
        float expectedDistance = 5.0f;

        // Calculate the actual distance
        float actualDistance = Distance.calculateDistance(c1, c2);

        // Assert that the calculated distance matches the expected distance
        assertEquals(expectedDistance, actualDistance, 0.0001,
                "The calculated distance should match the expected distance.");
    }
}
