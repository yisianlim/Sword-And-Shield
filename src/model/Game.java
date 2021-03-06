package model;

import gui.drawers.SquareButton;
import model.command.Command;
import model.command.CommandManager;
import model.piece.EmptyPiece;
import model.piece.FacePiece;
import model.piece.Piece;
import model.piece.PlayerPiece;
import model.player.GreenPlayer;
import model.player.Hand;
import model.player.Player;
import model.player.Player.Direction;
import model.player.YellowPlayer;
import model.reaction.Reaction;
import model.reaction.ReactionResult;

import java.util.*;

import static model.Game.Phase.*;

public class Game extends Observable {

    /**
     * Sword and Shield game is separated into two phases, CREATE and ACTION.
     *
     * CREATE phase is when the user can choose a PlayerPiece from its creation shelf to create. At this point,
     * the user can only choose to create or pass.
     *
     * ACTION phase is when the user can move or rotate any of its PlayerPiece that is in the createBoard. The user can also
     * undo its move and go back to CREATE phase or pass to pass the game to the next player.
     *
     * FINAL phase is when the user moves all of their pieces on the createBoard. The user can only input pass or undo at
     * this point.
     */
    public enum Phase{
        DISPLAY, CREATE, ACTION, FINAL,
    }

    /**
     * Current state of the createBoard.
     */
    public Board board;

    /**
     * Counts the total number of moves played during this game.
     * From this, we can determine whose turn it is to play.
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
     * The winning player of the game. Remains null until the game is over.
     */
    private Player winner;

    /**
     * Represents the phase the game is currently in.
     */
    private Phase gamePhase;

    /**
     * Stores and manages the commands that have been executed by the current player.
     */
    private CommandManager commandManager;

    /**
     * This is mainly for the view, to let the user know about the status of the game.
     */
    private String status;

    /**
     * Warning count to be shown to the user. This is used to keep track how many times
     * the user have tried to create when its creation grid is occupied.
     */
    private int warning;

    /**
     * Start and end time of the game used to determine how long the game was played.
     */
    private long startTime;
    private long endTime;

    /**
     * PlayerPiece that just died in the previous move by the player. We use this to animate
     * the demise of the piece in the GUI.
     */
    private List<PlayerPiece> newlyDead;

    /**
     * Construct a new game from a given starting createBoard.
     *
     * @param b
     */
    public Game(Board b) {
        reset(b);
    }

    public void reset(Board b){
        this.moves = 0;
        setupPlayers();
        this.startTime = System.currentTimeMillis();
        this.gamePhase = Phase.DISPLAY;
        this.board = b;
        this.gameOver = false;
        this.cemetery = new Cemetery();
        this.commandManager = new CommandManager();
        this.status = getCurrentPlayer().getName() + "'s turn";
    }

    /**
     * Set up the players of the game. Invoked the constructor of the Player also setup all the PlayerPiece.
     */
    private void setupPlayers() {
        this.players = Arrays.asList(new YellowPlayer(this), new GreenPlayer(this));
        moves++;
        warning = 0;
        setCurrentPlayer(players.get(moves % players.size()));
        future = new HashSet<>();
        commandManager = new CommandManager();
        setStatus(currentPlayer.getName() + "'s turn");
    }

    /**
     * When passing the game to the next player, we need to clear the future pieces as well as clear all the commands
     * executed by the previous player (by creating a new CommandManager) so that the next player cannot undo the
     * previous player's move.
     */
    public void nextPlayer(){
        moves++;
        warning = 0;
        setCurrentPlayer(players.get(moves % players.size()));
        future = new HashSet<>();
        resetFuture();
        commandManager = new CommandManager();
        setStatus(currentPlayer.getName() + "'s turn");
    }

    /**
     * Get the internal createBoard representation of this game.
     * @return
     *      Board of the current game.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Create a piece with a given letter on hand on the createBoard. For this to be accepted,
     * the piece must be appropriate for the playerName who's turn it currently is.
     * @param letter
     *            Name of the PlayerPiece to place; cannot be null.
     * @param rotation
     *            rotation of the PlayerPiece input by the user.
     */
    public void createPiece(String letter, int rotation){
        clearNewlyDead();
        commandManager.executeCommand(new CreatePieceCommand(this, letter, rotation));

        // Notify and update the observer.
        setStatus("Created");
    }

