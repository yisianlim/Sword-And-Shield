package model.listener;

import model.Game;

import java.util.Scanner;

/**
 * PassListener simply listens for any "pass" command by the user.
 */
public class PassListener extends Listener {

    public PassListener(Scanner scanner, Game game){
        super(scanner, game);
    }

    /**
     * parse() is invoked in the event where isPass() is successfully.
     * Returns true if pass is successfully invoked in the game.
     * If the game is in CREATE phase, we move on to ACTION phase.
     * If the game is in ACTION phase, we will pass the game to the next player.
     * @return
     */
    public boolean parse() {
        scanner.next(); // Gobble up the "pass" command.
        game.pass();
        return true;
    }
}
