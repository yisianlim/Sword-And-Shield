package model;

import model.command.Command;
import model.command.CommandManager;
import model.piece.*;
import model.player.GreenPlayer;
import model.player.Hand;
import model.player.Player;
import model.player.Player.Direction;
import model.player.YellowPlayer;
import model.reaction.EliminateResult;
import model.reaction.PushedResult;
import model.reaction.Reaction;
import model.reaction.ReactionResult;

import java.util.*;

public class Game {

    /**
     * Sword and Shield game is separated into two phases, CREATE and ACTION.
     *
     * CREATE phase is when the user can choose a PlayerPiece from its creation shelf to create. At this point,
     * the user can only choose to create or pass.
     *
     * ACTION phase is when the user can move or rotate any of its PlayerPiece that is in the board. The user can also
     * undo its move and go back to CREATE phase or pass to pass the game to the next player.
     */
    public enum Phase{
        CREATE, ACTION
    }

    /**
     * Current state of the board.
     */
    private Board board;

    /**
     * Counts the total number of moves played during this game.
     * From this, you can determine whose turn it is to play.
     */
    private int moves;

    /**
     * Players of the game.
     */
    private Player currentPlayer;
    private List<Player> players;

    /**
     * Store the information of the PlayerPiece being pushed out of the game.
     */
    private Cemetery cemetery;

    /**
     * PlayerPiece that are to be moved / rotated by the current player in future.
     */
    private Set<PlayerPiece> future;

    /**
     * Flag to check if game is over.
     */
    private boolean gameOver;

    /**
     * Represents the phase the game is currently in.
     */
    private Phase gamePhase;

