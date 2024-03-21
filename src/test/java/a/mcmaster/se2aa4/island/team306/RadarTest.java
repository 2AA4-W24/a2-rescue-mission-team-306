package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RadarTest {
    @Test
    public void verifyDerivation(){
        assertEquals(Radar.deriveDecision(Direction.NORTH), Radar.SCAN_NORTH);
        assertEquals(Radar.deriveDecision(Direction.SOUTH), Radar.SCAN_SOUTH);
        assertEquals(Radar.deriveDecision(Direction.EAST), Radar.SCAN_EAST);
        assertEquals(Radar.deriveDecision(Direction.WEST), Radar.SCAN_WEST);
    }
}