    /**
     * Rotate a piece with a given letter on the createBoard. For this to be acceptable, the piece must
     * belong to the currentPlayer as well as be in the createBoard.
     * @param letter
     *          letter of the PlayerPiece in the createBoard we want to move.
     * @param rotation
     *          angle of rotation.
     */
    public void rotatePiece(String letter, int rotation){
        clearNewlyDead();
        commandManager.executeCommand(new RotatePieceCommand(this, letter, rotation));
    }

    /**
     * Move a piece with a given letter on the createBoard.
     * @param letter
     *          letter of the PlayerPiece in the createBoard we want to move.
     * @param direction
     *          direction of movement
     * @param dominant
     *          flag if it is the dominant piece
     *
     */
    public void movePiece(String letter, Direction direction, boolean dominant){
        clearNewlyDead();
        commandManager.executeCommand(new MovePieceCommand(this, letter, direction, dominant));

        // Reset the selected square.
        clearSelectedSquareInBoard();

        // Notify and update the observer.
        setStatus("Moved");
    }

    /**
     * Pass creating a PlayerPiece or pass the game to the next player.
     */
    public void pass(){
        clearNewlyDead();
        commandManager.executeCommand(new PassCommand());
    }

    /**
     * Undo the previous command of the game. For this to be valid,
     * commandManager will check if there are any commands left before undo.
     */
    public void undo() {
        clearNewlyDead();
        if(!commandManager.isUndoAvailable()){
            throw new IllegalArgumentException("There are no more commands to undo");
        }
        currentPlayer.increaseUndo();
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


    /**
     * getFuture returns the set of PlayerPiece in the createBoard that have not been moved / rotated by the current player.
     * @return
     *      Set of PlayerPiece to be moved / rotated in future.
     */
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
     * PlayerPiece that are on the createBoard as the PlayerPiece can be moved / rotated again in future.
     */
    public void resetFuture() {
        future = currentPlayer.getAllPiecesInBoard();
    }

    /**
     * Return true if the PlayerPiece in the specified position on the board have been moved or not.
     * Return false otherwise.
     * @param position
     *          Position of the Piece on the board.
     * @return
     *          true if the Piece have been moved.
     */
    public boolean movedPiece(Position position){
        Piece piece = board.getSquare(position);

        if(piece instanceof PlayerPiece){
            return !future.contains(piece);
        }

        return false;
    }

    /**
     * Return the current player of the game.
     * @return
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Player getGreenPlayer(){
        return players.get(1);
    }

    public Player getYellowPlayer(){
        return players.get(0);
    }


    public void setStatus(String message){
        this.status = message;
        setChanged();
        notifyObservers();
    }

    public void clearSelectedSquareInBoard(){
        board.clearSelectedSquare();
    }

    public void warningBeep(String message){
        warning++;
        setStatus("Error: " + message);
    }

    /**
     * Return true if there was a piece that just died. Used for sound effects.
     * @return
     */
    public boolean pieceJustDied(){
        return newlyDead != null && newlyDead.size() > 0;
    }

    /**
     * Clear the newlyDead record.
     */
    public void clearNewlyDead(){
        newlyDead = new ArrayList<>();
    }


    /**
     * Return how many times have the user been warned of their invalid inputs.
     * @return
     */
    public int getWarnings(){
        return warning;
    }

    public String getStatus(){
        return this.status;
    }

    /**
     * The game is finally won by the currentPlayer. Set the gameOver flag to true and update the winner.
     */
    public void playerHasWon(){
        endTime = System.currentTimeMillis();
        gamePhase = DISPLAY;
        gameOver = true;
        winner = currentPlayer;
        setStatus("Game over");
    }

    public void playerHasSurrender(){
        endTime = System.currentTimeMillis();
        gamePhase = DISPLAY;
        gameOver = true;
        winner = currentPlayer instanceof GreenPlayer ? getYellowPlayer() : getGreenPlayer();
        setStatus("Game over");
    }

    public String timeTaken(){
        long timeElapsed = endTime - startTime;
        int minutes = (int)((timeElapsed/1000) / 60);
        int seconds = (int)((timeElapsed/1000) % 60);
        return minutes + " minutes and " + seconds + " seconds";
    }

    public String undoMoves(){
        String s = "Green player have used undo " + getGreenPlayer().getUndoMoves() + " times\n";
        s += "Yellow player have used undo " + getYellowPlayer().getUndoMoves() + " times";
        return s;
    }

    public String moves(){
        return moves + " moves";
    }

    public String deadPieces(){
        return "Number of pieces died: " + cemetery.getDeadPiecesCount();
    }

    /**
     * Return the name of the winning player.
     * @return
     *      String containing the name of the winning player.
     */
    public String getWinnerName(){
        return winner.getName();
    }

    /**
     * Return the winning player.
     * @return
     *      Player of the winning player.
     */
    public Player getWinner(){
        return winner;
    }

    /**
     * Return true if there used to be a PlayerPiece in that position in the board.
     * @param position
     *          Position in the board.
     * @return
     *          true if there used to be a PlayerPiece
     */
    public boolean usedToHaveAPiece(Position position){
        if(newlyDead == null) return false;
        for(PlayerPiece p: newlyDead){
            if(p.getPosition().getX() == position.getX() &&
                    p.getPosition().getY() == position.getY()){
                return true;
            }
        }
        return false;
    }

    /**
     * Return a SquareButton of the PlayerPiece that used to be in the position of the
     * board.
     * @param position
     *             Position of the board that used to have a PlayerPiece.
     * @return
     *              Corresponding SquareButton of the newly dead PlayerPiece.
     */
    public SquareButton getNewlyDead(Position position){
        for(PlayerPiece p: newlyDead){
            if(p.getPosition().getX() == position.getX() &&
                    p.getPosition().getY() == position.getY()){
                return p.createButton(position, SquareButton.Panel.BOARD_DISPLAY);
            }
        }
        return null;
    }


    /**
     * CreatePieceCommand class executes the create as well as undo the created.
     * It stores the information of the state of the game before the PlayerPiece is created.
     * It also stores the user inputs in order to execute the command.
     */
    private class CreatePieceCommand implements Command{
        private Game game;

        /**
         * Previous states before create execution.
         */
        private Hand prev_player_hand;
        private Set<PlayerPiece> prev_player_pieces_in_board;
        private Set<PlayerPiece> prev_future;
        private Board prev_board;
        private Cemetery prev_cemetery;

        /**
         * Create command input by user.
         */
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

        /**
         * For this to be acceptable, the piece must belong to the currentPlayer and its creation grid
         * must be empty.
         * The PlayerPiece is created and then its neighbors are checked for reactions.
         */
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

            // Update the createBoard.
            Position newPosition = currentPlayer.getCreationGrid();
            board.setSquare(newPosition, pieceToCreate);

            // Check for reactions at all directions and store it in the list.
            List<ReactionResult> reactions = checkForReactions(pieceToCreate);

            // Finally, lets execute the reactions.
            executeReaction(reactions);

            // Change game phase if all pieces have been moved.
            if(future.isEmpty()){
                gamePhase = FINAL;
            } else {
                gamePhase = ACTION;
            }

        }

        /**
         * Undo the create command by restoring the game to its previous state.
         */
        @Override
        public void undo() {
            game.currentPlayer.hand = prev_player_hand;
            game.currentPlayer.piecesInBoard = prev_player_pieces_in_board;
            game.future = prev_future;
            game.board = prev_board;
            game.cemetery = prev_cemetery;
            gamePhase = DISPLAY;
            setStatus("Undo create");
        }
    }

