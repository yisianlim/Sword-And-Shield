package gui.square;

import model.Position;
import model.piece.*;

import javax.swing.*;

/**
 * Each square in the game is drawn as a JButton.
 */
public class SquareButton extends JButton {

    public enum SquareType{
        CREATION_SHELF_GREEN, CREATION_SHELF_YELLOW, TRAINING, BOARD, CEMETERY,
    };

    private Position position;
    private Piece piece;
    private SquareType squareType;

    public SquareButton(Piece piece, Position position, SquareType squareType){
        this.position = position;
        this.piece = piece;
        this.squareType = squareType;
    }

    public Position getPosition(){
        return position;
    }

    public SquareType getSquareType(){
        return squareType;
    }

}
