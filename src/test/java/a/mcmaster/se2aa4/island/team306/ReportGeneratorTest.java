package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.CreekReportGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportGeneratorTest {
    @Test
    public void verify(){
        CreekReportGenerator generator = new CreekReportGenerator();
        assertEquals(generator.deliverReport(), "no creek found");
        generator.setCreekId("id");
        assertEquals(generator.deliverReport(), "closest creek id: id");
    }
}
