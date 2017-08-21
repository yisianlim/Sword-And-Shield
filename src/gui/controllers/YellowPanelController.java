package gui.controllers;

import gui.drawers.SquareButton;
import model.Game;
import model.piece.Piece;
import model.piece.PlayerPiece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.Game.Phase.ACTION;
import static model.Game.Phase.CREATE;
import static model.Game.Phase.FINAL;

public class YellowPanelController implements ActionListener {

    private Game gameModel;

    public YellowPanelController(Game g) {
        this.gameModel = g;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (squareInBoard(e.getSource())) {
            SquareButton squareButton = (SquareButton) e.getSource();

            switch (squareButton.getPanelType()) {
                case CREATION_SHELF:
                    if(gameModel.getGamePhase().equals(FINAL)
                            || gameModel.getGamePhase().equals(ACTION)){
                        gameModel.warningMessage("You have passed the creation phase.");
                        return;
                    }

                    if(!gameModel.getCurrentPlayer().validCreation()){
                        gameModel.warningBeep("Creation grid is occupied!");
                        return;
                    }

                    if(!gameModel.getCurrentPlayer().isYellow()){
                        gameModel.warningMessage("These pieces does not belong to you!");
                        return;
                    }

                    // Get the selected piece.
                    Piece yellowPiece = gameModel.getCurrentPlayer().hand.getPiece(squareButton);

                    // Proceed to creation phase only is the player clicks on its own panel,
                    // have an empty creation grid and clicks on a PlayerPiece.
                    if(yellowPiece instanceof PlayerPiece) {
                        PlayerPiece selected = (PlayerPiece) yellowPiece;
                        gameModel.getCurrentPlayer().hand.setSelected(selected);
                        gameModel.setGamePhase(CREATE);
                        gameModel.setStatus("Choose the following orientation you wish to create");
                    }
                    break;

                case TRAINING:
                    // Get the selected piece.
                    PlayerPiece playerPiece = gameModel
                            .getCurrentPlayer()
                            .hand
                            .getSelectedPiece(squareButton.getPosition());
                    gameModel.createPiece(playerPiece.getLetter(), playerPiece.getRotation());
                    gameModel.setStatus("Created");

                    //TODO: Debugging
                    System.out.println(gameModel.getGamePhase().toString());
                    System.out.println(gameModel.getCurrentPlayer().getName());

                    break;
            }
        }

    }

    public boolean squareInBoard(Object o) {
        return o.getClass().equals(SquareButton.class);
    }
}
