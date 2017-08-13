package model.piece;

/**
 * This class represents an FacePiece in the Board to display the player symbol.
 * 1 for Green & 0 for Yellow.
 * Anytime a PlayerPiece moves into a FacePiece, it ends up in the cemetery.
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
