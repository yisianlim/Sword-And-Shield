package game.model.view;

import game.model.Game;
import game.model.listener.*;
import game.model.piece.PlayerPiece;

import java.util.Scanner;

import static game.model.Game.Phase.*;

/**
 * Provides a simple interface for the Sword and Shield Game.
 *
 *
 */
public class Interface {
    public final Game game;
    public final Scanner READER;

    public Interface(Game game) {
        this.game = game;
        this.READER = new Scanner(System.in);
        splashScreen();
    }

    private void splashScreen() {
        System.out.println("========= Welcome to Sword And Shield ========");
        System.out.println("=========      By: Yi Sian Lim        ========");
        System.out.println("\n");

        // Explain instruction.

        // Wait for user response.
        System.out.println("Press any key then ENTER to continue");
        KeyListener keyListener = new KeyListener(READER, game);
        if(keyListener.parse()){
            beginGame();
        }
    }

    private void beginGame(){
        while(!game.gameOver()) {
            //
            createPhase();
            actionPhase();
            game.nextPlayer();
        }
    }

    private void actionPhase() {
        game.drawActionPhase();
        game.setGamePhase(ACTION);
        game.updateUnactedPieces();

        if(game.getUnactedPieces().isEmpty()){
            System.out.println("Since you have no pieces on the board, we are going to move to the next player\n\n");
            sleep();
            return;
        }

        while(!game.getUnactedPieces().isEmpty()){
            System.out.print("You can move the following pieces: ");
            for (PlayerPiece playerPiece : game.getUnactedPieces()) {
                System.out.print(playerPiece.getLetter() + " ");
            }
            System.out.println();

            Listener listener = parseActionPhase(READER);

            if(listener != null){
                // Add it to record.
            } else {
                fail("Please try again\n");
                READER.nextLine();
                continue;
            }

            // Only draw the game state if it is not empty.
            // We exit the action phase and move on to create phase once all pieces are acted.
            if(!game.getUnactedPieces().isEmpty())
                game.drawActionPhase();
        }

    }

    private Listener parseActionPhase(Scanner scan){
        Listener listener = new MoveListener(scan, game);
        if(listener.isMove()){
            if(listener.parse()){
                return listener;
            } else {
                return null;
            }
        }

        listener = new RotateListener(READER, game);
        if(listener.isRotate()){
            if(listener.parse()){
                return listener;
            } else {
                return null;
            }
        }

        listener = new PassListener(READER, game);
        if(listener.isPass()){
            if(listener.parse()){
                return listener;
            } else {
                return null;
            }
        }
        return null;
    }

    private void createPhase() {
        game.drawCreatePhase();
        game.setGamePhase(CREATE);

        // While waiting for a valid input.
        // Break from the loop if the input was successfully parsed.
        while(true) {
            Listener listener = parseCreatePhase(READER);
            if(listener != null){
                // Add it to record.
                break;
            } else {
                fail("Please try again");
                READER.nextLine();
                continue;
            }
        }
    }

    public Listener parseCreatePhase(Scanner scan){
        Listener listener = new CreateListener(scan, game);
        if (listener.isCreate()) {
            if (listener.parse()) {
                return listener;
            }
            else {
                return null;
            }
        }

        listener = new PassListener(scan, game);
        if(listener.isPass()){
            if(listener.parse()){
                return listener;
            }
            else {
                return null;
            }
        }
        return null;
    }

//    public Listener parseActionPhase(Scanner scan);

    public static void fail(String message){
        System.out.println(message);
    }

    /**
     * Let the console sleep for 2 seconds.
     */
    public final static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
