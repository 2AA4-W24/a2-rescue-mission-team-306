package a.mcmaster.se2aa4.island.team306;

import org.junit.jupiter.api.Test;
import ca.mcmaster.se2aa4.island.team306.*;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {

    @Test
    public void verify(){
        Coords c = new Coords(0, 0);
        SpiralMapValue value = SpiralMapValue.OUT_OF_RANGE;
        String id = "id";
        Tile tile = new Tile(value, c, id);
        Tile tile2 = new Tile(value, c);

        assertEquals(tile.getType(), value);
        assertEquals(tile.getID(), id);
        assertEquals(tile.getLocation(), c);

        assertNotEquals(tile, tile2);
        assertNull(tile2.getID());
    }
}
