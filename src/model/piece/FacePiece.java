package model.piece;

/**
 * This class represents an FacePiece in the Board to display the playerName.
 */
public class FacePiece extends Piece {

    private String player;

    public FacePiece(String player){
        this.player = player;
        m_rep = new String[][]{
                {" ", " ", " "},
                {" ", player, " "},
                {" ", " ", " "}
        };
    }

    public boolean greenPlayer(){
        return player.equals("1");
    }

    public boolean yellowPlayer(){
        return player.equals("0");
    }
}
