package model;

import model.command.Command;
import model.command.CommandManager;
import model.piece.*;
import model.player.GreenPlayer;
import model.player.Hand;
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

    private CommandManager commandManager;

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
        commandManager = new CommandManager();
    }


    private void setupPlayers() {
        this.players = Arrays.asList(new YellowPlayer(this), new GreenPlayer(this));
        nextPlayer();
    }

    public void nextPlayer(){
        moves++;
        setCurrentPlayer(players.get(moves % players.size()));
        unactedPieces = new HashSet<>();
        commandManager = new CommandManager();
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
     * Create a piece with a given letter on hand on the board. For this to be accepted,
     * the piece must be appropriate for the playerName who's turn it currently is.
     * @param letter
     *            Name of the PlayerPiece to place; cannot be null.
     * @param rotation
     *            rotation of the PlayerPiece input by the user.
     */
    public void createPiece(String letter, int rotation){
        commandManager.executeCommand(new CreatePieceCommand(this, letter, rotation));
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
        commandManager.executeCommand(new RotatePieceCommand(this, letter, rotation));
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
        commandManager.executeCommand(new MovePieceCommand(this, letter, direction, isNeighbor));
    }

    public void undo(){
        if(!commandManager.isUndoAvailable()){
            throw new IllegalArgumentException("There are no more commands to undo");
        }
        commandManager.undo();
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
        currentPlayer.hand.draw();
        drawTurn();
    }

    /**
     * For action phase of the game, we do not need to draw the player's hand as no pieces can be created.
     */
    public void drawActionPhase(){
        drawCreatePhase();
        System.out.println("You can now choose to move or rotate any of YOUR pieces on the board");
        System.out.println("Or you can input pass to finish your turn");
    }

    /**
     * Display to the user on whose turn is it.
     */
    public void drawTurn(){
        System.out.println("\n******** " + currentPlayer.getName() + " player's turn ********\n");
    }

    public int commandsExecuted(){
        return commandManager.commands();
    }

    /**
     * Display to the user the PlayerPiece that are in the cemetery.
     */
    public void drawCemetery(){

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
        List<PlayerPiece> list_copy = new ArrayList<>();
        for(PlayerPiece piece : cemetery){
            list_copy.add(piece);
        }
        return list_copy;
    }

    public Set<PlayerPiece> getUnactedPieces(){
        Set<PlayerPiece> copy_set = new HashSet<>();
        for(PlayerPiece piece : unactedPieces){
            copy_set.add(piece);
        }
        return copy_set;
    }

    /**
     * updateUnactedPieces is invoked when the game moves on to the action phase.
     */
    public void updateUnactedPieces() {
        unactedPieces = currentPlayer.getAllPiecesInBoard();
    }

    private class MovePieceCommand implements Command {

        private Game game;

        // Previous states.
        private List<PlayerPiece> prev_cemetery;
        private Set<PlayerPiece> prev_player_pieces_in_board;
        private Set<PlayerPiece> prev_unacted_pieces;
        private Board prev_board;

        // Move command.
        private String letter;
        private Direction direction;
        private boolean isNeighbor;

        public MovePieceCommand(Game game, String letter, Direction direction, boolean isNeighbor){
            this.game = game;

            this.prev_cemetery = game.getCemetery();
            this.prev_player_pieces_in_board = game.currentPlayer.getAllPiecesInBoard();
            this.prev_unacted_pieces = game.getUnactedPieces();
            this.prev_board = game.getBoard().clone();

            this.letter = letter;
            this.direction = direction;
            this.isNeighbor = isNeighbor;
        }

        @Override
        public void execute() {
            PlayerPiece piece = board.findPiece(letter);

            if(piece == null){
                throw new IllegalArgumentException("No such piece is found on the board");
            } else if(!currentPlayer.validPiece(piece)){
                throw new IllegalArgumentException("This piece does not belong to you");
            } else if(!unactedPieces.contains(piece)  && !isNeighbor){
                throw new IllegalArgumentException("This piece has already been moved/rotated");
            }

            Position old_position = piece.getPosition();
            Position new_position = old_position.moveBy(direction);

            // If the piece goes out of the board, it should be added to the cemetery.
            if(board.isCemetery(new_position) ||
                    board.getSquare(new_position) instanceof FacePiece ||
                    board.getSquare(new_position) instanceof BlankPiece){
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

            // Update the game state of unacted piece only if it is not a neighboring piece.
            if(!isNeighbor) {
                removeFromUnactedPieces(piece);
            }

            // Check for neighbor.
            // If there is a neighbor, we will move the piece to the same direction as well.
            Piece neighbor = board.getSquare(new_position);
            if(neighbor instanceof PlayerPiece){
                PlayerPiece p = (PlayerPiece) neighbor;
                MovePieceCommand movePieceCommand = new MovePieceCommand(game, p.getLetter(), direction, true);
                movePieceCommand.execute();
            }

            // Update the board.
            board.setSquare(new_position, piece);
            board.setSquare(old_position, new EmptyPiece());
        }

        @Override
        public void undo() {
            game.cemetery = prev_cemetery;
            currentPlayer.piecesInBoard = prev_player_pieces_in_board;
            game.unactedPieces = prev_unacted_pieces;
            game.board = prev_board;
        }
    }

    private class RotatePieceCommand implements Command{

        private Game game;

        // Previous states.
        private Set<PlayerPiece> prev_unacted_pieces;
        private Board prev_board;
        private PlayerPiece prev_piece;

        // Rotate command.
        String letter;
        int rotation;

        public RotatePieceCommand(Game game, String letter, int rotation){
            this.game = game;

            this.prev_unacted_pieces = game.getUnactedPieces();
            this.prev_board = game.getBoard().clone();
            this.prev_piece = game.getBoard().findPiece(letter);

            this.letter = letter;
            this.rotation = rotation;
        }


        @Override
        public void execute() {
            PlayerPiece piece = board.findPiece(letter);

            if(piece == null){
                throw new IllegalArgumentException("No such piece is found on the board");
            } else if(!currentPlayer.validPiece(piece)){
                throw new IllegalArgumentException("This piece does not belong to you");
            } else if(rotation!= 0 && rotation != 90 && rotation!= 180 && rotation != 270){
                throw new IllegalArgumentException("Rotation needs to be 0, 90, 180, 270 only");
            } else if(!unactedPieces.contains(piece)){
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

        @Override
        public void undo() {
            game.unactedPieces = prev_unacted_pieces;
            game.board = prev_board;
        }
    }

    private class CreatePieceCommand implements Command{
        private Game game;

        // Previous states.
        private Hand prev_player_hand;
        private Set<PlayerPiece> prev_player_pieces_in_board;
        private Set<PlayerPiece> prev_unacted_pieces;
        private Board prev_board;

        // Create command.
        private String letter;
        private int rotation;

        public CreatePieceCommand(Game game, String letter, int rotation){
            this.game = game;

            this.prev_player_hand = currentPlayer.hand.clone();
            this.prev_player_pieces_in_board = currentPlayer.getAllPiecesInBoard();
            this.prev_unacted_pieces = getUnactedPieces();
            this.prev_board = game.getBoard().clone();

            this.letter = letter;
            this.rotation = rotation;
        }

        @Override
        public void execute() {
            PlayerPiece piece = currentPlayer.hand.getPiece(letter);

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
            currentPlayer.hand.remove(piece);
            currentPlayer.addToPiecesInBoard(piece);

            // Update the game state.
            unactedPieces.add(piece);

            board.setSquare(currentPlayer.getCreationGrid(), piece);
        }

        @Override
        public void undo() {
            game.currentPlayer.hand = prev_player_hand;
            game.currentPlayer.piecesInBoard = prev_player_pieces_in_board;
            game.unactedPieces = prev_unacted_pieces;
            game.board = prev_board;
        }
    }

}
