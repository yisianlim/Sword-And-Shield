package tests;

import model.Board;
import model.Game;
import model.Position;
import model.piece.PlayerPiece;
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
        game.movePiece("L", UP, false);

        try{
            game.movePiece("L", LEFT, false);
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
        game.movePiece("L", UP, false);

        Position pos = game.getBoard().findPiece("L").getPosition();
        assertTrue(pos.getX() == 1);
        assertTrue(pos.getY() == 2);
    }

    /**
     * Attempt to move a piece with a neighbor should have both its own and neighbor's position modified.
     */
    @Test
    public void test_NeighborMovePiece(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.movePiece("L", DOWN, false);

        Position posL = game.getBoard().findPiece("L").getPosition();
        assertTrue(posL.getX() == 3);
        assertTrue(posL.getY() == 2);

        // Attempt to move piece A should also move piece L down.
        game.createPiece("A", 0);
        game.movePiece("A", DOWN, false);

        posL = game.getBoard().findPiece("L").getPosition();
        assertTrue(posL.getX() == 4);
        assertTrue(posL.getY() == 2);

    }

    /**
     * Attempt to move a neighbor piece out of the board should result in the piece being the cemetery.
     */
    @Test
    public void test_PushToCemetery(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        PlayerPiece piece_L = board.findPiece("L");
        game.movePiece("L", UP, false);

        game.createPiece("A", 0);
        game.movePiece("A", UP, false);
        game.movePiece("A", UP, false);

        // Piece L should be in the cemetery by now.
        assertTrue(game.getCemetery().contains(piece_L));

    }
}
