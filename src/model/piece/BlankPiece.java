package model.piece;

/**
 * Represents the empty piece inside the Board that should never have any piece placed.
 * Anytime another Piece wishes to replace BlankPiece.
 */
public class BlankPiece extends Piece {
    public BlankPiece(){
        super();
        m_rep = new String[][]{
                {"X", "X", "X"},
                {"X", "X", "X"},
                {"X", "X", "X"}
        };
    }
}
