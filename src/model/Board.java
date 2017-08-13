package model;

import model.piece.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 10x10 board in the Game. Each square on the board can either be empty, or hold Piece. The board
 * is simply responsible for storing this information.
 */
public class Board {
    /**
     * The Game board is a 2-dimensional array
     */
    private Piece[][] board;

    /**
     * String representation of the Board in lines.
     */
    private List<String> stringRepresentation;

    /**
     * Create an empty and initialized game board.
     */
    public Board(){
        this.board = new Piece[10][10];

        // Initialise EmptyPiece(s) in the board.
        // EmptyPiece can be replaced be PlayerPiece throughout the game.
        for(int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                setSquare(new Position(x,y), new EmptyPiece());
            }
        }

        // Initialise the BlankPiece on specific Position on the board.
        // The square must always remain a BlankPiece.
        setSquare(new Position(0,0), new BlankPiece());
        setSquare(new Position(0,1), new BlankPiece());
        setSquare(new Position(1,0), new BlankPiece());
        setSquare(new Position(9,9), new BlankPiece());
        setSquare(new Position(8,9), new BlankPiece());
        setSquare(new Position(9,8), new BlankPiece());

        // Initialise the Player's FacePiece on the board.
        // The square must always remain a FacePiece.
        setSquare(new Position(1,1), new FacePiece("1"));
        setSquare(new Position(8,8), new FacePiece("0"));

        updateStringRepresentation();
    }

    /**
     * Get the Piece at a given x and y position, or null if the square is empty.
     * @param pos
     *          Position of square to get
     * @return
     */
    public Piece getSquare(Position pos){
        return board[pos.getX()][pos.getY()];
    }

    /**
     * Check if the Position is out of the board.
     * @param pos
     *          Position to check for.
     * @return
     *      True if it is out of the board. False otherwise.
     */
    public boolean outOfBoard(Position pos){
        return pos.getX() < 0 ||
                pos.getX() >= 10 ||
                pos.getY() < 0 ||
                pos.getY() >= 10 ||
                getSquare(pos) instanceof FacePiece ||
                getSquare(pos) instanceof BlankPiece;
    }

    /**
     * Find the following PlayerPiece with the letter in the board.
     * @param letter
     *          Letter of the PlayerPiece to look for.
     * @return
     *          Corresponding PlayerPiece if found. Null if it is not represent.
     */
    public PlayerPiece findPiece(String letter){
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[0].length; y++){
                if(board[x][y] instanceof PlayerPiece){
                    PlayerPiece p = (PlayerPiece) board[x][y];
                    if(p.getLetter().equals(letter))
                        return p;
                }
            }
        }
        return null;
    }

    /**
     * Set the Piece at a given x and y position or null (to set the square as empty).
     * @param pos
     *          Position of square to set
     * @param piece
     *          Piece to place at that position.
     */
    public void setSquare(Position pos, Piece piece){
        board[pos.getX()][pos.getY()] = piece;
    }

    /**
     * Update the String representation of the board line by line.
     */
    private void updateStringRepresentation(){
        this.stringRepresentation = new ArrayList<>();
        stringRepresentation.add("     0   1   2   3   4   5   6   7   8   9  ");
        stringRepresentation.add(border());

        String line1, line2, line3;

        for(int row = 0; row < board.length; row++){
            line1 = "   |";
            line2 = " " + row + " |";
            line3 = "   |";
            for(int col = 0; col < board[0].length; col++){
                Piece piece = board[row][col];
                line1 += piece.topLine()    + "|";
                line2 += piece.midLine()    + "|";
                line3 += piece.bottomLine() + "|";
            }
            stringRepresentation.add(line1);
            stringRepresentation.add(line2);
            stringRepresentation.add(line3);
            stringRepresentation.add(border());
        }
    }

    /**
     * Construct the string representation of the board.
     * @return
     */
    public String toString(){
        updateStringRepresentation();
        String output = "";
        for(String string : stringRepresentation){
            output+= string + "\n";
        }
        return output;
    }

    /**
     * Return the String representation of line num of board.
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
     * Construct a border line for the board.
     * @return
     */
    public String border(){
        return "   +---+---+---+---+---+---+---+---+---+---+";
    }

    /**
     * Deep clone the current Board object.
     * @return
     *      Cloned Board.
     */
    public Board clone(){
        Board clone = new Board();

        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[0].length; col++){
                clone.board[row][col] = board[row][col].clone();
            }
        }
        return clone;
    }
}
