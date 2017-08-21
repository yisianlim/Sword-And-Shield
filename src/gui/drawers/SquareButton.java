package gui.drawers;

import model.Position;
import model.piece.*;

import javax.swing.*;
import java.awt.*;

/**
 * SquareButton keeps all the information needed when it is laid out on GameView to the user.
 * Each drawers in the game is drawn as a JButton.
 */
public class SquareButton extends JButton {

    /**
     * Signifies which panel the SquareButton is in which is used to determine where the
     * SquareButton is about to be rendered into.
     *      - CREATION_SHELF: player's creation shelf.
     *      - TRAINING: selected piece laid out in all orientations.
     *      - BOARD: game board.
     *      - CEMETERY: game cemetery.
     */
    public enum Panel {
        CREATION_SHELF, TRAINING, BOARD, CEMETERY,
    }

    /**
     * Position of the SquareButton in its respective panel.
     */
    private Position position;

    /**
     * Piece that is to be customized with its SquareButton.
     */
    private Piece piece;

    /**
     * Panel type in which the SquareButton is about to be rendered into.
     */
    private Panel panelType;

    /**
     * Flag on whether this button should be highlighted or not.
     */
    private boolean highlighted;

    public SquareButton(Piece piece, Position position, Panel panelType){
        this.position = position;
        this.piece = piece;
        this.panelType = panelType;
        this.highlighted = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(highlighted){
            g.setColor(new Color(163,12,232,100));
            g.fillRect(0,0, 60,60);
        }
    }

    public void highlight() {
        this.highlighted = true;
    }

    public Position getPosition(){
        return position;
    }

    public Panel getPanelType(){
        return panelType;
    }

}
