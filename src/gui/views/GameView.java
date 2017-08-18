package gui.views;

import gui.Controller;
import gui.square.SquareButton;
import javafx.geometry.Pos;
import model.Board;
import model.Game;
import model.Position;

import javax.swing.*;
import java.awt.*;

/**
 * GameView renders the state of the game to the user.
 */
public class GameView extends JPanel {

    Controller controller;
    Game gameModel;

    JToolBar toolBar;
    JPanel board;

    public GameView(Controller c, Game g){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Render components in each rows.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make buttons have the same size.
        this.controller = c;
        this.gameModel = g;
        drawBoard();
        drawToolbar();
        add(toolBar, gbc);
        add(board, gbc);
    }

    public void drawBoard(){
        board = new JPanel();
        board.setPreferredSize(new Dimension(600,600));
        board.setLayout(new GridLayout(10,10));
        Board gameBoard = gameModel.getBoard();
        for(int row = 0; row < gameBoard.ROWS; row++){
            for(int col = 0; col < gameBoard.COLS; col++){
                Position currentPosition = new Position(row, col);
                JButton squareButton = new SquareButton(gameBoard.getSquare(currentPosition), currentPosition);
                squareButton.addActionListener(controller);
                board.add(squareButton);
            }
        }
    }

    public void drawToolbar(){
        toolBar = new JToolBar();
        JButton pass = new JButton("Pass");
        JButton undo = new JButton("Undo");
        JButton surrender = new JButton("Surrender");
        toolBar.add(pass);
        toolBar.add(undo);
        toolBar.add(surrender);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000,1000);
    }

//    enum Type{
//        Board, Creation
//    };
//
//    public class Square extends JButton{
//        private int x, y;
//        private Type type;
//
//        public Square(int x, int y, Type type){
//            this.x = x;
//            this.y = y;
//            this.type = type;
//
//            Color squareColor = ((x%2) == (y%2)) ? Color.black : Color.WHITE;
//            if(outOfBoard()) squareColor = Color.GRAY;
//
//            this.setBackground(squareColor);
//            this.setOpaque(true);
//            this.setBorderPainted(false);
//            this.addActionListener(controller);
//        }
//
//        public Position getPosition(){
//            return new Position(x, y);
//        }
//
//        public Type getType(){
//            return type;
//        }
//
//        public boolean outOfBoard(){
//            return
//                    (x==0 && y==0) ||
//                    (x==0 && y==1) ||
//                    (x==1 && y==0) ||
//                    (x==9 && y==9) ||
//                    (x==8 && y==9) ||
//                    (x==9 && y==8);
//
//        }
//    }
}
