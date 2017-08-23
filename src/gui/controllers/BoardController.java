package gui.controllers;

import gui.drawers.SquareButton;
import model.Game;
import model.piece.Piece;
import model.piece.PlayerPiece;
import model.player.Player;

import java.awt.event.*;

import static model.Game.Phase.ACTION;

public class BoardController implements ActionListener, MouseListener, KeyListener {

    private Game gameModel;

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
            gameModel.warningMessage("You are not allowed to move / rotate your pieces at this stage");
            return;
        }
        if (!(piece instanceof PlayerPiece)) {
            gameModel.warningMessage("Please select a valid PlayerPiece");
            return;
        }
        PlayerPiece selected = (PlayerPiece) piece;
        if (!gameModel.getCurrentPlayer().validPiece(selected)) {
            gameModel.warningMessage("This piece does not belong to you!");
            return;
        }
        if (!gameModel.getFuture().contains(selected)) {
            gameModel.warningMessage("This piece have already been moved and rotated!");
            return;
        }

        // Highlight the selected piece.
        gameModel.board.setSelectedSquare(squareButton.getPosition());
        gameModel.setStatus("Selected");
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        PlayerPiece toMove = (PlayerPiece) gameModel.getBoard().getPiece((SquareButton) e.getSource());
        Player.Direction headedDirection = getDirection(e);

        if(headedDirection == null){
            gameModel.warningMessage("Cannot identify a valid direction. Please try again");
            return;
        }

        gameModel.movePiece(toMove.getLetter(), headedDirection, true);
        gameModel.clearSelectedSquareInBoard();
        gameModel.setStatus("Moved");
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


    private Player.Direction getDirection(MouseEvent e){
        int width = ((SquareButton) e.getSource()).getWidth();
        int height = ((SquareButton) e.getSource()).getHeight();
        int mouseX = e.getX();
        int mouseY = e.getY();

        if(mouseX >= OFFSET && mouseX <= (width - OFFSET) && mouseY >= 0 && mouseY <= OFFSET){
            return Player.Direction.UP;
        }

        if(mouseX >= OFFSET && mouseX <= (width - OFFSET) && mouseY >= height - OFFSET && mouseY <= height){
            return Player.Direction.DOWN;
        }

        if(mouseX >= 0 && mouseX <= OFFSET && mouseY >= OFFSET && mouseY <= height - OFFSET){
            return Player.Direction.LEFT;
        }

        if(mouseX >= width - OFFSET && mouseX <= width && mouseY >= OFFSET && mouseY <= height - OFFSET){
            return Player.Direction.RIGHT;
        }

        return null;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
