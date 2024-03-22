package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParsedResultTest {

    @Test
    public void testFly(){
        ParsedResultBuilder builder = ParsedResult.builder(Mover.FLY_EAST);
        ParsedResult result;
        // Ensure empty result does not build
        boolean nullBuildable = true;
        try {
            builder.build();
        }
        catch (AssertionError e){
            // What should happen
            nullBuildable = false;
        }
        if (nullBuildable){
            Assertions.fail("Successfully built unpopulated result");
            return;
        }
        String response = "{ \"cost\": 2, \"extras\": {}, \"status\": \"OK\" }";
        builder.populate(response);
        try {
            result = builder.build();
        }
        catch(AssertionError e){
            Assertions.fail("Failed to build populated result");
            return;
        }
        assertEquals(result.getType(), DecisionType.FLY_FORWARD);
        assertEquals(result.getDirection(), Direction.EAST);
        assertEquals(result.getCost(), 2);
        assertNull(result.getID());
    }

    @Test
    public void testTurn(){
        ParsedResultBuilder builder = ParsedResult.builder(Mover.TURN_SOUTH);
        String response = "{ \"cost\": 4, \"extras\": {}, \"status\": \"OK\" }";
        ParsedResult result = builder.populate(response).build(); // Funky behaviour tested in testFly
        assertEquals(result.getType(), DecisionType.TURN);
        assertEquals(result.getDirection(), Direction.SOUTH);
        assertEquals(result.getCost(), 4);
        assertNull(result.getID());
    }


    @Test
    public void testRadar(){
        ParsedResultBuilder builder = ParsedResult.builder(Radar.SCAN_NORTH);

        // Found land
        String response = "{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }";
        ParsedResult result = builder.populate(response).build();

        // General, not double-testing
        assertEquals(result.getDirection(), Direction.NORTH);
        assertEquals(result.getType(), DecisionType.RADAR);
        assertEquals(result.getCost(), 1);
        assertNull(result.getID());
        assertEquals(result.getRange(), 2);

        // For found land
        List<MapValue> values = result.getValues();
        for (int i = 0; i + 1 < values.size(); i++){
            MapValue value = values.get(i);
            assertEquals(value, MapValue.OCEAN);
        }
        assertEquals(values.getLast(), MapValue.GROUND);

        // Hit out of range
        response = "{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }";
        result = builder.populate(response).build();
        values = result.getValues();
        for (int i = 0; i + 1 < values.size(); i++){
            MapValue value = values.get(i);
            assertEquals(value, MapValue.OCEAN);
        }
        assertEquals(values.getLast(), MapValue.OUT_OF_RANGE);
    }

    @Test
    public void testPhoto(){
        ParsedResultBuilder builder = ParsedResult.builder(PhotoScanner.getDecision());
        String response = "{\"cost\": 2, \"extras\": { \"biomes\": [\"GLACIER\", \"ALPINE\"], \"creeks\": [], \"sites\": []}, \"status\": \"OK\"}";
        ParsedResult result = builder.populate(response).build();
        assertEquals(result.getType(), DecisionType.PHOTO);
        assertNull(result.getDirection());
        assertEquals(result.getCost(), 2);
        assertNull(result.getID());

        response = "{\"cost\": 2, \"extras\": { \"biomes\": [\"BEACH\"], \"creeks\": [\"id\"], \"sites\": []}, \"status\": \"OK\"}";
        result = builder.populate(response).build();
        assertEquals(result.getID(), "id");

        response = "{\"cost\": 2, \"extras\": { \"biomes\": [\"BEACH\"], \"creeks\": [], \"sites\": [\"id\"]}, \"status\": \"OK\"}";
        result = builder.populate(response).build();
        assertEquals(result.getID(), "id");


    }

    @Test
    public void testAbort(){
        ParsedResultBuilder builder = ParsedResult.builder(Aborter.getDecision());
        String response = "{ \"cost\": 3, \"extras\": {}, \"status\": \"OK\" }";
        ParsedResult result = builder.populate(response).build();

        assertEquals(result.getType(), DecisionType.ABORT);
        assertNull(result.getDirection());
        assertEquals(result.getCost(), 3);
        assertNull(result.getID());
    }
}


