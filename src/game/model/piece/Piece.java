package game.model.piece;

/**
 * This class represents the piece.
 */
public abstract class Piece {

    // The 3x3 String representation of the Piece.
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

}