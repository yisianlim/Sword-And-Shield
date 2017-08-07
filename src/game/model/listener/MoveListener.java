package game.model.listener;

import game.model.Game;
import game.model.player.Player.Direction;
import game.model.view.Interface;

import java.util.Scanner;

public class MoveListener extends Listener {

    private String letter;
    private Direction direction;

    public MoveListener(Scanner scanner, Game game){
       super(scanner, game);
    }

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
            game.movePiece(letter, direction);
            return true;
        } catch (IllegalArgumentException e) {
            Interface.fail(e.getMessage());
            return false;
        }
    }

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
