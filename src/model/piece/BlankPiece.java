package model.piece;

import gui.square.SquareButton;
import model.Position;

import java.awt.*;

/**
 * Represents the empty piece inside the Board that should never have any piece placed.
 * Anytime a PlayerPiece moves into the BlankPiece, it ends up in the cemetery.
 */
public class BlankPiece extends Piece {
    public BlankPiece(){
        super();
        m_rep = new String[][]{
                {"X", "X", "X"},
                {"X", "X", "X"},
                {"X", "X", "X"}
        };
    }

    @Override
    public SquareButton createButton(Position position, SquareButton.SquareType squareType) {
        SquareButton squareButton = new SquareButton(this, position, squareType);
        squareButton.setBackground(Color.GRAY);
        squareButton.setOpaque(true);
        squareButton.setBorderPainted(false);
        return squareButton;
    }
}
