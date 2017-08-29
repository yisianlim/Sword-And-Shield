package gui.controllers;

import gui.drawers.Dialogs;
import gui.drawers.SquareButton;
import model.Game;
import model.piece.Piece;
import model.piece.PlayerPiece;
import model.player.Player;

import javax.swing.*;
import java.awt.event.*;

import static model.Game.Phase.ACTION;

/**
 * BoardController contains the code for interfacing with the game board.
 * It contains most of the game logic that is used to the control how the player
 * interacts with game and ensure that an invalid move done by the user is handled
 * gracefully.
 */
public class BoardController implements ActionListener, MouseListener {

    /**
     * The Model of the GUI.
     */
    private Game gameModel;

    /**
     * Allowable region from the mouse click.
     */
    private static final int OFFSET = 10;

    public BoardController(Game g) {
        this.gameModel = g;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SquareButton squareButton = (SquareButton) e.getSource();

        // Get the selected piece.
        Piece piece = gameModel.getBoard().getPiece(squareButton);
        if (!gameModel.getGamePhase().equals(ACTION)) {
            Dialogs.actionErrorDialog("You are not allowed to move / rotate your pieces at this stage");
            return;
        }
        if (!(piece instanceof PlayerPiece)) {
            Dialogs.actionErrorDialog("Please select a valid PlayerPiece");
            return;
        }
        PlayerPiece selected = (PlayerPiece) piece;
        if (!gameModel.getCurrentPlayer().validPiece(selected)) {
            Dialogs.actionErrorDialog("This piece does not belong to you!");
            return;
        }
        if (!gameModel.getFuture().contains(selected)) {
            Dialogs.actionErrorDialog("This piece have already been moved and rotated!");
            return;
        }

        // Highlight the selected piece.
        gameModel.board.setSelectedSquare(squareButton.getPosition());
        gameModel.setStatus("Selected");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // MouseListener is only added to selected piece.
        // Get the selected piece to move in the board.
        PlayerPiece toMove = (PlayerPiece) gameModel.getBoard().getPiece((SquareButton) e.getSource());
        Player.Direction headedDirection = getDirection(e);

        if(headedDirection == null){
            Dialogs.actionErrorDialog("Cannot identify a valid direction. Please try again");
            return;
        }

        // Move the piece in the board and update the game state accordingly.
        gameModel.movePiece(toMove.getLetter(), headedDirection, true);
    }

    /**
     * Return the appropriate Player.Direction based on the position of the mouse click.
     * @param e
     *          MouseEvent of the mouse click.
     * @return
     *          Player.Direction to move the selected piece.
     */
    private Player.Direction getDirection(MouseEvent e){
        int width = ((SquareButton) e.getSource()).getWidth();
        int height = ((SquareButton) e.getSource()).getHeight();
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Check for top region.
        if(mouseX >= OFFSET && mouseX <= (width - OFFSET) && mouseY >= 0 && mouseY <= OFFSET){
            return Player.Direction.UP;
        }

        // Check for bottom region.
        if(mouseX >= OFFSET && mouseX <= (width - OFFSET) && mouseY >= height - OFFSET && mouseY <= height){
            return Player.Direction.DOWN;
        }

        // Check for left region.
        if(mouseX >= 0 && mouseX <= OFFSET && mouseY >= OFFSET && mouseY <= height - OFFSET){
            return Player.Direction.LEFT;
        }

        // Check for right region.
        if(mouseX >= width - OFFSET && mouseX <= width && mouseY >= OFFSET && mouseY <= height - OFFSET){
            return Player.Direction.RIGHT;
        }

        return null;
    }

    /**
     * bindWASDKey binds the squareButton to the WASD keys on the keyboard allowing
     * players to move the selected piece using the following keys:
     * W - move up
     * A - move left
     * S - move down
     * D - move right
     * @param squareButton
     *          SquareButton to bind the WASD keys to.
     */
    public void bindWASDKey(SquareButton squareButton) {
        PlayerPiece toMove = (PlayerPiece) gameModel.getBoard().getPiece(squareButton);

        // Update the InputMap on the squareButton
        // Binds WASD keys to a specified action value.
        squareButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("W"), "UpAction");
        squareButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("A"), "LeftAction");
        squareButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("S"), "DownAction");
        squareButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("D"), "RightAction");

        // Update the ActionMap on the squareButton.
        // Binds the action value to a FunctionalAction in order to execute the action.
        squareButton.getActionMap().put("UpAction", new FunctionalAction(ae -> {
            gameModel.movePiece(toMove.getLetter(), Player.Direction.UP, true);
        }));
        squareButton.getActionMap().put("LeftAction", new FunctionalAction(ae -> {
            gameModel.movePiece(toMove.getLetter(), Player.Direction.LEFT, true);
        }));
        squareButton.getActionMap().put("DownAction", new FunctionalAction(ae -> {
            gameModel.movePiece(toMove.getLetter(), Player.Direction.DOWN, true);
        }));
        squareButton.getActionMap().put("RightAction", new FunctionalAction(ae -> {
            gameModel.movePiece(toMove.getLetter(), Player.Direction.RIGHT, true);
        }));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
