import game.model.Board;
import game.model.Game;
import game.model.view.Interface;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        Game game = new Game(board);
        Interface ui = new Interface(game);
    }
}
