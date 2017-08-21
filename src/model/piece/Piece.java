package model.piece;

import gui.drawers.SquareButton;
import model.Position;

/**
 * This class represents all pieces. All the various types of piece in the game extends this superclass.
 */
public abstract class Piece {


    /**
     * The 3x3 String representation of the Piece
     */
    protected String[][] m_rep;

    public String topLine(){
        return m_rep[0][0] + m_rep[0][1] + m_rep[0][2];
    }

    public String midLine(){
        return m_rep[1][0] + m_rep[1][1] + m_rep[1][2];
    }

    public String bottomLine(){
        return m_rep[2][0] + m_rep[2][1] + m_rep[2][2];
    }

    public String toString(){
        return  topLine() + "\n" + midLine() + "\n" + bottomLine() + "\n";
    }

    /**
     * Returns the clone for the Piece. All subclasses except PlayerPiece returns this as they are immutable.
     * @return
     *      cloned Piece.
     */
    public Piece clone(){
        return this;
    }

    /**
     * Generate the SquareButton needed for the view based on the type.
     * @param position
     *          Position of the Piece on the board.
     * @param squareType
     *          Panel of the Piece in the view. (Cemetery, board or creation shelf)
     * @return
     *          Appropriate SquareButton based on type.
     */
    public abstract SquareButton createButton(Position position, SquareButton.Panel squareType);
}