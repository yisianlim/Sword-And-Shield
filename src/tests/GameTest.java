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
        game.createPiece("L", 0);
        try {
            game.createPiece("B", 0);
            fail("Cannot create if CREATION_GRID if occupied.");
        } catch (IllegalArgumentException e){

        }
    }

    /**
     * Creating a PlayerPiece that the player do not own should throw an exception.
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
     * The player should not have that piece on their hand anymore.
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
     * Attempt to move a piece should have its position successfully modified.
     */
    @Test
    public void test_ValidMovePiece(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.movePiece("L", UP, true);

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
        game.movePiece("L", DOWN, true);

        Position posL = game.getBoard().findPiece("L").getPosition();
        assertTrue(posL.getX() == 3);
        assertTrue(posL.getY() == 2);

        // Attempt to move piece A should also move piece L down
        // Piece L will move down once again due to sword shield reaction.
        game.createPiece("A", 0);
        game.movePiece("A", DOWN, true);

        posL = game.getBoard().findPiece("L").getPosition();
        assertTrue(posL.getX() == 5);
        assertTrue(posL.getY() == 2);

    }

    /**
     * Attempt to move a neighbor piece out of the createBoard should result in the piece being the cemetery.
     */
    @Test
    public void test_PushToCemetery(){
        Board board = new Board();
        Game game = new Game(board);
        Player greenPlayer = new GreenPlayer(game);

        game.setCurrentPlayer(greenPlayer);
        game.createPiece("L", 0);
        PlayerPiece piece_L = board.findPiece("L");
        game.movePiece("L", UP, true);

        game.resetFuture();

        game.createPiece("A", 0);
        game.movePiece("A", UP, true);

        game.resetFuture();

        game.movePiece("A", UP, true);

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
        game.movePiece("L", UP, true);

        try{
            game.movePiece("L", UP, true);
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
     * Attempt to undo when there are no more moves to undo should thrown an exception.
     */
    @Test
    public void test_UndoMove_Invalid(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.createPiece("L", 0);
        game.undo();
        try{
            game.undo();
            fail("An exception have to be thrown for attempt to undo when there are no commands left");
        } catch (IllegalArgumentException e){

        }
    }

    /**
     * Pass during CREATION phase should move on the FINAL phase when there are no
     * pieces belonging to the player on the createBoard.
     */
    @Test
    public void test_Pass_ChangePhase(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.pass();
        assertTrue(game.getGamePhase().equals(Game.Phase.FINAL));
    }

    /**
     * Undo Pass of CREATION phase should go back to CREATION phase.
     */
    @Test
    public void test_Pass_ChangePhase_Undo(){
        Board board = new Board();
        Game game = new Game(board);
        Player player = new GreenPlayer(game);
        game.setCurrentPlayer(player);
        game.pass();
        game.undo();
        assertTrue(game.getGamePhase().equals(Game.Phase.CREATE));

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
        game.movePiece("L", LEFT, true);
        game.resetFuture();
        game.movePiece("L", LEFT, true);
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
        game.movePiece("L", DOWN, true);

        Position posL = game.getBoard().findPiece("L").getPosition();
        assertTrue(posL.getX() == 3);
        assertTrue(posL.getY() == 2);

        game.createPiece("A", 0);
        game.movePiece("A", DOWN, true);

        posL = game.getBoard().findPiece("L").getPosition();
        assertTrue(posL.getX() == 5);
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
        game.movePiece("L", UP, true);

        game.resetFuture();

        game.createPiece("A", 0);
        game.movePiece("A", UP, true);

        game.resetFuture();

        // Push piece L into the cemetery.
        game.movePiece("A", UP, true);

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
        assertTrue(game.getFuture().contains(piece_L));
    }

    /**
     * Test if undo rotation is successful.
     */
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

    /**
     * Test if sword and shield reaction after create is successful.
     */
    @Test
    public void testReaction_Create_Sword_Shield(){
        Board board = new Board();
        Game game = new Game(board);
        Player greenPlayer = new GreenPlayer(game);

        game.setCurrentPlayer(greenPlayer);
        game.createPiece("L", 0);
        game.movePiece("L", DOWN, true);
        game.resetFuture();
        game.createPiece("A", 0);

        PlayerPiece piece_L = board.findPiece("L");
        assertTrue(piece_L.getPosition().getX() == 4); // L should be pushed by by shield.
        assertTrue(piece_L.getPosition().getY() == 2);
    }

    /**
     * Test if undo sword and shield reaction after create is successful.
     */
    @Test
    public void testReaction_Create_Sword_Shield_Undo(){
        Board board = new Board();
        Game game = new Game(board);
        Player greenPlayer = new GreenPlayer(game);

        game.setCurrentPlayer(greenPlayer);
        game.createPiece("L", 0);
        game.movePiece("L", DOWN, true);
        game.resetFuture();
        game.createPiece("A", 0);

        PlayerPiece piece_L = game.getBoard().findPiece("L");
        assertTrue(piece_L.getPosition().getX() == 4); // L should be pushed by by shield.
        assertTrue(piece_L.getPosition().getY() == 2);

        game.undo();

        piece_L = game.getBoard().findPiece("L");
        assertTrue(piece_L.getPosition().getX() == 3); // L should be back to its old position.
        assertTrue(piece_L.getPosition().getY() == 2);
    }

    /**
     * Test if sword vs sword reaction is successful.
     */
    @Test
    public void testReaction_Create_Sword_Sword(){
        Board board = new Board();
        Game game = new Game(board);
        Player greenPlayer = new GreenPlayer(game);

        game.setCurrentPlayer(greenPlayer);
        game.createPiece("S", 0);
        game.movePiece("S", DOWN, true);
        game.resetFuture();
        game.createPiece("K", 0);

        // Both piece K and S should not be in the createBoard due to sword vs sword reaction.
        assertTrue(game.getBoard().findPiece("K") == null);
        assertTrue(game.getBoard().findPiece("S") == null);
    }

    /**
     * Test if sword vs sword reaction is successful.
     */
    @Test
    public void testReaction_Create_Sword_Sword_Undo(){
        Board board = new Board();
        Game game = new Game(board);
        Player greenPlayer = new GreenPlayer(game);

        game.setCurrentPlayer(greenPlayer);
        game.createPiece("S", 0);
        game.movePiece("S", DOWN, true);
        game.resetFuture();
        game.createPiece("K", 0);

        // Both piece K and S should not be in the createBoard due to sword vs sword reaction.
        assertTrue(game.getBoard().findPiece("K") == null);
        assertTrue(game.getBoard().findPiece("S") == null);
        game.undo();

        // After undoing create, piece S should be back in the createBoard.
        assertFalse(game.getBoard().findPiece("S") == null);
    }

    /**
     * Test if sword vs nothing reaction is successful.
     */
    @Test
    public void testReaction_Rotate_Sword_Nothing(){
        Board board = new Board();
        Game game = new Game(board);
        Player greenPlayer = new GreenPlayer(game);

        game.setCurrentPlayer(greenPlayer);
        game.createPiece("F", 0);
        game.movePiece("F", DOWN, true);
        game.resetFuture();
        game.createPiece("U", 0);
        game.rotatePiece("U", 180);

        // Piece F should not be in the createBoard due to sword vs nothing reaction.
        assertTrue(game.getBoard().findPiece("F") == null);
    }

    /**
     * Test if sword vs sword reaction is successful.
     */
    @Test
    public void testReaction_Move_Sword_Sword(){
        Board board = new Board();
        Game game = new Game(board);
        Player greenPlayer = new GreenPlayer(game);

        game.setCurrentPlayer(greenPlayer);
        game.createPiece("S", 0);
        game.movePiece("S", DOWN, true);
        game.resetFuture();
        game.createPiece("T", 0);
        game.rotatePiece("T", 90);
        game.movePiece("S", UP, true);

        // Piece S and T should not be in the createBoard due to sword vs nothing reaction.
        assertTrue(game.getBoard().findPiece("S") == null);
        assertTrue(game.getBoard().findPiece("T") == null);
    }
}
