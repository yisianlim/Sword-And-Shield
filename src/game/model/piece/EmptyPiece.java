package game.model.piece;

/**
 * This class represents an EmptyPiece in the Board. The purpose of create a class for an empty piece for code reuse
 * for the toString() method in Board.
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
