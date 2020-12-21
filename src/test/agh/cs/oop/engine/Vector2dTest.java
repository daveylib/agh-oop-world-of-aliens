package agh.cs.oop.engine;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class Vector2dTest {
    @Test
    void toStringMethod() {
        Vector2d vectorA = new Vector2d(2, 2);
        assertEquals(vectorA.toString(), "(2,2)");

        Vector2d vectorB = new Vector2d(-2, 2);
        assertEquals(vectorB.toString(), "(-2,2)");

        Vector2d vectorC = new Vector2d(2, -2);
        assertEquals(vectorC.toString(), "(2,-2)");

        Vector2d vectorD = new Vector2d(-2, -2);
        assertEquals(vectorD.toString(), "(-2,-2)");

        Vector2d vectorE = new Vector2d(0, -0);
        assertEquals(vectorE.toString(), "(0,0)");
    }

    @Test
    void precedesMethod() {
        Vector2d vectorMain = new Vector2d(5, 5);

        Vector2d vectorTestA = new Vector2d(3, 3);
        assertTrue(vectorTestA.precedes(vectorMain));

        Vector2d vectorTestB = new Vector2d(5, 3);
        assertTrue(vectorTestB.precedes(vectorMain));

        Vector2d vectorTestC = new Vector2d(3, 5);
        assertTrue(vectorTestC.precedes(vectorMain));

        Vector2d vectorTestD = new Vector2d(7, 7);
        assertFalse(vectorTestD.precedes(vectorMain));

        Vector2d vectorTestE = new Vector2d(7, 3);
        assertFalse(vectorTestE.precedes(vectorMain));

        Vector2d vectorTestF = new Vector2d(3, 7);
        assertFalse(vectorTestF.precedes(vectorMain));
    }

    @Test
    void followsMethod() {
        Vector2d vectorMain = new Vector2d(5, 5);

        Vector2d vectorTestA = new Vector2d(7, 7);
        assertTrue(vectorTestA.follows(vectorMain));

        Vector2d vectorTestB = new Vector2d(5, 7);
        assertTrue(vectorTestB.follows(vectorMain));

        Vector2d vectorTestC = new Vector2d(7, 5);
        assertTrue(vectorTestC.follows(vectorMain));

        Vector2d vectorTestD = new Vector2d(3, 3);
        assertFalse(vectorTestD.follows(vectorMain));

        Vector2d vectorTestE = new Vector2d(5, 3);
        assertFalse(vectorTestE.follows(vectorMain));

        Vector2d vectorTestF = new Vector2d(3, 5);
        assertFalse(vectorTestF.follows(vectorMain));
    }

    @Test
    void addMethod() {
        Vector2d vectorA = new Vector2d(2, 5);

        Vector2d vectorATestA = new Vector2d(3, 3);
        assertEquals(vectorA.add(vectorATestA).toString(), "(5,8)");

        Vector2d vectorATestB = new Vector2d(-3, 3);
        assertEquals(vectorA.add(vectorATestB).toString(), "(-1,8)");

        Vector2d vectorB = new Vector2d(-2, -5);

        Vector2d vectorBTestA = new Vector2d(3, 3);
        assertEquals(vectorB.add(vectorBTestA).toString(), "(1,-2)");

        Vector2d vectorBTestB = new Vector2d(-3, 3);
        assertEquals(vectorB.add(vectorBTestB).toString(), "(-5,-2)");
    }

    @Test
    void compareToMethod() {
        Vector2d vectorMain = new Vector2d(2, 2);

        Vector2d vectorTestA = new Vector2d(2, 2);
        assertEquals(vectorMain.compareTo(vectorTestA), 0);

        Vector2d vectorTestB = new Vector2d(2, 3);
        assertTrue(vectorMain.compareTo(vectorTestB) < 0);

        Vector2d vectorTestC = new Vector2d(3, 2);
        assertTrue(vectorMain.compareTo(vectorTestC) < 0);

        Vector2d vectorTestD = new Vector2d(3, 3);
        assertTrue(vectorMain.compareTo(vectorTestD) < 0);

        Vector2d vectorTestE = new Vector2d(2, -2);
        assertTrue(vectorMain.compareTo(vectorTestE) > 0);

        Vector2d vectorTestF = new Vector2d(-2, -2);
        assertTrue(vectorMain.compareTo(vectorTestF) > 0);
    }
}
