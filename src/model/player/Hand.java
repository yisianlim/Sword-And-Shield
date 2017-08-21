package model.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import gui.drawers.SquareButton;
import model.Position;
import model.piece.BlankPiece;
import model.piece.EmptyPiece;
import model.piece.Piece;
import model.piece.PlayerPiece;

/**
 * Represents a hand of PlayerPieces held by a player. As the game progresses, the number of
 * PlayerPieces held by the player will decrease.
 */
public class Hand {

    /**
     * PlayerPiece that the player owns.
     */
    private List<PlayerPiece> hand;

    /**
     * PlayerPiece that the user has selected to create.
     */
    private PlayerPiece selected;

    /**
     * String representations for each lines for the hand.
     */
    private List<String> stringRepresentation;

    public Hand(List<PlayerPiece> pieces){
        this.hand = pieces;
        updateStringRepresentation();
    }

    /**
     * Remove the piece from hand.
     * @param piece
     *          PlayerPiece to remove.
     */
    public void remove(PlayerPiece piece){
        hand.remove(piece);
    }

    /**
     * @param piece
     *          PlayerPiece to check
     * @return
     *          Returns true if the Player's hand have the following piece.
     */
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

    /**
     * Update the String representation of the Player's hand by each line.
     */
    private void updateStringRepresentation(){
        this.stringRepresentation = new ArrayList<>();
        addBlankLines(6);
        stringRepresentation.add(" ###### WARRIORS #####");
        stringRepresentation.add(" # +---+---+---+---+ #");

        int rows = 6;
        int cols = 4;
        Piece[][] pieces = new Piece[rows][cols];
        int row = 0;
        int col = 0;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                pieces[i][j] = new EmptyPiece();
            }
        }

        hand.sort(Comparator.comparing(PlayerPiece::getLetter));

        for(int k = 0; k < hand.size(); k++){
            pieces[row][col++] = hand.get(k);
            if(col == cols){
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
            line2 += " #";
            line3 += " #";
            stringRepresentation.add(line1);
            stringRepresentation.add(line2);
            stringRepresentation.add(line3);
            stringRepresentation.add(" # +---+---+---+---+ #");

        }
        stringRepresentation.add(" #####################");
        addBlankLines(42);
    }

    public void addBlankLines(int num){
        while(stringRepresentation.size() < num){
            stringRepresentation.add("                      ");
        }
    }

    /**
     * Return a 6*4 representation of PlayerPiece in the player's hand.
     * @return
     */
    public Piece[][] getArrayRepresentation(){

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

        hand.sort(Comparator.comparing(PlayerPiece::getLetter));

        for(int k = 0; k < hand.size(); k++){
            pieces[row][col++] = hand.get(k);
            if(col == cols){
                col = 0;
                row++;
            }
        }

        return pieces;
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
        updateStringRepresentation();
        return stringRepresentation.get(num);
    }

    /**
     * Returns a deep cloned version of the current Hand object.
     * @return
     *          Cloned Hand.
     */
    public Hand clone(){
        List<PlayerPiece> clone_list = new ArrayList<>();
        for(PlayerPiece p : hand){
            clone_list.add(p);
        }
        return new Hand(clone_list);
    }

    /**
     * The PlayerPiece selected by the user.
     * @param p
     */
    public void setSelected(PlayerPiece p){
        this.selected = p;
    }

    /**
     * Return a list of all the possible orientation of the selected piece by the user.
     * @return
     */
    public List<PlayerPiece> getSelectedInAllOrientations(){
        List<PlayerPiece> list = new ArrayList<>();
        PlayerPiece clone_90 = selected.clone();
        clone_90.rotate(90);

        PlayerPiece clone_180 = selected.clone();
        clone_180.rotate(180);

        PlayerPiece clone_270 = selected.clone();
        clone_270.rotate(270);

        list.add(selected.clone());
        list.add(clone_90);
        list.add(clone_180);
        list.add(clone_270);

        return list;
    }

    public PlayerPiece getSelectedPiece(Position pos){
        List<PlayerPiece> list = getSelectedInAllOrientations();
        return list.get(pos.getY());
    }

    public Piece getPiece(SquareButton squareButton) {
        Position position = squareButton.getPosition();
        Piece[][] array = getArrayRepresentation();
        return array[position.getX()][position.getY()];
    }
}
