package model.view;

import static model.Game.Phase.ACTION;
import static model.Game.Phase.CREATE;

import java.util.Scanner;
import model.Game;
import model.listener.*;
import model.piece.PlayerPiece;

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
            createPhase();
            actionPhase(false);
            game.nextPlayer();
        }
    }

    private void actionPhase(boolean undo) {
        System.out.println("why u do draw");
        game.drawActionPhase();
        game.setGamePhase(ACTION);

        // Undo command does not need to update.
        // We simply use the previous state of unactedPieces.
        if(!undo)
            game.resetFuture();

        // Listener that listens to the user input at this stage.
        Listener listener;

        // Action phase goes on as long as there are still unacted pieces.
        while(!game.getFuture().isEmpty()){
            System.out.print("You can move the following pieces: ");
            for (PlayerPiece playerPiece : game.getFuture()) {
                System.out.print(playerPiece.getLetter() + " ");
            }
            System.out.println();



            listener = parseInitialActionPhase(READER);

            if(listener != null){
                // If it is a pass, then we end action phase.
                if(listener instanceof PassListener) return;

                // If it is an undo, we need to check if we need to go back to create phase.
                System.out.println(game.isUndoAvailable());
                if(listener instanceof UndoListener && !game.isUndoAvailable()){
                    createPhase();
                }

            } else {
                fail("Please try again\n");
                READER.nextLine();
                continue;
            }

            game.drawActionPhase();
        }

        finalActionPhase();
    }

    private void finalActionPhase(){
        // The final stage of the action phase is when all the pieces have been acted.
        // The user can only input "pass" to move onto the next user or "undo" to go back.
        Listener listener = null;
        while(listener == null) {
            listener = parseFinalActionPhase(READER);
            if (listener instanceof PassListener) {
                return;
            } else if (listener instanceof UndoListener && !game.isUndoAvailable()) {
                //TODO: FIX THIS STRUCTURE HERE.
                createPhase();
                actionPhase(true);
            } else if(listener instanceof UndoListener){
                actionPhase(true);
            } else {
                fail("Please try again\n");
                READER.nextLine();
            }
        }
    }

    private void createPhase() {
        game.drawCreatePhase();
        game.setGamePhase(CREATE);

        // While waiting for a valid input.
        // Break from the loop if the input was successfully parsed.
        while(true) {
            Listener listener = parseCreatePhase(READER);
            if(listener != null){
                break;
            } else {
                fail("Please try again\n");
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

    private Listener parseInitialActionPhase(Scanner scan){
        Listener listener = new MoveListener(scan, game);
        if(listener.isMove()){
            if(listener.parse()){
                return listener;
            } else {
                return null;
            }
        }

        listener = new RotateListener(scan, game);
        if(listener.isRotate()){
            if(listener.parse()){
                return listener;
            } else {
                return null;
            }
        }

        listener = new PassListener(scan, game);
        if(listener.isPass()){
            if(listener.parse()){
                return listener;
            } else {
                return null;
            }
        }

        listener = new UndoListener(scan, game);
        if(listener.isUndo()){
            if(listener.parse()){
                return listener;
            } else {
                return null;
            }
        }
        return null;
    }

    public Listener parseFinalActionPhase(Scanner scan){
        Listener listener = new PassListener(scan, game);
        if(listener.isPass()){
            if(listener.parse()){
                return listener;
            } else {
                return null;
            }
        }

        listener = new UndoListener(scan, game);
        if(listener.isUndo()){
            if(listener.parse()){
                return listener;
            } else {
                return null;
            }
        }
        return null;
    }

    public static void fail(String message){
        System.out.println(message);
    }
}
