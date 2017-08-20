package gui;

import gui.square.SquareButton;
import gui.views.PrimaryView;
import model.Game;
import model.Position;
import model.piece.Piece;
import model.piece.PlayerPiece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.Game.Phase.*;

public class Controller implements ActionListener {

    private Game gameModel;
    private PrimaryView view;

    public Controller(Game g, PrimaryView v){
        this.gameModel = g;
        this.view = v;
    }

    public void mainMenuPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "Quit":
                System.exit(0);
                break;
            case "Info":
                view.showInformation();
                break;
            case "Begin":
                view.showGame();
                break;
        }
    }

    public void infoViewPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "Back":
                view.showMainMenu();
                break;
        }
    }

    public void gameViewPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "Undo":
                gameModel.undo();
                gameModel.setStatus("Undo");
                break;
        }

        if(squareInBoard(e.getSource())){
            SquareButton squareButton = (SquareButton) e.getSource();

            switch(squareButton.getSquareType()){
                case CREATION_SHELF_GREEN:
                    // Get the selected piece.
                    Piece piece = gameModel.getCurrentPlayer().hand.getPiece(squareButton);

                    // Proceed to creation phase only is the player clicks on its own panel,
                    // have an empty creation grid and clicks on a PlayerPiece.
                    if(gameModel.getCurrentPlayer().isGreen()
                            && gameModel.getCurrentPlayer().validCreation()
                            && piece instanceof PlayerPiece) {
                        PlayerPiece selected = (PlayerPiece) piece;
                        gameModel.getCurrentPlayer().hand.setSelected(selected);
                        gameModel.setGamePhase(CREATE);
                        gameModel.setStatus("Choose the following orientation you wish to create");
                    }
                    break;

                case TRAINING:
                    // Get the selected piece.
                    PlayerPiece playerPiece = gameModel
                            .getCurrentPlayer()
                            .hand.getSelectedPiece(squareButton.getPosition());
                    gameModel.createPiece(playerPiece.getLetter(), playerPiece.getRotation());
                    gameModel.setStatus("Created");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainMenuPerformed(e);
        infoViewPerformed(e);
        gameViewPerformed(e);
    }

    public boolean squareInBoard(Object o){
        return o.getClass().equals(SquareButton.class);
    }

    public Position getPosition(Object o){
        return ((SquareButton) o).getPosition();
    }
}
