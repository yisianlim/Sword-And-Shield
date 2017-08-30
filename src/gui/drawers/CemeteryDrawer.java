package gui.drawers;

import gui.views.PrimaryView;
import model.Game;
import model.Position;
import model.piece.Piece;
import model.player.Player;

import javax.swing.*;
import java.awt.*;

/**
 * CemeteryDrawers handles all the logic of drawing the cemetery.
 */
public class CemeteryDrawer extends JPanel {

    /**
     * Model of the GUI.
     */
    private Game gameModel;

    /**
     * Dimension of the board.
     */
    private int width;
    private int height;

    /**
     * The player we are drawing the cemetery for.
     */
    private Player player;

    public CemeteryDrawer(Player player, Game gameModel){
        this.player = player;
        this.gameModel = gameModel;

        // Calculate the width and height for the cemetery.
        this.width = (int) (PrimaryView.PANEL_WIDTH_RATIO * PrimaryView.getPrimaryViewWidth());
        this.height = (int) (PrimaryView.PANEL_HEIGHT_RATIO * PrimaryView.getPrimaryViewHeight());
    }

    /**
     * Returns a JPanel rendered as a cemetery based on the player.
     * @return
     *      JPanel of player's cemetery.
     */
    public JPanel createCemetery(){
        setPreferredSize(new Dimension(width, height));
        setLayout(new GridLayout(4, 6, 10,10));

        // Get the model's cemetery.
        Piece[][] cemetery = gameModel.getCemetery().getPlayerPiecesInCemetery(player);

        // Draw the cemetery.
        for(int row = 0; row < cemetery.length; row++){
            for(int col = 0; col < cemetery[0].length; col++){
                Position currentPosition = new Position(row, col);
                SquareButton squareButton = new SquareButtonDrawer(cemetery[row][col],
                        currentPosition,
                        SquareButton.Panel.CEMETERY).makeButton();
                add(squareButton);
            }
        }
        return this;
    }
}
