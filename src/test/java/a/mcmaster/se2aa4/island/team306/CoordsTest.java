package a.mcmaster.se2aa4.island.team306;
import org.junit.jupiter.api.Test;


import ca.mcmaster.se2aa4.island.team306.*;

import static org.junit.jupiter.api.Assertions.*;

public class CoordsTest {
    private static final double double_margin = 0.0001;
    private final Coords root;
    private final Coords rootcpy;
    private final Coords thirdRoot;
    private final Coords c1;
    private final Coords c1cpy;
    private final Coords thirdC1;
    private final Coords c2;
    
    private final Coords unitNorth, unitEast, unitWest, unitSouth;
    private final Coords c1North, c1East, c1West, c1South;

    public CoordsTest(){
        root = new Coords(0, 0);
        rootcpy = new Coords(0, 0);
        thirdRoot = new Coords(0, 0);
        c1 = new Coords(11, 10);
        c1cpy = new Coords(11, 10);
        thirdC1 = new Coords(11, 10);
        c2 = new Coords(3, 4);
        unitNorth = new Coords(0, 1);
        unitSouth = new Coords(0, -1);
        unitEast = new Coords(1, 0);
        unitWest = new Coords(-1, 0);
        c1East = new Coords(12, 10);
        c1West = new Coords(10, 10);
        c1North = new Coords(11, 11);
        c1South = new Coords(11, 9);
    }

    private boolean doubleEquals(double d1, double d2){
        // Account for floating-point errrors
        return Math.abs(d1 - d2) < double_margin;
    }

    @Test
    public void distanceSymmetryTest(){
        // Arbitrary values, not equivalent
        assertTrue(doubleEquals(c1.distance(c2), c2.distance(c1)));
        // Arbitrary values, equivalent
        assertTrue(doubleEquals(c1.distance(c1cpy), c1cpy.distance(c1)));

        // The root, not equivalent
        assertTrue(doubleEquals(root.distance(c2), c2.distance(root)));
        // The root, equivalent
        assertTrue(doubleEquals(root.distance(rootcpy), rootcpy.distance(root)));

    }

    @Test
    public void zeroDistanceTest(){
        // Self-equivalency
        assertTrue(doubleEquals(root.distance(root), 0));
        // Other zero
        assertTrue(doubleEquals(root.distance(rootcpy), 0));
        // Another self-equivalency
        assertTrue(doubleEquals(c1.distance(c1), 0));
        // Another non-zero
        assertTrue(doubleEquals(c1.distance(c1cpy), 0));
    }

    @Test
    public void standardDistanceTest(){
        // Root to coordinate
        assertTrue(doubleEquals(root.distance(c2), 5));
        // Non-root to coordinate
        assertTrue(doubleEquals(c1.distance(c2), 10));
    }

    @Test
    public void testEquals(){
        // Reflexivity
        assertEquals(root, root);
        assertEquals(c1, c1);
        // Symmetry
        assertEquals(root.equals(rootcpy), rootcpy.equals(root));
        assertEquals(c1.equals(c1cpy), c1cpy.equals(c1));
        assertEquals(c2.equals(c1), c1.equals(c2));
        assertEquals(c2.equals(root), root.equals(c2));
        // Transitivity
        assertEquals(root, rootcpy);
        assertEquals(rootcpy, thirdRoot);
        assertEquals(root, thirdRoot);
        assertEquals(c1, c1cpy);
        assertEquals(c1cpy, thirdC1);
        assertEquals(c1, thirdC1);
        // Consistency omitted because coords are immutable
        // Non-nullity
        assertNotEquals(null, root);
        assertNotEquals(null, c1);
        assertNotEquals(null, c2);
        // Non-equivalent elements (equivalent elements verified above)
        assertNotEquals(root, c1);
        assertNotEquals(c2, root);
        assertNotEquals(c1, c2);
    }

    @Test
    public void testHashCode(){
        // Two equivalent objects have the same hashcode
        assertEquals(c1.hashCode(), c1cpy.hashCode());
        assertEquals(root.hashCode(), rootcpy.hashCode());

        // Two different objects (almost always) have different hashcodes
        assertNotEquals(root.hashCode(), c1.hashCode());
        assertNotEquals(root.hashCode(), c2.hashCode());
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void testOffset(){
        assertEquals(root, root.offset(0, 0));
        assertEquals(c1, c1.offset(0, 0));

        assertEquals(root.offset(c1.x, c1.y), c1);
        assertEquals(c1.offset(c2.x - c1.x, c2.y - c1.y), c2);
        assertEquals(c2.offset(-c2.x, -c2.y), root);
    }
    
    @Test 
    public void testStep(){
        // Test inverses
        assertEquals(root, root.step(Direction.NORTH).step(Direction.SOUTH));
        assertEquals(root, root.step(Direction.EAST).step(Direction.WEST));
        assertEquals(c1, c1.step(Direction.SOUTH).step(Direction.NORTH));
        assertEquals(c1, c1.step(Direction.WEST).step(Direction.EAST));
        
        // Test root's branching
        assertEquals(root.step(Direction.NORTH), unitNorth);
        assertEquals(root.step(Direction.SOUTH), unitSouth);
        assertEquals(root.step(Direction.EAST), unitEast);
        assertEquals(root.step(Direction.WEST), unitWest);
        
        // Test c1's branching
        assertEquals(c1.step(Direction.NORTH), c1North);
        assertEquals(c1.step(Direction.SOUTH), c1South);
        assertEquals(c1.step(Direction.EAST), c1East);
        assertEquals(c1.step(Direction.WEST), c1West);
    }
}