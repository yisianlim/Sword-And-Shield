package model.piece;

/**
 * This class represents an EmptyPiece in the Board. It remains empty until a PlayerPiece moves into it.
 */
public class EmptyPiece extends Piece {

    /**
     * Represents the empty piece inside the Board that determines the game.
     */
    public EmptyPiece(){
        super();
        m_rep = new String[][]{
                {" ", " ", " "},
                {" ", " ", " "},
                {" ", " ", " "}
        };
    }
}
