package a.mcmaster.se2aa4.island.team306;
import ca.mcmaster.se2aa4.island.team306.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhotoTest {

    @Test
    public void testDecision(){
        Decision decision = PhotoScanner.getDecision();
        assertEquals(decision.getType(), DecisionType.PHOTO);
        assertNull(decision.getDirection());
    }

}
