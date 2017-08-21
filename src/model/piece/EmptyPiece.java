package model.piece;

import gui.drawers.SquareButton;
import model.Position;

import java.awt.*;

/**
 * This class represents an EmptyPiece in the Board. It remains empty until a PlayerPiece moves into it.
 */
public class EmptyPiece extends Piece {

    /**
     * Represents the empty piece inside the Board that determines the game.
     */
    public EmptyPiece(){
        super();
        m_rep = new String[][]{
                {" ", " ", " "},
                {" ", " ", " "},
                {" ", " ", " "}
        };
    }

    @Override
    public SquareButton createButton(Position position, SquareButton.Panel squareType) {
        SquareButton squareButton = new SquareButton(this, position, squareType);
        Color squareColor = ((position.getX()%2) == (position.getY()%2)) ? Color.BLACK : Color.WHITE;
        squareButton.setBackground(squareColor);
        squareButton.setOpaque(true);
        squareButton.setBorderPainted(false);
        return squareButton;
    }
}
