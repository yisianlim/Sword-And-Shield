package model;

import model.piece.BlankPiece;
import model.piece.EmptyPiece;
import model.piece.Piece;
import model.piece.PlayerPiece;
import model.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public Piece[][] getPlayerPiecesInCemetery(Player player){
        if(player.isGreen())
            return getGreenPiecesInCemetery();
        else
            return getYellowPiecesInCemetery();
    }

    /**
     * Return a Piece[][] with just Green PlayerPiece in it.
     * @return
     */
    public Piece[][] getGreenPiecesInCemetery(){
        int rows = 4;
        int cols = 6;
        Piece[][] pieces = new Piece[rows][cols];
        int row = 0;
        int col = 0;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                pieces[i][j] = new BlankPiece();
            }
        }

        List<PlayerPiece> greenCemetery = cemetery.stream()
                .filter(piece -> piece.greenPlayer())
                .collect(Collectors.toList());

        for(int k = 0; k < greenCemetery.size(); k++){
            pieces[row][col++] = greenCemetery.get(k);
            if(col == cols){
                col = 0;
                row++;
            }
        }

        return pieces;
    }

    /**
     * Return a Piece[][] with just Yellow PlayerPiece in it.
     * @return
     */
    public Piece[][] getYellowPiecesInCemetery(){
        int rows = 4;
        int cols = 6;
        Piece[][] pieces = new Piece[rows][cols];
        int row = 0;
        int col = 0;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                pieces[i][j] = new BlankPiece();
            }
        }

        List<PlayerPiece> yellowCemetery = cemetery.stream()
                .filter(piece -> piece.yellowPlayer())
                .collect(Collectors.toList());

        for(int k = 0; k < yellowCemetery.size(); k++){
            pieces[row][col++] = yellowCemetery.get(k);
            if(col == cols){
                col = 0;
                row++;
            }
        }

        return pieces;
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
