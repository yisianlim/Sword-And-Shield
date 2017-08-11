package model.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.piece.EmptyPiece;
import model.piece.Piece;
import model.piece.PlayerPiece;

/**
 * Represents a hand of PlayerPieces held by a player. As the game progresses, the number of
 * PlayerPieces held by the player will decrease.
 */
public class Hand {

    private List<PlayerPiece> hand;

    /**
     * String representations for each lines for the hand.
     */
    private List<String> stringRepresentation;

    public Hand(List<PlayerPiece> pieces){
        this.hand = pieces;
        setStringRepresentation();
    }

    public void remove(PlayerPiece piece){
        hand.remove(piece);
    }

    public boolean contains(PlayerPiece piece){
        return hand.contains(piece);
    }

    /**
     * Get the PlayerPiece with the letter from the Player's hand.
     * Null if it is not present.
     * @param letter
     *          letter of the PlayerPiece we are looking for.
     * @return
     *          The PlayerPiece with the letter.
     */
    public PlayerPiece getPiece(String letter){
        for(PlayerPiece piece : hand){
            if(piece.getLetter().equals(letter)){
                return piece;
            }
        }
        return null;
    }

    private void setStringRepresentation(){
        this.stringRepresentation = new ArrayList<>();
        while(stringRepresentation.size() < 6){
            stringRepresentation.add("                      ");
        }

        stringRepresentation.add(" ###### WARRIORS #####");
        stringRepresentation.add(" # +---+---+---+---+ #");

        int ROWS = 6;
        int COLS = 4;
        Piece[][] pieces = new Piece[ROWS][COLS];
        int row = 0;
        int col = 0;

        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                pieces[i][j] = new EmptyPiece();
            }
        }

        hand.sort(Comparator.comparing(PlayerPiece::getLetter));

        for(int k = 0; k < hand.size(); k++){
            pieces[row][col++] = hand.get(k);
            if(col == COLS){
                col = 0;
                row++;
            }
        }

        String line1, line2, line3;

        for(row = 0; row < pieces.length; row++){
            line1 = " # |";
            line2 = " # |";
            line3 = " # |";
            for(col = 0; col < pieces[0].length; col++){
                Piece piece = pieces[row][col];
                line1 += piece.topLine()    + "|";
                line2 += piece.midLine()    + "|";
                line3 += piece.bottomLine() + "|";
            }
            line1 += " #";
            line2 +=" #";
            line3 += " #";
            stringRepresentation.add(line1);
            stringRepresentation.add(line2);
            stringRepresentation.add(line3);
            stringRepresentation.add(" # +---+---+---+---+ #");

        }
        stringRepresentation.add(" #####################");

        while(stringRepresentation.size() < 42){
            stringRepresentation.add("                      ");
        }
    }


    /**
     * Return the String representation of the player's hand in a 8 * 3 board.
     * @return
     */
    public String toString(){
        String output = "";
        for(String string : stringRepresentation){
            output+= string + "\n";
        }
        return output;
    }

    /**
     * Return the String representation of line num of player's hand.
     * @param num
     *          Line number to get String from.
     * @return
     *          String of the line number specified.
     */
    public String toLine(int num){
        setStringRepresentation();
        return stringRepresentation.get(num);
    }


    public Hand clone(){
        List<PlayerPiece> clone_list = new ArrayList<>();
        for(PlayerPiece p : hand){
            clone_list.add(p);
        }
        return new Hand(clone_list);
    }

}
