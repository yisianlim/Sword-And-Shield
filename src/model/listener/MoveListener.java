package model.listener;

import java.util.Scanner;
import model.Game;
import model.player.Player;
import model.view.Interface;

public class MoveListener extends Listener {

    private String letter;
    private Player.Direction direction;

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
            game.movePiece(letter, direction, true);
            return true;
        } catch (IllegalArgumentException e) {
            Interface.fail(e.getMessage());
            return false;
        }
    }

    private Player.Direction parseDirection(Scanner s) {
        String dir = s.next().toLowerCase();
        switch(dir){
            case "up":
                return Player.Direction.UP;
            case "down":
                return Player.Direction.DOWN;
            case "left":
                return Player.Direction.LEFT;
            case "right":
                return Player.Direction.RIGHT;
            default:
                return null;
        }
    }
}
