package tests;

import model.piece.PlayerPiece;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
