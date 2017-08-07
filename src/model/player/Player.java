package model.player;

import model.Game;
import model.Position;
import model.piece.EmptyPiece;
import model.piece.Piece;
import model.piece.PlayerPiece;

import java.util.*;

public abstract class Player {

    public enum Direction{
        UP(1), DOWN(2), LEFT(3), RIGHT(4);

        private int value;

        Direction(int value){
            this.value = value;
        }

        Direction opposite(){
            switch (value){
                case 1:
                    return DOWN;
                case 2:
                    return UP;
                case 3:
                    return RIGHT;
                case 4:
                    return LEFT;
            }
            return null;
        }
    }

    protected String m_playerName;

    // PlayerPiece that are on hand to be created.
    protected List<PlayerPiece> m_hand;

    // PlayerPiece that are currently on the board.
    protected Set<PlayerPiece> m_pieces_in_board;

    protected Game m_game;

    public Player(String playerName, Game game){
        m_playerName = playerName;
        m_hand = new ArrayList<>();
        m_pieces_in_board = new HashSet<>();
        m_game = game;
    }

    /**
     * Return true if the CREATION_GRID is empty. False otherwise.
     * @return
     *      TRUE if the player can create a PlayerPiece.
     */
    public abstract boolean validCreation();

    public abstract Position getCreationGrid();

    /**
     * Return true if piece belongs to player. False otherwise.
     * @param piece
     *          PlayerPiece piece to check against.
     * @return
     */
    public boolean validPiece(PlayerPiece piece){
        return m_hand.contains(piece) || m_pieces_in_board.contains(piece);
    }

    /**
     * Get the PlayerPiece with the letter from the Player's hand.
     * Null if it is not present.
     * @param letter
     *          letter of the PlayerPiece we are looking for.
     * @return
     *          The PlayerPiece with the letter.
     */
    public PlayerPiece getPieceFromHand(String letter){
        for(PlayerPiece piece : m_hand){
            if(piece.getLetter().equals(letter)){
                return piece;
            }
        }
        return null;
    }

    /**
     * Get the PlayerPiece with the letter in the board.
     * Null if it is not present.
     * @param letter
     *          letter of the PlayerPiece we are looking for.
     * @return
     *          The PlayerPiece with the letter.
     */
    public PlayerPiece getPieceFromPiecesInBoard(String letter){
        for(PlayerPiece piece : m_pieces_in_board){
            if(piece.getLetter().equals(letter)){
                return piece;
            }
        }
        return null;
    }

    public Set<PlayerPiece> getAllPiecesInBoard(){
        Set<PlayerPiece> set = new HashSet<>();
        for(PlayerPiece p : m_pieces_in_board){
            set.add(p);
        }
        return set;
    }

    public void removeFromHand(PlayerPiece piece){
        m_hand.remove(piece);
    }

    public void addToPiecesInBoard(PlayerPiece piece){
        m_pieces_in_board.add(piece);
    }

    public void drawHand() {
        System.out.println("******** " + m_playerName + "'s piece to create ********");
        System.out.println(handToString());
        System.out.println();
    }

    /**
     * Return the String representation of the player's hand in a 8 * 3 board.
     * @return
     */
    public String handToString(){
        int ROWS = 3;
        int COLS = 8;
        Piece[][] pieces = new Piece[ROWS][COLS];
        int row = 0;
        int col = 0;

        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                pieces[i][j] = new EmptyPiece();
            }
        }

        m_hand.sort(Comparator.comparing(PlayerPiece::getLetter));

        for(int k = 0; k < m_hand.size(); k++){
            pieces[row][col++] = m_hand.get(k);
            if(col == COLS){
                col = 0;
                row++;
            }
        }

        String output =  "   +---+---+---+---+---+---+---+---+\n";
        String line1, line2, line3;

        for(row = 0; row < pieces.length; row++){
            line1 = "   |";
            line2 = "   |";
            line3 = "   |";
            for(col = 0; col < pieces[0].length; col++){
                Piece piece = pieces[row][col];
                line1 += piece.topLine()    + "|";
                line2 += piece.midLine()    + "|";
                line3 += piece.bottomLine() + "|";
            }
            line1 += "\n";
            line2 +="\n";
            line3 += "\n";
            output += line1 + line2 + line3 + "   +---+---+---+---+---+---+---+---+\n";
        }
        return output;
    }

    public String getName(){
        return m_playerName;
    }

}
