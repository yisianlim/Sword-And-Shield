package gui.drawers;

import model.Position;
import model.piece.Piece;
import model.piece.PlayerPiece;

/**
 * ButtonDrawer encapsulates the information needed to create a SquareButton.
 */
public class ButtonDrawer {

    private Piece piece;
    private Position position;
    private SquareButton.Panel panelType;

    public ButtonDrawer(Piece piece, Position position, SquareButton.Panel panelType){
        this.piece = piece;
        this.position = position;
        this.panelType = panelType;
    }

    public ButtonDrawer(PlayerPiece piece, Position position, SquareButton.Panel panelType){
        this.piece = piece;
        this.position = position;
        this.panelType = panelType;
    }

    /**
     * Return the custom SquareButton based on the piece which is resolved at runtime from
     * dynamic dispatch.
     * @return
     *      Customized SquareButton.
     */
    public SquareButton makeButton(){
        return piece.createButton(position, panelType);
    }

}
