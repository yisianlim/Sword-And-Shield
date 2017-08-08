package tests;

import model.piece.PlayerPiece;
import org.junit.Test;

import static model.piece.PlayerPiece.Item.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceTest {

    /**
     * Test whether the PlayerPiece is rotated correctly.
     */
    @Test
    public void test_PieceRotation() {
        String before = " | \n" +
                        "-X#\n" +
                        "   \n";
        String after =  " # \n" +
                        "-X \n" +
                        " | \n";

        PlayerPiece a = new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, NO_ITEM, SHIELD, "X");
        assertEquals(before, a.toString());
        a.rotate(90);
        assertEquals(after, a.toString());
    }

    /**
     * Test whether the equals(Object o) method was override correctly.
     * Two piece are considered the same piece as long as they have the same letter.
     */
    @Test
    public void testEqualsOverride(){
        PlayerPiece a = new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, NO_ITEM, SHIELD, "X");
        PlayerPiece b = new PlayerPiece(HORIZONTAL_SWORD, NO_ITEM, SHIELD, VERTICAL_SWORD, "X");
        PlayerPiece c = new PlayerPiece(HORIZONTAL_SWORD, NO_ITEM, SHIELD, VERTICAL_SWORD, "X");

        // Reflexive property.
        assertTrue(a.equals(a));

        // Symmetric property.
        assertTrue( a.equals(b) == b.equals(a));

        // Transitive property
        if (a.equals(b) && b.equals(c)) {
            assertTrue( a.equals(c) );
        }

        // Consistency property.
        assertTrue( a.equals(b) == a.equals(b));

        // Non-null property.
        assertFalse( a.equals(null));
    }

    /**
     * Test whether the hashCode() was override correctly.
     */
    @Test
    public void testHashCodeOverride(){
        PlayerPiece a = new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, NO_ITEM, SHIELD, "X");
        PlayerPiece b = new PlayerPiece(HORIZONTAL_SWORD, NO_ITEM, SHIELD, VERTICAL_SWORD, "X");
        assertTrue(a.hashCode() == b.hashCode());
    }

}
