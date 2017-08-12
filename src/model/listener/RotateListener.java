package model.listener;

import model.Game;
import view.Interface;

import java.util.Scanner;

public class RotateListener extends Listener {

    private String letter;
    private int rotation;

    public RotateListener(Scanner scanner, Game game){
        super(scanner, game);
    }

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
