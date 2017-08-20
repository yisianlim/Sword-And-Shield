package model.player;

import model.Game;
import model.Position;
import model.piece.PlayerPiece;

import java.util.*;

public abstract class Player {

    /**
     * Directions that the player can move.
     */
    public enum Direction{
        UP(1), DOWN(2), LEFT(3), RIGHT(4);

        private int value;

        Direction(int value){
            this.value = value;
        }

        public Direction opposite(){
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

    /**
     * Green or yellow player.
     */
    protected String playerName;

    /**
     * Represents the PlayerPiece that player has on hand.
     */
    public Hand hand;

    /**
     * Player's PlayerPiece that are currently on the board.
     */
    public Set<PlayerPiece> piecesInBoard;

    /**
     * Game that the Player is currently dealing with.
     */
    protected Game game;

    public Player(String playerName, Game game){
        this.playerName = playerName;
        this.piecesInBoard = new HashSet<>();
        this.game = game;
    }

    /**
     * Return true if the CREATION_GRID is empty. False otherwise.
     * @return
     *      TRUE if the player can create a PlayerPiece.
     */
    public abstract boolean validCreation();

    /**
     * @return
     *      Position of the respective Player's creation grid.
     */
    public abstract Position getCreationGrid();

    /**
     * Return true if piece belongs to player. False otherwise.
     * @param piece
     *          PlayerPiece piece to check against.
     * @return
     */
    public boolean validPiece(PlayerPiece piece){
        return hand.contains(piece)|| piecesInBoard.contains(piece);
    }

    /**
     * Returns all Player's PlayerPiece that are currently on the board.
     * @return
     */
    public Set<PlayerPiece> getAllPiecesInBoard(){
        Set<PlayerPiece> set = new HashSet<>();
        set.addAll(piecesInBoard);
        return set;
    }

    /**
     * Update the piecesInBoard if the Player's PlayerPiece goes onto the board.
     * @param piece
     *          Piece being added to board.
     */
    public void addToPiecesInBoard(PlayerPiece piece){
        piecesInBoard.add(piece);
    }

    /**
     * Update the piecesInBoard if the Player's PlayerPiece leaves the board.
     * @param piece
     *          Piece being removed from board.
     */
    public void removeFromPiecesInBoard(PlayerPiece piece){
        piecesInBoard.remove(piece);
    }

    /**
     * @return
     *      Name of the player. (Green or yellow)
     */
    public String getName(){
        return playerName;
    }

    public boolean isGreen(){
        return playerName.equals("Green");
    }

    public boolean isYellow(){
        return playerName.equals("Yellow");
    }

}