    /**
     * MovePieceCommand class executes the move as well as undo the move.
     * It stores the information of the state of the game before the PlayerPiece is moved.
     * It also stores the user inputs in order to execute the command.
     */
    private class MovePieceCommand implements Command {

        private Game game;

        /**
         * Previous states before move execution.
         */
        private Cemetery prev_cemetery;
        private Set<PlayerPiece> prev_player_pieces_in_board;
        private Set<PlayerPiece> prev_future;
        private Board prev_board;
        private Phase prev_phase;

        /**
         * Create command input by user.
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
            this.prev_phase = game.getGamePhase();

            this.letter = letter;
            this.direction = direction;
            this.dominant = dominant;
        }

        /**
         * For this to be acceptable, the piece must belong to the currentPlayer, be on the createBoard and must not have
         * been moved by the player in the same round.
         * We also check for neighbors in the direction of the move and call the execute the move recursively if
         * neighbors is present, which will take care of all the successive neighbors.
         * Then, check its new neighbors for reactions before executing them.
         */
        @Override
        public void execute() {
            PlayerPiece pieceToMove = board.findPiece(letter);

            if(pieceToMove == null){
                throw new IllegalArgumentException("No such piece is found on the createBoard");
            } else if(!currentPlayer.validPiece(pieceToMove)){
                throw new IllegalArgumentException("This piece does not belong to you");
            } else if(!future.contains(pieceToMove)  && dominant){
                throw new IllegalArgumentException("This piece has already been moved/rotated");
            } else if(cemetery.contains(pieceToMove)){
                throw new IllegalArgumentException("This piece is already in the cemetery");
            }

            Position old_position = pieceToMove.getPosition();
            Position new_position = old_position.moveBy(direction);

            // If the piece goes out of the createBoard, it should be added to the cemetery.
            // The game state is updated accordingly before finishing its execution.
            if(board.outOfBoard(new_position)) {
                cemetery.add(pieceToMove);
                newlyDead.add(pieceToMove);
                currentPlayer.removeFromPiecesInBoard(pieceToMove);
                removeFromFuture(pieceToMove);

                // Update the createBoard.
                board.setSquare(old_position, new EmptyPiece());
                return;
            }

            // Update the piece.
            pieceToMove.setPosition(new_position);

            // Update the game state of future piece only if it is a dominant piece.
            // Neighbor pieces are still to be moved / rotated in future.
            if(dominant) {
                removeFromFuture(pieceToMove);
            }

            // Check for neighbor.
            // If there is a neighbor, we will move the neighbor piece to the same direction recursively.
            Piece neighbor = board.getSquare(new_position);
            if(neighbor instanceof PlayerPiece){
                PlayerPiece p = (PlayerPiece) neighbor;
                MovePieceCommand movePieceCommand = new MovePieceCommand(game, p.getLetter(), direction, false);
                movePieceCommand.execute();
            }

            // Update the createBoard.
            board.setSquare(new_position, pieceToMove);
            board.setSquare(old_position, new EmptyPiece());

            // Check for reactions at all directions and store it in the list.
            List<ReactionResult> reactions = checkForReactions(pieceToMove);

            // Finally, lets execute the reactions.
            executeReaction(reactions);

            // Change game phase if all pieces have been moved.
            if(future.isEmpty()){
                gamePhase = FINAL;
            }
        }


