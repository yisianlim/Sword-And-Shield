package gui.square;

import model.Position;
import model.piece.Piece;
import model.piece.PlayerPiece;

public class ButtonMaker {

    private Piece piece;
    private Position position;
    private SquareButton.Panel squareType;

    public ButtonMaker(Piece piece, Position position, SquareButton.Panel squareType){
        this.piece = piece;
        this.position = position;
        this.squareType = squareType;
    }

    public ButtonMaker(PlayerPiece piece, Position position, SquareButton.Panel squareType){
        this.piece = piece;
        this.position = position;
        this.squareType = squareType;
    }

    public SquareButton makeButton(){
        return piece.createButton(position, squareType);
    }

}
