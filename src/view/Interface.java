package view;

import java.util.Scanner;
import model.Game;
import model.listener.*;
import model.piece.PlayerPiece;

/**
 * Provides a simple interface for the Sword and Shield Game.
 */
public class Interface {

    public final Game game;
    public final Scanner READER;

    public Interface(Game game) {
        this.game = game;
        this.READER = new Scanner(System.in);
        splashScreen();

        // Wait for user response.
        KeyListener keyListener = new KeyListener(READER, game);
        if(keyListener.parse()){
            beginGame();
        }
    }

    /**
     * Introduction and instruction of the game to the user.
     */
    private void splashScreen() {
        System.out.println("======================= Welcome to Sword And Shield ======================");
        System.out.println("=======================      By: Yi Sian Lim        ======================");
        System.out.println("\n");

        // Explain instruction.
        System.out.println(
                "Since this is a console based game, the players are denoted as follows:\n"+
                "Green Player  > 0\n" +
                "Yellow Player > 1\n\n" +
                "The warriors the players can summon are placed at:\n" +
                "Green Player  > LEFT\n" +
                "Yellow Player > RIGHT\n\n"
        );

        System.out.println(
                "At the start of each round, which is the creation phase, the user can type the following commands:\n" +
                "\"create <letter> <0/90/180/270>\"     -> summon a warrior into the board\n" +
                "\"pass\"                               -> pass in summoning warrior and move on to action phase\n\n"+
                "During the action phase, the user can type the following commands:\n" +
                "\"move <letter> <up/down,left,right>\" -> move the warrior in the board\n" +
                "\"rotate <letter> <0/90/180/270>\"     -> rotate the warrior in the board\n" +
                "\"pass\"                               -> end the current player's turn\n" +
                "\"undo\"                               -> revert the last command\n"
        );

        System.out.println("\t\t\t\tPress any key then ENTER to continue");
    }

    /**
     * Begin the game. The game goes on until a winner is declared.
     */
    private void beginGame(){
        while(!game.gameOver()){
            switch(game.getGamePhase()){
                case CREATE:
                    createPhase();
                    break;
                case ACTION:
                    actionPhase();
                    break;
                case FINAL:
                    finalActionPhase();
                    break;
            }
        }
        declareWinner();
    }

    /**
     * At the create phase of the game, it only listens for the create and pass command.
     * Other commands are considered invalid.
     */
    private void createPhase() {
        game.drawCreatePhase();

        // While waiting for a valid input.
        // Break from the loop if the input was successfully parsed.
        while(true) {
            Listener listener = parseCreatePhase(READER);
            if(listener != null){
                return;
            } else {
                fail("Please try again\n");
                READER.nextLine();
                continue;
            }
        }
    }

    /**
     * At the action phase of the game, it listens for the move, rotate, pass or undo command.
     * Other commands are considered invalid.
     */
    private void actionPhase() {
        game.drawActionPhase();
        Listener listener;
        // Action phase goes on as long as there are still future pieces.
        while(true){
            System.out.print("You can move the following pieces: ");
            for (PlayerPiece playerPiece : game.getFuture()) {
                System.out.print(playerPiece.getLetter() + " ");
            }
            System.out.println();
            listener = parseActionPhase(READER);
            if(listener == null) {
                fail("Please try again\n");
                READER.nextLine();
                continue;
            }
            if(listener != null){
                return;
            }
            game.drawActionPhase();
        }
    }

    /**
     *  At the final action phase of the game (when all the pieces on the board have been moved / rotated)
     *  The user can only input "pass" to end its turn or "undo" to revert its previous commands.
     */
    private void finalActionPhase(){
        game.drawActionPhase();
        Listener listener = null;
        while(listener == null) {
            listener = parseFinalActionPhase(READER);
            if(listener == null){
                fail("Please try again\n");
                READER.nextLine();
            }
        }
    }

    /**
     * Declare the winning player.
     */
    private void declareWinner() {
        game.draw();
        System.out.println(game.getWinner() + " has won!");
    }

    /**
     * Parse the input during create phase.
     * @param scan
     *          Scanner to parse.
     * @return
     *      Corresponding listener based on the command. Null if the command was invalid.
     */
    private Listener parseCreatePhase(Scanner scan){
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

    /**
     * Parse the input during action phase.
     * @param scan
     *          Scanner to parse.
     * @return
     *      Corresponding listener based on the command. Null if the command was invalid.
     */
    private Listener parseActionPhase(Scanner scan){
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

    /**
     * Parse the input during final action phase.
     * @param scan
     *          Scanner to parse.
     * @return
     *      Corresponding listener based on the command. Null if the command was invalid.
     */
    private Listener parseFinalActionPhase(Scanner scan){
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
