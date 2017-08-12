package model.listener;

import model.Game;
import view.Interface;

import java.util.Scanner;

/**
 * UndoListener simply listens for the undo command by the user. In the event where the parse is successfully, the game
 * will undo to go back to its previous state.
 */
public class UndoListener extends Listener {

    public UndoListener(Scanner scanner, Game game){
        super(scanner, game);
    }

    @Override
    public boolean parse() {
        scanner.next(); // Gobble up the undo command.
        try{
            game.undo();
            return true;
        } catch (IllegalArgumentException e){
            Interface.fail(e.getMessage());
            return false;
        }
    }
}
