package gui.square;

import model.Position;
import model.piece.*;

import javax.swing.*;

/**
 * Each square in the game is drawn as a JButton.
 */
public class SquareButton extends JButton {

    public enum Panel {
        CREATION_SHELF_GREEN, CREATION_SHELF_YELLOW, TRAINING, BOARD, CEMETERY,
    };

    private Position position;
    private Piece piece;
    private Panel panelType;

    public SquareButton(Piece piece, Position position, Panel panelType){
        this.position = position;
        this.piece = piece;
        this.panelType = panelType;
    }

    public Position getPosition(){
        return position;
    }

    public Panel getPanelType(){
        return panelType;
    }

}
