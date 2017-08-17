package gui;

import model.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    Game gameModel;
    View view;

    public Controller(Game g, View v){
        this.gameModel = g;
        this.view = v;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "Quit":
                System.exit(0);
                break;
            case "Info":
                view.showInformation();
                break;
            case "Begin":
                // GameView
                break;
        }
    }
}
