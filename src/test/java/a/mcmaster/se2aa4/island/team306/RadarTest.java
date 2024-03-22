package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RadarTest {
    @Test
    public void verifyDerivation(){
        assertEquals(SpiralRadar.deriveDecision(Direction.NORTH), SpiralRadar.SCAN_NORTH);
        assertEquals(SpiralRadar.deriveDecision(Direction.SOUTH), SpiralRadar.SCAN_SOUTH);
        assertEquals(SpiralRadar.deriveDecision(Direction.EAST), SpiralRadar.SCAN_EAST);
        assertEquals(SpiralRadar.deriveDecision(Direction.WEST), SpiralRadar.SCAN_WEST);
    }
}
