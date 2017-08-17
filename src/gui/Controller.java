package gui;

import model.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    Game gameModel;

    public Controller(Game g){
        this.gameModel = g;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
