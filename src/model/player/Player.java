package model.player;

import model.Game;
import model.Position;
import model.piece.PlayerPiece;

import java.util.*;

public abstract class Player {

    public enum Direction{
        UP(1), DOWN(2), LEFT(3), RIGHT(4);

        private int value;

        Direction(int value){
            this.value = value;
        }
    }

    protected String playerName;

    public Hand hand;

    // PlayerPiece that are currently on the board.
    public Set<PlayerPiece> piecesInBoard;

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


    public Set<PlayerPiece> getAllPiecesInBoard(){
        Set<PlayerPiece> set = new HashSet<>();
        set.addAll(piecesInBoard);
        return set;
    }

    public void addToPiecesInBoard(PlayerPiece piece){
        piecesInBoard.add(piece);
    }

    public void removeFromPiecesInBoard(PlayerPiece piece){
        piecesInBoard.remove(piece);
    }

    public String getName(){
        return playerName;
    }

}
