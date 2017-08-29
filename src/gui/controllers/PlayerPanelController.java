package gui.controllers;

import gui.drawers.Dialogs;
import gui.drawers.SquareButton;
import model.Game;
import model.piece.Piece;
import model.piece.PlayerPiece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.Game.Phase.ACTION;
import static model.Game.Phase.CREATE;
import static model.Game.Phase.FINAL;

/**
 * PlayerPanelController handles the user actions in the panels for both green and yellow players.
 * It displays the appropriate error messages in the event the user does an invalid action.
 * It also updates the game based on the user's action.
 * There are different panel types for the SquareButton in the PlayerPanel:
 * - CREATION_SHELF: the stage where all the PlayerPiece in player's hand is laid out.
 * - TRAINING: the stage where all orientations of the selected PlayerPiece is displayed.
 */
public class PlayerPanelController implements ActionListener {

    /**
     * Model of the GUI.
     */
    private Game gameModel;

    public PlayerPanelController(Game g) {
        this.gameModel = g;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the SquareButton clicked.
        SquareButton squareButton = (SquareButton) e.getSource();

        // Determine which panel the SquareButton was clicked.
        switch (squareButton.getPanelType()) {
            // Check valid creation based on the PlayerPiece that have been selected to create.
            case CREATION_SHELF:

                // User cannot select piece that does not belong to them.
                if ((gameModel.getCurrentPlayer().isYellow() && squareButton.isGreen()) ||
                        gameModel.getCurrentPlayer().isGreen() && squareButton.isYellow()) {
                    Dialogs.creationErrorDialog("These pieces does not belong to you!");
                    return;
                }

                // User cannot select piece if the game has passed the creation phase.
                if (gameModel.getGamePhase().equals(FINAL)
                        || gameModel.getGamePhase().equals(ACTION)) {
                    Dialogs.creationErrorDialog("You have passed the creation phase.");
                    return;
                }

                // User cannot select piece is the creation grid is occupied.
                if (!gameModel.getCurrentPlayer().validCreation()) {
                    gameModel.warningBeep("Creation grid is occupied!");
                    return;
                }

                // Get the selected piece.
                Piece selectedPiece = gameModel.getCurrentPlayer().hand.getPiece(squareButton);

                // User cannot create a non-Player Piece.
                if (!(selectedPiece instanceof PlayerPiece)) {
                    Dialogs.creationErrorDialog("This is not a valid piece to create");
                }

                // Proceed to the next phase of creation phase in which the user can choose
                // the orientation of the piece they selected.
                PlayerPiece selected = (PlayerPiece) selectedPiece;

                // Set the piece as selected in the player's hand and update model.
                gameModel.getCurrentPlayer().hand.setSelected(selected);
                gameModel.setGamePhase(CREATE);
                gameModel.setStatus("Choose the following orientation you wish to create");
                break;

            case TRAINING:
                // Get the selected piece by the player.
                PlayerPiece playerPiece = gameModel
                        .getCurrentPlayer()
                        .hand
                        .getSelectedPiece(squareButton.getPosition());

                // Create the selected piece
                gameModel.createPiece(playerPiece.getLetter(), playerPiece.getRotation());
                break;
        }
    }

}
