package model;

import model.piece.EmptyPiece;
import model.piece.Piece;
import model.piece.PlayerPiece;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a the PlayerPiece that are in the cemetery. As the game progresses, the number of PlayerPiece should
 * increase.
 */
public class Cemetery {

    /**
     * PlayerPiece that have landed in the cemetery.
     */
    public List<PlayerPiece> cemetery;

    public Cemetery(){
        cemetery = new ArrayList<>();
    }

    /**
     * Add piece to cemetery.
     * @param piece
     *          Piece to add.
     */
    public void add(PlayerPiece piece){
        cemetery.add(piece);
    }

    /**
     * Draw the cemetery.
     */
    public void draw(){
        System.out.println(toString());
        System.out.println();
    }

    /**
     * Check if the cemetery contains the following PlayerPiece.
     * @param piece
     *          PlayerPiece to check against.
     * @return
     *          True if it is in the cemetery. False otherwise.
     */
    public boolean contains(PlayerPiece piece){
        return cemetery.contains(piece);
    }

    /**
     * Return the String representation of the cemetery in a 3 * 16 board.
     * @return
     */
    public String toString(){
        int ROWS = 3;
        int COLS = 16;
        Piece[][] pieces = new Piece[ROWS][COLS];
        int row = 0;
        int col = 0;

        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                pieces[i][j] = new EmptyPiece();
            }
        }

        cemetery.sort(Comparator.comparing(PlayerPiece::getLetter));

        for(int k = 0; k < cemetery.size(); k++){
            pieces[row][col++] = cemetery.get(k);
            if(col == COLS){
                col = 0;
                row++;
            }
        }
        String output = "";
        output += indent() + "---------------------------REST IN PEACE-----------------------------\n";
        output += indent() + "XXXXXXXXXXXXXXXXXXXXXXXXXXXX CEMETERY XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n";
        output +=  border();

        String line1, line2, line3;
        for(row = 0; row < pieces.length; row++){
            line1 = indent() + "X |";
            line2 = indent() + "X |";
            line3 = indent() + "X |";
            for(col = 0; col < pieces[0].length; col++){
                Piece piece = pieces[row][col];
                line1 += piece.topLine()    + "|";
                line2 += piece.midLine()    + "|";
                line3 += piece.bottomLine() + "|";
            }
            line1 += " X\n";
            line2 += " X\n";
            line3 += " X\n";
            output += line1 + line2 + line3 + border();
        }
        output += indent() + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        return output;
    }

    public String border(){
        return indent() + "X +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+ X\n";
    }

    public String indent(){
        return "            ";
    }

    /**
     * Deep clone the Cemetery object.
     * @return
     *      Cloned Cemetery.
     */
    public Cemetery clone(){
        Cemetery clone = new Cemetery();
        for(PlayerPiece piece : cemetery){
            clone.cemetery.add(piece);
        }
        return clone;
    }

}
