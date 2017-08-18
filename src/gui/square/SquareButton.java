package gui.square;

import model.Position;
import model.piece.BlankPiece;
import model.piece.EmptyPiece;
import model.piece.FacePiece;
import model.piece.Piece;

import javax.swing.*;
import java.awt.*;

/**
 * Each square in h
 */
public class SquareButton extends JButton {

    private Position position;

    public SquareButton(Piece piece, Position position){
        this.position = position;
        Color squareColor = Color.GRAY;
        if(piece instanceof FacePiece){
            FacePiece fp = (FacePiece) piece;
            squareColor = fp.greenPlayer() ? Color.green : Color.yellow;
        } else if (piece instanceof BlankPiece){
            squareColor = Color.GRAY;
        } else if (piece instanceof EmptyPiece){
            squareColor = ((position.getX()%2) == (position.getY()%2)) ? Color.black : Color.WHITE;
        }

        this.setBackground(squareColor);
        this.setOpaque(true);
        this.setBorderPainted(false);
    }

    public Position getPosition(){
        return position;
    }
}
