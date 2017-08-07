package model;

import model.piece.BlankPiece;
import model.piece.EmptyPiece;
import model.piece.FacePiece;
import model.piece.PlayerPiece;
import model.player.GreenPlayer;
import model.player.Player;
import model.player.YellowPlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {

    // Current phase of the game.
    public enum Phase{
        CREATE, ACTION
    };

    // Current state of the board.
    private Board board;

    // Counts the total number of moves played during this game.
    // From this, you can determine who's turn it is to play.
    private int moves;

    private Player currentPlayer;
    private List<Player> players;

    // Pieces in the game that is yet to be acted by the player.
    private Set<PlayerPiece> unactedPieces;

    private boolean gameOver;
    private Phase gamePhase;

    /**
     * Commands that were executed during a Player's round
     */

    /**
     * Construct a new game from a given starting board.
     *
     * @param b
     */
    public Game(Board b) {
        this.board = b;
        setupPlayers();
        gameOver = false;
        setGamePhase(Phase.CREATE);
    }


    private void setupPlayers() {
        this.players = Arrays.asList(new YellowPlayer(this), new GreenPlayer(this));
        nextPlayer();
    }

    public void nextPlayer(){
        moves++;
        setCurrentPlayer(players.get(moves % players.size()));
        unactedPieces = new HashSet<>();
    }

    private void beginGame() {
        // Loop until the game is over.
        boolean gameOver = false;

        // Number of moves taken throughout the game. Used to determine which player's turn.
        moves = 1;


        while (!gameOver) {
            setCurrentPlayer(players.get(moves % players.size()));
            moves++;
        }
    }

    /**
     * Get the internal board representation of this game.
     *
     * @return
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Create a piece with a given letter on hand on the board. For this to be accepted, the piece must
     * be appropriate for the playerName who's turn it currently is.
     * @param letter
     *            Name of the PlayerPiece to place; cannot be null.
     * @param rotation
     *            rotation of the PlayerPiece input by the user.
     */
    public void createPiece(String letter, int rotation){
        PlayerPiece piece = currentPlayer.getPieceFromHand(letter);

        if(!currentPlayer.validCreation()){
            throw new IllegalArgumentException("Your creation grid is occupied");
        } else if(piece == null){
            throw new IllegalArgumentException("You do not have such piece to create");
        } else if (rotation!=0 && rotation != 90 && rotation!= 180 && rotation !=270){
            throw new IllegalArgumentException("Rotation needs to be 0, 90, 180, 270 only");
        }

        // Update the piece's rotation and position.
        piece.rotate(rotation);
        piece.setPosition(currentPlayer.getCreationGrid());

        // Update the player's state.
        currentPlayer.removeFromHand(piece);
        currentPlayer.addToPiecesInBoard(piece);

        // Update the game state.
        unactedPieces.add(piece);

        board.setSquare(currentPlayer.getCreationGrid(), piece);
    }

    /**
     * Rotate a piece with a given letter on the board. For this to be acceptable, the piece must
     * belong to the currentPlayer as well as be in the board.
     * @param letter
     * @param rotation
     */
    public void rotatePiece(String letter, int rotation){
        PlayerPiece piece = board.findPiece(letter);

        if(piece == null){
            throw new IllegalArgumentException("No such piece is found on the board");
        } else if(!currentPlayer.validPiece(piece)){
            throw new IllegalArgumentException("This piece does not belong to you");
        }

        // Update the piece's rotation.
        piece.rotate(rotation);

        // Update the game state.
        unactedPieces.remove(piece);
        unactedPieces.remove(null);

        // Update the board.
        board.setSquare(piece.getPosition(), piece);
    }

    /**
     * Move a piece with a given letter on the board. For this to be acceptable, the piece must
     * belong to the currentPlayer, be on the board and be a valid move.
     * @param letter
     * @param direction
     */
    public void movePiece(String letter, Player.Direction direction){
        PlayerPiece piece = board.findPiece(letter);

        if(piece == null){
            throw new IllegalArgumentException("No such piece is found on the board");
        } else if(!currentPlayer.validPiece(piece)){
            throw new IllegalArgumentException("This piece does not belong to you");
        } else if (board.getSquare(piece.getPosition().moveBy(direction)) instanceof FacePiece){
            throw new IllegalArgumentException("You cannot move into a FacePiece");
        } else if (board.getSquare(piece.getPosition().moveBy(direction)) instanceof BlankPiece){
            throw new IllegalArgumentException("You cannot move into a BlankPiece");
        }

        Position position = piece.getPosition();
        board.setSquare(position, new EmptyPiece());

        // Update the piece.
        position = position.moveBy(direction);
        piece.setPosition(position);

        // Update the game state.
        unactedPieces.remove(piece);
        unactedPieces.remove(null);

        // Update the board.
        board.setSquare(position, piece);
    }

    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

    /**
     * For create phase of the game, we draw the player's hand.
     */
    public void drawCreatePhase() {
        board.draw();
        currentPlayer.drawHand();
        drawTurn();
    }

    /**
     * For action phase of the game, we do not need to draw the player's hand as no pieces can be created.
     */
    public void drawActionPhase(){
        board.draw();
        System.out.println("\n\nYou can now choose to move or rotate any of YOUR pieces on the board");
        System.out.println("Or you can input pass to finish your turn\n");
        drawTurn();
    }

    /**
     * Display to the user on whose turn is it.
     */
    public void drawTurn(){
        System.out.println("\n******** " + currentPlayer.getName() + " player's turn ********\n");
    }

    public boolean gameOver(){
        return gameOver;
    }

    public Phase getGamePhase(){
        return gamePhase;
    }

    public void setGamePhase(Phase phase){
        this.gamePhase = phase;
    }

    public Set<PlayerPiece> getUnactedPieces(){
        return unactedPieces;
    }

    public void updateUnactedPieces() {
        unactedPieces = currentPlayer.getAllPiecesInBoard();
    }

}
