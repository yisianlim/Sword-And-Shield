package gui.drawers;

import gui.views.GameView;
import gui.views.PrimaryView;
import model.Board;
import model.Game;
import model.Position;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;

/**
 * BoardDrawer handles all the logic of drawing the board (both display and game over board).
 */
public class BoardDrawer extends JPanel {

    /**
     * Model of the GUI.
     */
    private Game gameModel;

    /**
     * Dimension of the board.
     */
    private int width;
    private int height;

    /**
     * View that we draw this board on.
     */
    private GameView gameView;

    public BoardDrawer(Game gameModel, GameView gameView){
        this.gameModel = gameModel;
        this.gameView = gameView;

        // Calculate the dimension of the board based on the size of the PrimaryView panel.
        this.width = (int) (PrimaryView.BOARD_WIDTH_RATIO * PrimaryView.getPrimaryViewWidth());
        this.height = (int) (PrimaryView.BOARD_HEIGHT_RATIO * PrimaryView.getPrimaryViewHeight());
    }

    /**
     * Read the board of the game and render the all the graphical assets of the board
     * onto the JLayeredPane.
     * @return
     *      Rendered JLayeredPane based on the gameModel's board.
     */
    public JComponent createBoard(){

        setPreferredSize(new Dimension(width, height));
        setBounds(0,0, width, height);

        setLayout(new GridLayout(10,10));
        Board gameBoard = gameModel.getBoard();

        // Create a custom, responsive SquareButton for each Piece in gameBoard.
        // Add the SquareButton into JPanel createBoard.
        for(int row = 0; row < gameBoard.ROWS; row++){
            for(int col = 0; col < gameBoard.COLS; col++){
                Position currentPosition = new Position(row, col);
                SquareButton squareButton = new SquareButtonDrawer(
                        gameBoard.getSquare(currentPosition),
                        currentPosition,
                        SquareButton.Panel.BOARD_DISPLAY)
                        .makeButton();

                // If the PlayerPiece is selected, then we flagSelected the piece
                // and bind the appropriate listeners from boardController.
                if(gameBoard.selectedSquare(squareButton.getPosition())){
                    squareButton.setPanelType(SquareButton.Panel.BOARD_SELECTED);
                    squareButton.addMouseListener(gameView.boardController);
                    gameView.boardController.bindWASDKey(squareButton);
                    squareButton.flagSelected();
                }

                // If the PlayerPiece has been moved, then we set unavailable for action.
                if(gameModel.movedPiece(squareButton.getPosition())){
                    squareButton.flagMoved();
                }

                // Bind all Pieces in the board with an ActionListener.
                squareButton.addActionListener(gameView.boardController);

                // Finally, add the SquareButton assets into the JPanel.
                add(squareButton);
            }
        }

        if(gameModel.gameOver()){
            // If the game is over, we decorate the board with a grey layer.
            BoardLayerUI boardLayerUI = new BoardLayerUI(gameModel);
            JLayer<JPanel> jPanelJLayer = new JLayer<>(this, boardLayerUI);

            // Animation of the winning FacePiece being less greyed out over a period of time.
            new Timer(100,
                    e -> {
                        boardLayerUI.increaseDist();
                        repaint();
                    }
            ).start();

            return jPanelJLayer;
        } else {
            // Otherwise, we just return the board itself.
            return this;
        }
    }
}
