package model;

import model.piece.*;

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

    public boolean isCemetery(Position pos){
        return pos.getX() < 0 || pos.getX() >= 10 && pos.getY() < 0 || pos.getY() >= 10;
    }

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
     * Construct the string representation of the board.
     * @return
     */
    public String toString(){
        String output = "     0   1   2   3   4   5   6   7   8   9  \n" + drawBorder();
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
            line1 += "\n";
            line2 +="\n";
            line3 += "\n";
            output += line1 + line2 + line3 + drawBorder();
        }
        return output;
    }

    /**
     * Construct a border line for the board.
     * @return
     */
    public String drawBorder(){
        return "   +---+---+---+---+---+---+---+---+---+---+\n";
    }

    public void draw() {
        System.out.println(toString());
    }

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
