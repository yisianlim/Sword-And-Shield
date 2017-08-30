package gui.drawers;

import gui.views.PrimaryView;

import javax.swing.*;

/**
 * Dialogs creates various dialog boxes to get input from the user in different situations
 * as well as notify the user in the event where they have executed an invalid move.
 * (eg. Trying to create a piece more than twice, trying to move a piece twice, etc)
 */
public class Dialogs {

    /**
     * Notify the user that the game is over and who has won the game.
     * Clicking "OK" would navigate the user back to the main menu and restart the game once again.
     * @param msg
     *          Message to be displayed to the user.
     */
    public static void gameOverDialog(String msg, String winner){
        String[] options = {"OK"};

        int choice = JOptionPane.showOptionDialog(null, msg, winner + " player won!",
                JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(choice == 0){
            PrimaryView.restartGame();
        }
    }

    /**
     * Notify the user of the error they did during the creation phase of the game.
     * @param msg
     *          Message to be displayed to the user.
     */
    public static void creationErrorDialog(String msg){
        JOptionPane.showMessageDialog(null, msg, "Creation error", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Notify the user of the error they did during the action phase of the game.
     * @param msg
     *          Message to be displayed to the user.
     */
    public static void actionErrorDialog(String msg){
        JOptionPane.showMessageDialog(null, msg, "Action error", JOptionPane.WARNING_MESSAGE);

    }

    /**
     * Notify the user that all undoable commands have been undone. No more commands can be undone.
     * @param msg
     *          Message to be displayed to the user.
     */
    public static void undoErrorDialog(String msg){
        JOptionPane.showMessageDialog(null, msg, "Invalid undo", JOptionPane.WARNING_MESSAGE);
    }


}
