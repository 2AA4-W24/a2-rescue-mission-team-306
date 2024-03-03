package a.mcmaster.se2aa4.island.team306;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ca.mcmaster.se2aa4.island.team306.*;

public class CoordsTest {
    private static final double double_margin = 0.0001;
    private final Coords root;
    private final Coords rootcpy;
    private final Coords thirdRoot;
    private final Coords c1;
    private final Coords c1cpy;
    private final Coords thirdC1;
    private final Coords c2;

    public CoordsTest(){
        root = new Coords(0, 0);
        rootcpy = new Coords(0, 0);
        thirdRoot = new Coords(0, 0);
        c1 = new Coords(11, 10);
        c1cpy = new Coords(11, 10);
        thirdC1 = new Coords(11, 10);
        c2 = new Coords(3, 4);
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
        // Consistency ommitted because coords are immutable
        // Non-nullity
        assertFalse(root.equals(null));
        assertFalse(c1.equals(null));
        assertFalse(c2.equals(null));
        // Non-equivalent elements (equivalent elements verified above)
        assertFalse(root.equals(c1));
        assertFalse(c2.equals(root));
        assertFalse(c1.equals(c2));
    }

    @Test
    public void testHashCode(){
        // Two equivalent objects have the same hashcode
        assertEquals(c1.hashCode(), c1cpy.hashCode());
        assertEquals(root.hashCode(), rootcpy.hashCode());

        // Two different objects (almost always) have different hashcodes
        assertFalse(root.hashCode() == c1.hashCode());
        assertFalse(root.hashCode() == c2.hashCode());
        assertFalse(c1.hashCode() == c2.hashCode());
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
        Coords c = new Coords(1, 0);
        assertEquals(root.step(Direction.EAST), c);

        c = new Coords(3, 3);
        assertEquals(c2.step(Direction.SOUTH), c);
    }
}