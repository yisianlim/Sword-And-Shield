package gui.controllers;

import gui.views.PrimaryView;
import model.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    private Game gameModel;
    private PrimaryView view;

    public Controller(Game g, PrimaryView v){
        this.gameModel = g;
        this.view = v;
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
                try {
                    gameModel.undo();
                    gameModel.setStatus("Undo");
                } catch(IllegalArgumentException error){
                    gameModel.warningMessage(error.getMessage());
                    return;
                }
                break;

            case "Pass":
                gameModel.setStatus("Pass");
                gameModel.pass();
        }
    }

}