        /**
         * Undo the move command by restoring the game to its previous state.
         */
        @Override
        public void undo() {
            game.cemetery = prev_cemetery;
            currentPlayer.piecesInBoard = prev_player_pieces_in_board;
            game.future = prev_future;
            game.board = prev_board;
            gamePhase = prev_phase;
            setStatus("Undo move");
        }
    }

    /**
     * RotatePieceCommand class executes the rotate as well as undo the move.
     * It stores the information of the state of the game before the PlayerPiece is rotated.
     * It also stores the user inputs in order to execute the command.
     */
    private class RotatePieceCommand implements Command{

        private Game game;

        /**
         * Previous states before rotate execution.
         */
        private Cemetery prev_cemetery;
        private Set<PlayerPiece> prev_player_pieces_in_board;
        private Set<PlayerPiece> prev_future;
        private Board prev_board;
        private Phase prev_phase;

        /**
         * Rotate command input by user.
         */
        String letter;
        int rotation;

        public RotatePieceCommand(Game game, String letter, int rotation){
            this.game = game;
            this.prev_cemetery = game.getCemetery();
            this.prev_player_pieces_in_board = currentPlayer.getAllPiecesInBoard();
            this.prev_future = game.getFuture();
            this.prev_board = game.getBoard().clone();
            this.prev_phase = gamePhase;

            this.letter = letter;
            this.rotation = rotation;
        }

        /**
         * For this to be acceptable, the piece must belong to the currentPlayer, be on the createBoard and must not have
         * been moved / rotated by the player in the same round.
         * The PlayerPiece is rotated and then its neighbors are checked for reactions.
         */
        @Override
        public void execute() {
            PlayerPiece pieceToRotate = board.findPiece(letter);

            if(pieceToRotate == null){
                throw new IllegalArgumentException("No such piece is found on the createBoard");
            } else if(!currentPlayer.validPiece(pieceToRotate)){
                throw new IllegalArgumentException("This piece does not belong to you");
            } else if(rotation!= 0 && rotation != 90 && rotation!= 180 && rotation != 270){
                throw new IllegalArgumentException("Rotation needs to be 0, 90, 180, 270 only");
            } else if(!future.contains(pieceToRotate)){
                throw new IllegalArgumentException("This piece has already been acted");
            }

            // Update the piece's rotation.
            pieceToRotate.rotate(rotation);

            // Update the game state.
            future.remove(pieceToRotate);
            future.remove(null);

            // Update the createBoard.
            board.setSquare(pieceToRotate.getPosition(), pieceToRotate);

            // Check for reactions at all directions and store it in the list.
            List<ReactionResult> reactions = checkForReactions(pieceToRotate);

            // Finally, lets execute the reactions.
            executeReaction(reactions);

            // Change game phase if all pieces have been moved.
            if(future.isEmpty()){
                gamePhase = FINAL;
            }
        }

