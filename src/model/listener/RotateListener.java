package model.listener;

import model.Game;
import view.Interface;

import java.util.Scanner;

/**
 * RotateListener simply listens for any "rotate" command by the user.
 */
public class RotateListener extends Listener {

    private String letter;
    private int rotation;

    public RotateListener(Scanner scanner, Game game){
        super(scanner, game);
    }

    /**
     * parse is invoked in the event where isMove is successful.
     * Return true if rotate is successfully invoked in the game.
     * Return false if the user inputs an invalid create command such as :
     *      - rotation that is not 0/90/180/270
     *      - move a Piece not belonging to them.
     *      - move a Piece that is not on the board.
     *      - move a Piece that has already been moved / rotated.
     * @return
     */
    @Override
    public boolean parse() {
        scanner.next(); // Gobble up "rotate" command.

        letter = scanner.next();

        if(!scanner.hasNextInt()){
            Interface.fail("You need to input an integer");
            return false;
        }

        rotation = scanner.nextInt();

        try {
            game.rotatePiece(letter, rotation);
            return true;
        } catch(IllegalArgumentException e){
            Interface.fail(e.getMessage());
            return false;
        }
    }


}
