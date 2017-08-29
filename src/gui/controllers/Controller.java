package gui.controllers;

import gui.drawers.Dialogs;
import gui.views.PrimaryView;
import model.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller handles all the actions that should be after any button click in all views
 * (except for SquareButton which needs to be handled separately in its respective panels).
 */
public class Controller implements ActionListener {

    /**
     * Model of the GUI.
     */
    private Game gameModel;

    public Controller(Game g){
        this.gameModel = g;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainMenuPerformed(e);
        infoViewPerformed(e);
        gameViewPerformed(e);
    }

    public void mainMenuPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "Quit":
                // Exit the game.
                System.exit(0);
                break;
            case "Info":
                // Display the information about the game.
                PrimaryView.showInformation();
                break;
            case "Begin":
                // Display the game.
                PrimaryView.showGame();
                break;
        }
    }

    public void infoViewPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "Back":
                // Go back to the main menu.
                PrimaryView.showMainMenu();
                break;
        }
    }

    public void gameViewPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "Undo":
                // Undo the state of the game.
                // In the event where the game can no longer undo, we display an error dialog to user.
                try {
                    gameModel.undo();
                } catch(IllegalArgumentException error){
                    Dialogs.undoErrorDialog(error.getMessage());
                    return;
                }
                break;

            case "Pass":
                // Pass the game.
                gameModel.pass();
                break;

            case "Surrender":
                // Update the game to set the winner
                // Notify the players of the winner with a dialog.
                gameModel.playerHasSurrender();
                Dialogs.gameOverDialog(
                        getStatistics(),
                        gameModel.getWinnerName()
                );
        }
    }

    public String getStatistics(){
        String statistics = "Number of moves was " + gameModel.moves() + "\n";
        statistics += "Time taken to play was " + gameModel.timeTaken() + "\n";
        statistics += gameModel.undoMoves() + "\n";
        statistics += gameModel.deadPieces();
        return statistics;
    }

}
