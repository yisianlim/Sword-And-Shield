package tests;

import model.Board;
import model.Game;
import model.Position;
import model.piece.PlayerPiece;
import model.player.GreenPlayer;
import model.player.Player;
import model.player.YellowPlayer;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
        game.createPiece("L", 0);
        try {
            game.createPiece("B", 0);
            fail("Cannot create if CREATION_GRID is occupied.");
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
            fail("An exception have to be throw for attempt to create a piece the player don't have");
        } catch (IllegalArgumentException e){

        }
    }

    /**
     * Creating a Piece and making sure that the Player's state is updated according.
     * The player should not have that piece on their hand anymore.listener = parseFinalActionPhase(READER);
     */
    @Test
    public void test_CreatePiece(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        assertTrue(player.hand.getPiece("L") != null);
        game.createPiece("L", 0);
        assertTrue(player.hand.getPiece("L") == null);
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
        Player greenPlayer = new GreenPlayer(game);

        game.setCurrentPlayer(greenPlayer);
        game.createPiece("L", 0);
        PlayerPiece piece_L = board.findPiece("L");
        game.movePiece("L", UP, false);

        game.updateUnactedPieces();

        game.createPiece("A", 0);
        game.movePiece("A", UP, false);

        game.updateUnactedPieces();

        game.movePiece("A", UP, false);

        // Piece L should be in the cemetery by now.
        assertTrue(game.getCemetery().contains(piece_L));

        // Piece L should not be in the piecesInBoard for player.
        assertFalse(greenPlayer.getAllPiecesInBoard().contains(piece_L));
    }

    /**
     * Attempt to move a piece twice during a round should throw an exception.
     */
    @Test
    public void test_CannotMoveTwice(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.movePiece("L", UP, false);

        try{
            game.movePiece("L", UP, false);
            fail("An exception have to be thrown for attempt to move twice");
        } catch (IllegalArgumentException e){

        }
    }

    /**
     * Attempt to move a piece twice during a round should throw an exception.
     */
    @Test
    public void test_CannotRotateTwice(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.rotatePiece("L", 90);
        try{
            game.rotatePiece("L", 90);
            fail("An exception have to be throw for attempt to rotate twice");
        } catch (IllegalArgumentException e){

        }
    }

  /**
   * Test if undo move is successful.
   */
  @Test
    public void test_UndoMove1(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.movePiece("L", LEFT, false);
        PlayerPiece piece_l = game.getBoard().findPiece("L");

        assertTrue(piece_l.getPosition().getX() == 2);
        assertTrue(piece_l.getPosition().getY() == 1);

        game.undo();

        piece_l = game.getBoard().findPiece("L");
        assertTrue(piece_l.getPosition().getX() == 2);
        assertTrue(piece_l.getPosition().getY() == 2);
    }

    /**
     * Test if undo move twice is successful.
     */
    @Test
    public void test_UndoMove2(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.movePiece("L", LEFT, false);
        game.updateUnactedPieces();
        game.movePiece("L", LEFT, false);
        PlayerPiece piece_l = game.getBoard().findPiece("L");

        assertTrue(piece_l.getPosition().getX() == 2);
        assertTrue(piece_l.getPosition().getY() == 0);

        game.undo();
        game.undo();

        piece_l = game.getBoard().findPiece("L");
        assertTrue(piece_l.getPosition().getX() == 2);
        assertTrue(piece_l.getPosition().getY() == 2);
    }

  /**
   * Test if undo move that push a neighboring piece is successful.
   */
    @Test
    public void test_UndoMove3(){
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

        game.undo();
        game.undo();
        game.undo();

        // Piece L should now be back in CREATION GRID.
        posL = game.getBoard().findPiece("L").getPosition();
        assertTrue(posL.getX() == 2);
        assertTrue(posL.getY() == 2);
    }


    /**
     * Test if undo move that pushes a neighboring piece to the cemetery is successful.
     */
    @Test
    public void test_UndoMove4(){
        Board board = new Board();
        Game game = new Game(board);
        Player greenPlayer = new GreenPlayer(game);

        game.setCurrentPlayer(greenPlayer);
        game.createPiece("L", 0);
        PlayerPiece piece_L = board.findPiece("L");
        game.movePiece("L", UP, false);

        game.updateUnactedPieces();

        game.createPiece("A", 0);
        game.movePiece("A", UP, false);

        game.updateUnactedPieces();

        // Push piece L into the cemetery.
        game.movePiece("A", UP, false);

        // Piece L should be in the cemetery by now.
        assertTrue(game.getCemetery().contains(piece_L));

        // Piece L should not be in the piecesInBoard for player.
        assertFalse(greenPlayer.getAllPiecesInBoard().contains(piece_L));

        game.undo();

        // Piece L should not be in the cemetery after undo.
        assertFalse(game.getCemetery().contains(piece_L));

        // Piece L should be back in the piecesInBoard for player.
        assertTrue(greenPlayer.getAllPiecesInBoard().contains(piece_L));

        // Piece L should be back in the unacted pieces.
        assertTrue(game.getUnactedPieces().contains(piece_L));
    }

    @Test
    public void test_UndoRotation(){
        String before =
                " | \n" +
                "-L-\n" +
                " # \n";

        String after =
                " | \n" +
                "-L#\n" +
                " | \n";

        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.rotatePiece("L", 90);

        PlayerPiece piece_L = game.getBoard().findPiece("L");
        assertEquals(after, piece_L.toString());

        game.undo();

        piece_L = game.getBoard().findPiece("L");
        assertEquals(before, piece_L.toString());
    }

}
