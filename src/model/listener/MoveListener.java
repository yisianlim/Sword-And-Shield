package model.listener;

import java.util.Scanner;
import model.Game;
import model.player.Player.Direction;
import view.Interface;

/**
 * MoveListener simply listens for any "move" command by the user.
 */
public class MoveListener extends Listener {

    private String letter;
    private Direction direction;

    public MoveListener(Scanner scanner, Game game){
       super(scanner, game);
    }

    /**
     * parse is invoked in the event where isMove is successful.
     * Return true if move is successfully invoked in the game.
     * Return false if the user inputs an invalid create command such as :
     *      - direction that is NOT left/right/up/down
     *      - move a Piece not belonging to them.
     *      - move a Piece that is not on the board.
     *      - move a Piece that has already been moved / rotated.
     * @return
     */
    @Override
    public boolean parse() {
        scanner.next(); // Gobble up the "move" command

        letter = scanner.next();

        direction = parseDirection(scanner);

        // Check if the direction inputted is a valid one.
        if(direction == null){
            Interface.fail("We require a left, right, up or down command only");
            return false;
        }

        try{
            game.movePiece(letter, direction, true);
            return true;
        } catch (IllegalArgumentException e) {
            Interface.fail(e.getMessage());
            return false;
        }
    }

    /**
     * Parse the direction input by the user. Returns null if the input was unsuccessfully parsed.
     * @param s
     *          Scanner to parse the direction input.
     * @return
     *          Respective direction as inputted by the user.
     */
    private Direction parseDirection(Scanner s) {
        String dir = s.next().toLowerCase();
        switch(dir){
            case "up":
                return Direction.UP;
            case "down":
                return Direction.DOWN;
            case "left":
                return Direction.LEFT;
            case "right":
                return Direction.RIGHT;
            default:
                return null;
        }
    }
}
