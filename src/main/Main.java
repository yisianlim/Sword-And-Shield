package main;

import gui.views.PrimaryView;
import model.Board;
import model.Game;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        Game game = new Game(board);
        SwingUtilities.invokeLater(() -> new PrimaryView(game));
    }

    /**
     * Called to restart/start a new game
     */
    public static void newGame(){
        PrimaryView.restartGame();
    }
}
