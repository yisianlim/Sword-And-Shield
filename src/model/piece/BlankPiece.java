package model.piece;

/**
 * Represents the empty piece inside the Board that should never have any piece placed.
 * Anytime a PlayerPiece moves into the BlankPiece, it ends up in the cemetery.
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
