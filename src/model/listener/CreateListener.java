package model.listener;

import model.Game;
import view.Interface;

import java.util.Scanner;

/**
 * CreateListener simply listens for any "create" command by the user.
 */
public class CreateListener extends Listener{

    private String letter;
    private int rotation;

    public CreateListener(Scanner scanner, Game game){
        super(scanner, game);
    }

    /**
     * parse is invoked in the event where isCreate is successful.
     * Return true if create is successfully invoked in the game.
     * Return false if the user inputs an invalid create command such as :
     *      - create a Piece not belonging to them.
     *      - create a Piece when their CREATION_GRID is full.
     *      - create a Piece with rotation that is not 0, 90, 180, 270.
     * @return
     */
    @Override
    public boolean parse() {
        scanner.next(); // Gobble up "create" command.

        letter = scanner.next();

        if(!scanner.hasNextInt()){
            Interface.fail("You need to input an integer");
            return false;
        }

        rotation = scanner.nextInt();

        try {
            game.createPiece(letter, rotation);
            return true;
        } catch(IllegalArgumentException e){
            Interface.fail(e.getMessage());
            return false;
        }
    }
}
