package tests;

import model.Board;
import model.Game;
import model.Position;
import model.player.GreenPlayer;
import model.player.Player;
import org.junit.Test;

import static model.player.Player.Direction.*;
import static org.junit.Assert.*;

public class GameTest {
    /**
     * Creating a Piece in the CREATION_GRID twice should throw an exception.
     */
    @Test
    public void test_InvalidCreation(){
        Board board = new Board();
        Game game = new Game(board);
        game.setCurrentPlayer(new GreenPlayer(game));
        try {
            game.createPiece("L", 0);
            game.createPiece("B", 0);
        } catch (IllegalArgumentException e){

        }
    }

    /**
     * Creating a Piece that the player do not own should throw an exception.
     */
    @Test
    public void test_NoSuchPiece(){
        Board board = new Board();
        Game game = new Game(board);
        game.setCurrentPlayer(new GreenPlayer(game));
        try {
            game.createPiece("l", 0);
        } catch (IllegalArgumentException e){

        }
    }

    /**
     * Creating a Piece and making sure that the Player's state is updated according.
     * The player should not have that piece on their hand anymore.
     */
    @Test
    public void test_CreatePiece(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        assertTrue(player.getPieceFromHand("L") != null);
        game.createPiece("L", 0);
        assertTrue(player.getPieceFromHand("L") == null);
    }

    /**
     * Attempt to move a piece into a FacePiece should throw an exception.
     */
    @Test
    public void test_InvalidMovePiece(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.movePiece("L", UP);

        try{
            game.movePiece("L", LEFT);
        } catch (IllegalArgumentException e){

        }
    }

    /**
     * Attempt to move a piece should have its position successfully modified.
     */
    @Test
    public void test_ValidMovePiece(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.movePiece("L", UP);

        Position pos = game.getBoard().findPiece("L").getPosition();
        assertTrue(pos.getX() == 1);
        assertTrue(pos.getY() == 2);
    }
}