        /**
         * Undo the rotated command by restoring the game to its previous state.
         */
        @Override
        public void undo() {
            game.cemetery = prev_cemetery;
            game.currentPlayer.piecesInBoard = prev_player_pieces_in_board;
            game.future = prev_future;
            game.board = prev_board;
            gamePhase = prev_phase;
        }
    }

    /**
     * PassCommand class executes the pass as well as undo the pass (only for CREATE phase)
     * It stores the information of the state of the game before the player passes its CREATE phase.
     */
    private class PassCommand implements Command{

        /**
         * Executes the pass command.
         * If the game is in CREATE phase, we just moved on to ACTION phase if there are pieces to be moved / rotated
         * Otherwise, we move on to FINAL phase.
         * If the game is in ACTION / FINAL phase. We pass the game to the next player and reset the phase to
         * CREATE.
         */
        @Override
        public void execute() {
            switch(gamePhase){
                case DISPLAY:
                    resetFuture();
                    warning = 0;
                    if(future.isEmpty()) gamePhase = FINAL;
                    else gamePhase = ACTION;
                    setStatus("Pass create phase");
                    break;
                case ACTION:
                    gamePhase = DISPLAY;
                    nextPlayer();
                    setStatus(currentPlayer.getName() + "'s turn");
                    break;
                case FINAL:
                    gamePhase = DISPLAY;
                    nextPlayer();
                    setStatus(currentPlayer.getName() + "'s turn");
                    break;
            }
        }

        /**
         * Undo the pass command by going back to CREATE phase.
         */
        @Override
        public void undo() {
            switch(gamePhase) {
                case CREATE:
                    throw new IllegalArgumentException("There are no more commands to undo");
                case ACTION:
                    gamePhase = DISPLAY;
                    setStatus("Undo pass");
                    break;
                case FINAL:
                    gamePhase = DISPLAY;
                    setStatus("Undo pass");
                    break;
            }
        }
    }
    
    /**
     * Check for reactions around PlayerPiece piece and return the list of reactions that will occur.
     * @param piece
     *          PlayerPiece to check reactions against.
     * @return
     *          List of ReactionResult.
     */
    private List<ReactionResult> checkForReactions(PlayerPiece piece){
        List<ReactionResult> reactions = new ArrayList<>();
        // For each direction surrounding the piece.
        for(Direction direction: piece.DIRECTIONS){
            // Retrieve the position at the corresponding direction.
            Position positionAtDirection = piece.getPosition().moveBy(direction);

            // If positionAtDirection is outside of the createBoard, we do not need to check for reactions.
            if(positionAtDirection.outsideOfBoard()){
                continue;
            }

            // Get the piece at that direction.
            Piece pieceAtDirection = board.getSquare(positionAtDirection);

            // Determine what reaction has occurred and store it in the list.
            Reaction reaction;
            ReactionResult result = null;
            if(pieceAtDirection instanceof PlayerPiece){
                reaction = new Reaction(piece, (PlayerPiece) pieceAtDirection, direction);
                result = reaction.getReactionResult();
            } else if(pieceAtDirection instanceof FacePiece){
                reaction = new Reaction(piece, (FacePiece) pieceAtDirection, direction);
                result = reaction.getWinningStatus();
            }

            if(result != null){
                reactions.add(result);
            }
        }
        return reactions;
    }

    /**
     * Execute all of the reactions.
     * @param reactions
     *          Reactions to be executed
     *
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
            newlyDead.add(piece);
            currentPlayer.removeFromPiecesInBoard(piece);
            future.remove(piece);
            board.setSquare(piece.getPosition(), new EmptyPiece());
        }
    }

    /**
     * Push a PlayerPiece to the direction.
     * @param toPush
     *          PlayerPiece to push.
     * @param direction
     *          Direction to push.
     */
    public void push(PlayerPiece toPush, Direction direction){
        new MovePieceCommand(this, toPush.getLetter(), direction, false).execute();
    }
}
