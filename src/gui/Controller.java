package gui;

import gui.square.SquareButton;
import gui.views.PrimaryView;
import model.Game;
import gui.views.GameView.*;
import model.Position;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        // Retrieve the Position of the SquareButton that the user clicked on the Board.
        if(squareInBoard(e.getSource())){
            Position position = getPosition(e.getSource());
            System.out.println("X: " + position.getX() + " Y: " + position.getY());
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