    /**
     * Stores and manages the commands that have been executed by the current player.
     */
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
        cemetery = new Cemetery();
        commandManager = new CommandManager();
    }

    /**
     * Set up the players of the game.
     */
    private void setupPlayers() {
        this.players = Arrays.asList(new YellowPlayer(this), new GreenPlayer(this));
        nextPlayer();
    }

    /**
     * When passing the game to the next player, we need to clear the unacted pieces as well as clear all the commands
     * executed by the previous player (by creating a new CommandManager) so that the next player cannot undo the
     * previous player's move.
     */
    public void nextPlayer(){
        moves++;
        setCurrentPlayer(players.get(moves % players.size()));
        future = new HashSet<>();
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
     * Move a piece with a given letter on the board.
     * @param letter
     *          letter of the PlayerPiece in the board we want to move.
     * @param direction
     *          direction of movement
     * @param dominant
     *          flag if it is the dominant piece
     *
     */
    public void movePiece(String letter, Direction direction, boolean dominant){
        commandManager.executeCommand(new MovePieceCommand(this, letter, direction, dominant));
    }

    public void pass(){
        commandManager.executeCommand(new PassCommand());
    }

    /**
     * Undo the previous command of the game. For this to be valid,
     * commandManager will check if there are any commands left before undo.
     */
    public void undo(){
        if(!commandManager.isUndoAvailable()){
            throw new IllegalArgumentException("There are no more commands to undo");
        }
        commandManager.undo();
    }

    /**
     * Remove the instance of Piece in future set as the Piece have already been moved / rotated.
     * @param piece
     *          PlayerPiece to be removed.
     */
    public void removeFromFuture(PlayerPiece piece){
        future.remove(piece);
        future.remove(null);
    }

    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

    public void addToCemetery(PlayerPiece piece){
        cemetery.add(piece);
    }

    /**
     * Draw the create phase of the game.
     */
    public void drawCreatePhase() {
        prepareForNewRound();
        cemetery.draw();

        Player greenPlayer = players.get(1);
        Player yellowPlayer = players.get(0);

        int LINES = 42;

        String line;
        for(int i = 0; i < LINES; i++){
            line = greenPlayer.hand.toLine(i);
            line += board.toLine(i);
            line += yellowPlayer.hand.toLine(i);
            System.out.println(line);
        }
        System.out.println(currentPlayer.getName()+"'s move: ");
    }

    /**
     * Draw the action phase of the game. We simply let the user know that they can move their pieces on the board.
     */
    public void drawActionPhase(){
        drawCreatePhase();
        System.out.println("You can now choose to move or rotate any of YOUR pieces on the board");
        System.out.println("Or you can input pass to finish your turn or undo previous moves");
    }

    /**
     * Prints a gap before it renders the game for the next round.
     */
    private void prepareForNewRound() {
        for(int i=0; i < 10; i++){
            System.out.println();
        }
    }

    /**
     * Display to the user on whose turn is it.
     */
    public void drawTurn(){
        System.out.println("\n******** " + currentPlayer.getName() + " player's turn ********\n");
    }

    /**
     * Checks if there are any commands to undo.
     * @return
     *      true if there are still commands left to undo
     */
    public boolean isUndoAvailable(){
        return commandManager.isUndoAvailable();
    }

    /**
     * Checks if the game is over.
     * @return
     *      true if game is over.
     */
    public boolean gameOver(){
        return gameOver;
    }

    /**
     * Retrieve the information on the game phase.
     * @return
     *      current Phase of the game.
     */
    public Phase getGamePhase(){
        return gamePhase;
    }

    /**
     * Set the phase of the game.
     * @param phase
     *          phase we want to the set the game to.
     */
    public void setGamePhase(Phase phase){
        this.gamePhase = phase;
    }

    /**
     * Retrieve the cemetery to know what PlayerPieces have been pushed out of the game.
     * @return
     */
    public Cemetery getCemetery(){
        return cemetery.clone();
    }

    public Set<PlayerPiece> getFuture(){
        Set<PlayerPiece> copy_set = new HashSet<>();
        for(PlayerPiece piece : future){
            if(!cemetery.contains(piece))
                copy_set.add(piece);
        }
        return copy_set;
    }

    /**
     * resetFuture is invoked when the game moves on to the action phase. Get all the current player's
     * PlayerPiece that are on the board as the Piece can be moved / rotated again in future.
     */
    public void resetFuture() {
        future = currentPlayer.getAllPiecesInBoard();
    }

    /**
     * MovePieceCommand class executes the move as well as undo the move.
     * It stores the information of the state of the game before the PlayerPiece is moved.
     * It also stores the user inputs in order to execute the command.
     */
    private class MovePieceCommand implements Command {

        private Game game;

        /**
         * Previous state of the game before move is executed. They are basically the attributes that are going to
         * be modified after being moved.
         */
        private Cemetery prev_cemetery;
        private Set<PlayerPiece> prev_player_pieces_in_board;
        private Set<PlayerPiece> prev_future;
        private Board prev_board;

        /**
         * The parameters input by the user to carry out the move.
         */
        private String letter;
        private Direction direction;
        private boolean dominant;

        public MovePieceCommand(Game game, String letter, Direction direction, boolean dominant){
            this.game = game;

            this.prev_cemetery = game.getCemetery();
            this.prev_player_pieces_in_board = game.currentPlayer.getAllPiecesInBoard();
            this.prev_future = game.getFuture();
            this.prev_board = game.getBoard().clone();

            this.letter = letter;
            this.direction = direction;
            this.dominant = dominant;
        }

        /**
         * For this to be acceptable, the piece must belong to the currentPlayer, be on the board and must not have
         * been moved by the player in the same round.
         * We also check for neighbors in the direction of the move and call the execute the move recursively if
         * neighbors is present, which will take care of all the successive neighbors.
         */
        @Override
        public void execute() {
            PlayerPiece piece = board.findPiece(letter);

            if(piece == null){
                throw new IllegalArgumentException("No such piece is found on the board");
            } else if(!currentPlayer.validPiece(piece)){
                throw new IllegalArgumentException("This piece does not belong to you");
            } else if(!future.contains(piece)  && dominant){
                throw new IllegalArgumentException("This piece has already been moved/rotated");
            } else if(cemetery.contains(piece)){
                throw new IllegalArgumentException("This piece is already in the cemetery");
            }

            Position old_position = piece.getPosition();
            Position new_position = old_position.moveBy(direction);

            // If the piece goes out of the board, it should be added to the cemetery.
            if(board.outOfBoard(new_position)) {
                cemetery.add(piece);
                currentPlayer.removeFromPiecesInBoard(piece);
                removeFromFuture(piece);

                // Update the board.
                board.setSquare(old_position, new EmptyPiece());
                return;
            }

            // Update the piece.
            piece.setPosition(new_position);

            // Update the game state of future piece only if it is a dominant piece.
            // Neighbor pieces are still to be moved / rotated.
            if(dominant) {
                removeFromFuture(piece);
            }

            // Check for neighbor.
            // If there is a neighbor, we will move the neighbor piece to the same direction as well.
            Piece neighbor = board.getSquare(new_position);
            if(neighbor instanceof PlayerPiece){
                PlayerPiece p = (PlayerPiece) neighbor;
                MovePieceCommand movePieceCommand = new MovePieceCommand(game, p.getLetter(), direction, false);
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
            game.future = prev_future;
            game.board = prev_board;
        }
    }

    private class RotatePieceCommand implements Command{

        private Game game;

        // Previous states.
        private Set<PlayerPiece> prev_future;
        private Board prev_board;

        // Rotate command.
        String letter;
        int rotation;

        public RotatePieceCommand(Game game, String letter, int rotation){
            this.game = game;

            this.prev_future = game.getFuture();
            this.prev_board = game.getBoard().clone();

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
            } else if(!future.contains(piece)){
                throw new IllegalArgumentException("This piece has already been acted");
            }

            // Update the piece's rotation.
            piece.rotate(rotation);

            // Update the game state.
            future.remove(piece);
            future.remove(null);

            // Update the board.
            board.setSquare(piece.getPosition(), piece);
        }

        @Override
        public void undo() {
            game.future = prev_future;
            game.board = prev_board;
        }
    }

    private class CreatePieceCommand implements Command{
        private Game game;

        // Previous states.
        private Hand prev_player_hand;
        private Set<PlayerPiece> prev_player_pieces_in_board;
        private Set<PlayerPiece> prev_future;
        private Board prev_board;
        private Cemetery prev_cemetery;

        // Create command.
        private String letter;
        private int rotation;

        public CreatePieceCommand(Game game, String letter, int rotation){
            this.game = game;

            this.prev_player_hand = currentPlayer.hand.clone();
            this.prev_player_pieces_in_board = currentPlayer.getAllPiecesInBoard();

            resetFuture();
            this.prev_future = getFuture();
            this.prev_board = game.getBoard().clone();
            this.prev_cemetery = cemetery.clone();

            this.letter = letter;
            this.rotation = rotation;
        }

        @Override
        public void execute() {
            PlayerPiece pieceToCreate = currentPlayer.hand.getPiece(letter);

            if(!currentPlayer.validCreation()){
                throw new IllegalArgumentException("Your creation grid is occupied");
            } else if(pieceToCreate == null){
                throw new IllegalArgumentException("You do not have such piece to create");
            } else if (rotation!=0 && rotation != 90 && rotation!= 180 && rotation !=270){
                throw new IllegalArgumentException("Rotation needs to be 0, 90, 180, 270 only");
            }

            // Update the piece's rotation and position.
            pieceToCreate.rotate(rotation);
            pieceToCreate.setPosition(currentPlayer.getCreationGrid());

            // Update the player's state.
            currentPlayer.hand.remove(pieceToCreate);
            currentPlayer.addToPiecesInBoard(pieceToCreate);

            // Update the game state.
            future.add(pieceToCreate);

            // Update the board.
            Position newPosition = currentPlayer.getCreationGrid();
            board.setSquare(newPosition, pieceToCreate);

            // Check for reactions at all directions and store it in the list.
            List<ReactionResult> reactions = new ArrayList<>();
            for(Direction direction: pieceToCreate.DIRECTIONS){

                // Retrieve the position at the corresponding direction.
                Position positionAtDirection = currentPlayer.getCreationGrid().moveBy(direction);

                // If positionAtDirection is outside of the board, we do not need to check for reactions.
                if(positionAtDirection.outsideOfBoard()){
                    continue;
                }

                // Get the piece at that direction.
                Piece pieceAtDirection = board.getSquare(positionAtDirection);

                // Determine what reaction has occured and store it in the list.
                if(pieceAtDirection instanceof PlayerPiece){
                    Reaction reaction = new Reaction(pieceToCreate, (PlayerPiece) pieceAtDirection, direction);
                    ReactionResult result = reaction.getReactionResult();

                    if(result != null){
                        reactions.add(result);
                    }
                }
            }

            // Finally, lets execute the reactions.
            executeReaction(reactions);
        }

        @Override
        public void undo() {
            game.currentPlayer.hand = prev_player_hand;
            game.currentPlayer.piecesInBoard = prev_player_pieces_in_board;
            game.future = prev_future;

            game.board = prev_board;
            game.cemetery = prev_cemetery;
        }
    }

    private class PassCommand implements Command{

        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    /**
     * Execute all of the reactions that happened.
     * @param reactions
     */
    private void executeReaction(List<ReactionResult> reactions) {
        for(ReactionResult reactionResult : reactions){
            reactionResult.execute(this);
        }
    }

    /**
     * Eliminate the PlayerPiece in the toEliminate list.
     * @param toEliminate
     *          List containing PlayerPiece to eliminate
     */
    public void eliminate(List<PlayerPiece> toEliminate) {
        for(PlayerPiece piece : toEliminate){
            cemetery.add(piece);
            currentPlayer.removeFromPiecesInBoard(piece);
            board.setSquare(piece.getPosition(), new EmptyPiece());
        }
    }

    /**
     * Push a PlayerPiece to direction.
     * @param toPush
     *          PlayerPiece to push.
     * @param direction
     *          Direction to push.
     */
    public void push(PlayerPiece toPush, Direction direction){
        new MovePieceCommand(this, toPush.getLetter(), direction, false).execute();
    }

}
