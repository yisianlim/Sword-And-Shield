package gui.drawers;

import gui.views.GameView;
import gui.views.PrimaryView;
import model.Game;
import model.Position;
import model.piece.Piece;
import model.piece.PlayerPiece;
import model.player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static model.Game.Phase.CREATE;

/**
 * PlayerPanelDrawer contains the logic of creating the JPanel for each player.
 */
public class PlayerPanelDrawer extends JPanel {

    /**
     * Player to create the panel for.
     */
    private Player player;

    /**
     * Model of the game.
     */
    private Game gameModel;

    public PlayerPanelDrawer(Player player, Game game) {
        this.player = player;
        this.gameModel = game;
    }

    /**
     * Return a JPanel that should be rendered as the top component of the yellowPlayerPane.
     * There are two types of panel to be rendered out based on the phase of the game.
     *      - CREATE: Display all the orientations of the selected PlayerPiece to the user.
     *      - Other phases: Display all the PlayerPiece in the player's hand.
     * @return
     */
    public PlayerPanelDrawer createPanel(){
        Game.Phase phase = gameModel.getGamePhase();

        int panelWidth = (int) (PrimaryView.PANEL_WIDTH_RATIO * PrimaryView.getPrimaryViewWidth());
        int panelHeight = (int) (PrimaryView.PANEL_HEIGHT_RATIO * PrimaryView.getPrimaryViewHeight());
        setPreferredSize(new Dimension(panelWidth, panelHeight));

        if(phase.equals(CREATE) && samePlayer()){
            // Display all the orientations of the selected PlayerPiece.
            setLayout(new GridLayout(1,4));

            // Get all the selected PlayerPiece with the various orientation.
            List<PlayerPiece> allOrientations = gameModel.getCurrentPlayer().hand.getSelectedInAllOrientations();

            // Create a custom, responsive SquareButton for each PlayerPiece for the panel.
            // Add the SquareButton into JPanel greenPanelCreateMode.
            for(int i = 0; i < 4; i++){
                PlayerPiece current = allOrientations.get(i);
                SquareButton squareButton = new SquareButtonDrawer(current,
                        new Position(0, i),
                        SquareButton.Panel.TRAINING).makeButton();
                squareButton.addActionListener(GameView.playerPanelController);
                add(squareButton);
            }

        } else {
            // Display all the PlayerPiece available in player's hand.
            setLayout(new GridLayout(4, 6, 10, 10));

            // Get all the Piece that the player currently have on hand.
            Piece[][] hand = player.hand.getArrayRepresentation();

            // Create a custom SquareButton for each piece in hand for the panel.
            // Add the SquareButton into JPanel greenPanelDisplayMode.
            for (int row = 0; row < hand.length; row++) {
                for (int col = 0; col < hand[0].length; col++) {
                    Position currentPosition = new Position(row, col);
                    SquareButton squareButton = new SquareButtonDrawer(
                            hand[row][col],
                            currentPosition,
                            SquareButton.Panel.CREATION_SHELF)
                            .makeButton();
                    squareButton.addActionListener(GameView.playerPanelController);
                    add(squareButton);
                }
            }
        }
        return this;
    }

    /**
     * Returns true if the game's current player and the player we are looking at are the same player
     * in order to determine whether the panel should show all the orientations of the PlayerPiece.
     * @return
     */
    private boolean samePlayer(){
        return (gameModel.getCurrentPlayer().isGreen() && player.isGreen()) ||
                (gameModel.getCurrentPlayer().isYellow() && player.isYellow());
    }


}
