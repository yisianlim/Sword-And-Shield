package model;

import model.piece.*;
import model.player.GreenPlayer;
import model.player.Player;
import model.player.Player.Direction;
import model.player.YellowPlayer;

import java.util.*;

public class Game {

    // Current phase of the game.
    public enum Phase{
        CREATE, ACTION
    }

    // Current state of the board.
    private Board board;

    // Counts the total number of moves played during this game.
    // From this, you can determine who's turn it is to play.
    private int moves;

    private Player currentPlayer;
    private List<Player> players;

    // PlayerPiece that have been pushed out of the board.
    private List<PlayerPiece> cemetery;

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
        cemetery = new ArrayList<>();
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
     *          letter of the PlayerPiece in the board we want to move.
     * @param rotation
     *          angle of rotation.
     */
    public void rotatePiece(String letter, int rotation){
        PlayerPiece piece = board.findPiece(letter);

        if(piece == null){
            throw new IllegalArgumentException("No such piece is found on the board");
        } else if(!currentPlayer.validPiece(piece)){
            throw new IllegalArgumentException("This piece does not belong to you");
        } else if(rotation!= 0 && rotation != 90 && rotation!= 180 && rotation != 270){
            throw new IllegalArgumentException("Rotation needs to be 0, 90, 180, 270 only");
        }else if(!unactedPieces.contains(piece)){
            throw new IllegalArgumentException("This piece has already been acted");
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
     * Also check and move neighboring pieces recursively.
     * @param letter
     *          letter of the PlayerPiece in the board we want to move.
     * @param direction
     *          direction of movement
     * @param isNeighbor
     *          flag if it is a neighbor (being pushed by a dominant piece)
     *
     */
    public void movePiece(String letter, Direction direction, boolean isNeighbor){
        PlayerPiece piece = board.findPiece(letter);

        if(piece == null){
            throw new IllegalArgumentException("No such piece is found on the board");
        } else if(!currentPlayer.validPiece(piece)){
            throw new IllegalArgumentException("This piece does not belong to you");
        } else if(!unactedPieces.contains(piece)  && !isNeighbor){
            throw new IllegalArgumentException("This piece has already been acted");
        }

        Position old_position = piece.getPosition();
        Position new_position = old_position.moveBy(direction);

        // If the piece goes out of the board, it should be added to the cemetery.
        if(board.isCemetery(new_position)
                || board.getSquare(new_position) instanceof FacePiece
                || board.getSquare(new_position) instanceof BlankPiece){
            cemetery.add(piece);
            currentPlayer.removeFromPiecesInBoard(piece);
            removeFromUnactedPieces(piece);

            // Update the board.
            board.setSquare(old_position, new EmptyPiece());

            System.out.println("Piece " + piece.getLetter() + " has been pushed to the cemetery :(\n\n");
            return;
        }

        // Update the piece.
        piece.setPosition(new_position);

        // Check for neighbor.
        Piece neighbor = board.getSquare(new_position);

        // Update the game state of unacted piece only if it is not a neighboring piece.
        if(!isNeighbor) {
            removeFromUnactedPieces(piece);
        }

        // If there is a neighbor, we will move the piece to the same direction as well.
        if(neighbor instanceof PlayerPiece){
            PlayerPiece p = (PlayerPiece) neighbor;
            movePiece(p.getLetter(), direction, true);
        }

        // Update the board.
        board.setSquare(new_position, piece);
        board.setSquare(old_position, new EmptyPiece());
    }

    public void removeFromUnactedPieces(PlayerPiece piece){
        unactedPieces.remove(piece);
        unactedPieces.remove(null);
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
        System.out.println("You can now choose to move or rotate any of YOUR pieces on the board");
        System.out.println("Or you can input pass to finish your turn");
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

    public List<PlayerPiece> getCemetery(){
        return this.cemetery;
    }

    public Set<PlayerPiece> getUnactedPieces(){
        return unactedPieces;
    }

    /**
     * updateUnactedPieces is invoked when the game moves on to the action phase.
     */
    public void updateUnactedPieces() {
        unactedPieces = currentPlayer.getAllPiecesInBoard();
    }

}
