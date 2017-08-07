package model.listener;

import model.Game;

import java.util.Scanner;

/**
 * KeyListener simply listens for any key input by the user. It is used to note that the player is
 * ready to begin the game.
 */
public class KeyListener extends Listener {
        public KeyListener(Scanner scanner, Game game){
            super(scanner, game);
        }

    @Override
    public boolean parse() {
        // Wait for players to be ready before beginning the game.
        while (true) {
            if (scanner.hasNext()) {
                scanner.next();
                return true;
            }
        }
    }
}
