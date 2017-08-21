package gui.controllers;

import gui.drawers.SquareButton;
import model.Game;
import model.piece.Piece;
import model.piece.PlayerPiece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.Game.Phase.ACTION;

public class BoardController implements ActionListener {

    private Game gameModel;

    public BoardController(Game g){
        this.gameModel = g;

    }
    @Override
    public void actionPerformed(ActionEvent e) {

        SquareButton squareButton = (SquareButton) e.getSource();

        // Get the selected piece.
        Piece piece = gameModel.getBoard().getPiece(squareButton);

        if(!gameModel.getGamePhase().equals(ACTION)){
            gameModel.warningMessage("You are not allowed to move / rotate your pieces at this stage");
            return;
        }

        if(!(piece instanceof PlayerPiece)){
            gameModel.warningMessage("Please select a valid PlayerPiece");
            return;
        }

        PlayerPiece selected = (PlayerPiece) piece;

        if(!gameModel.getCurrentPlayer().validPiece(selected)){
            gameModel.warningMessage("This piece does not belong to you!");
            return;
        }

        if(!gameModel.getFuture().contains(selected)){
            gameModel.warningMessage("This piece have already been moved and rotated!");
            return;
        }

        gameModel.board.setSelectedSquare(squareButton.getPosition());
        gameModel.setStatus("Selected");
    }
}
