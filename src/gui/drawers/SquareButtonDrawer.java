package gui.drawers;

import model.Position;
import model.piece.Piece;
import model.piece.PlayerPiece;

/**
 * SquareButtonDrawer encapsulates the information needed to create a SquareButton.
 */
public class SquareButtonDrawer {

    /**
     * Piece to draw the SquareButton for.
     */
    private Piece piece;

    /**
     * Position of the SquareButton in the panel.
     */
    private Position position;

    /**
     * Panel type that the SquareButton is for.
     */
    private SquareButton.Panel panelType;

    public SquareButtonDrawer(Piece piece, Position position, SquareButton.Panel panelType){
        this.piece = piece;
        this.position = position;
        this.panelType = panelType;
    }

    public SquareButtonDrawer(PlayerPiece piece, Position position, SquareButton.Panel panelType){
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
