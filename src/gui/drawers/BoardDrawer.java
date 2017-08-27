package gui.drawers;

import com.sun.glass.events.KeyEvent;
import gui.views.GameView;
import gui.views.PrimaryView;
import model.Board;
import model.Game;
import model.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by limyisi on 23/08/17.
 */
public class BoardDrawer extends JPanel {

    private Game gameModel;

    private GameView gameView;

    public BoardDrawer(Game gameModel, GameView gameView){
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    public JPanel createBoard(){
        int boardWidth = (int) (PrimaryView.BOARD_WIDTH_RATIO * PrimaryView.getPrimaryViewWidth());
        int boardHeight = (int) (PrimaryView.BOARD_HEIGHT_RATIO * PrimaryView.getPrimaryViewHeight());
        setPreferredSize(new Dimension(boardWidth,boardHeight));

        setLayout(new GridLayout(10,10));
        Board gameBoard = gameModel.getBoard();

        // Create a custom, responsive SquareButton for each Piece in gameBoard.
        // Add the SquareButton into JPanel createBoard.
        for(int row = 0; row < gameBoard.ROWS; row++){
            for(int col = 0; col < gameBoard.COLS; col++){
                Position currentPosition = new Position(row, col);
                SquareButton squareButton = new ButtonDrawer(
                        gameBoard.getSquare(currentPosition),
                        currentPosition,
                        SquareButton.Panel.BOARD_DISPLAY)
                        .makeButton();

                // If the PlayerPiece is selected, then we highlight the piece.
                if(gameBoard.selectedSquare(squareButton.getPosition())){
                    squareButton.setPanelType(SquareButton.Panel.BOARD_SELECTED);
                    squareButton.addMouseListener(gameView.boardController);
                    gameView.boardController.bindWASDKey(squareButton);
                    squareButton.highlight();
                }

                // If the PlayerPiece has been moved, then we set unavailable for action.
                if(gameModel.movedPiece(squareButton.getPosition())){
                    squareButton.flagUnavailable();
                }

                squareButton.addActionListener(gameView.boardController);

                add(squareButton);
            }
        }
        return this;
    }
}
