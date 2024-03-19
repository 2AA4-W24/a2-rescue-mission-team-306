package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.ReportGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportGeneratorTest {
    @Test
    public void verify(){
        ReportGenerator generator = new ReportGenerator();
        assertEquals(generator.deliverReport(), "no creek found");
        generator.setCreekId("id");
        assertEquals(generator.deliverReport(), "closest creek id: id");
    }
}
