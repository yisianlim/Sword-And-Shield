package gui.drawers;

import gui.views.PrimaryView;
import model.Position;
import model.piece.*;

import javax.swing.*;
import java.awt.*;

/**
 * SquareButton keeps all the information needed when it is laid out on GameView to the user.
 * Each Piece in the game is drawn as a SquareButton.
 */
public class SquareButton extends JButton {

    /**
     * Signifies which panel the SquareButton is in which is used to determine where the
     * SquareButton is about to be rendered into.
     *      - CREATION_SHELF: player's creation shelf, which displays all the PlayerPiece the user currently have.
     *      - TRAINING: selected piece laid out in all orientations.
     *      - BOARD_DISPLAY: SquareButton is to be simply displayed.
     *      - BOARD_SELECTION: SquareButton is selected by the user to be moved.
     *      - CEMETERY: game cemetery.
     */
    public enum Panel {
        CREATION_SHELF, TRAINING, BOARD_DISPLAY, BOARD_SELECTED, CEMETERY,
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
     * Flag on whether this button should be selected or not.
     */
    private boolean selected;

    /**
     * Flag on whether this button have been moved or not.
     */
    private boolean moved;

    public SquareButton(Piece piece, Position position, Panel panelType){
        this.position = position;
        this.piece = piece;
        this.panelType = panelType;
        this.selected = false;
        this.moved = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = PrimaryView.getPreferredIconSize().width;
        int height = PrimaryView.getPreferredIconSize().height;

        // If the Piece is selected, then we draw highlight it with a purple alpha layer.
        if(selected){
            g.setColor(new Color(163,12,232,100));
            g.fillRect(0,0, width, height);
        }

        // If the Piece has been moved, then we draw highlight it with a red alpha layer.
        if(moved){
            g.setColor(new Color(255,0,0,100));
            g.fillRect(0,0, width, height);
        }
    }

    /**
     * Check if it is Yellow PlayerPiece.
     * @return
     *      true if the Piece binded to this SquareButton is Yellow's PlayerPiece.
     */
    public boolean isYellow(){
        if(piece instanceof PlayerPiece){
            return ((PlayerPiece) piece).yellowPlayer();
        }
        return false;
    }

    /**
     * Check if it is Green PlayerPiece.
     * @return
     *      true if the Piece binded to this SquareButton is Green's PlayerPiece.
     */
    public boolean isGreen(){
        if(piece instanceof PlayerPiece){
            return ((PlayerPiece) piece).greenPlayer();
        }
        return false;
    }

    public void flagMoved() {
        this.moved = true;
    }

    public void flagSelected() {
        this.selected = true;
    }

    public Position getPosition(){
        return position;
    }

    public Panel getPanelType(){
        return panelType;
    }

    public void setPanelType(Panel panel){
        this.panelType = panel;
    }

}
