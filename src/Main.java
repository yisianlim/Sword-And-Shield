import gui.View;
import model.Board;
import model.Game;
import view.Interface;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        Game game = new Game(board);
        SwingUtilities.invokeLater(()->new View(game));
    }
}
