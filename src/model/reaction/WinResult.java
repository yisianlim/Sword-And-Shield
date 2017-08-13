package model.reaction;

import model.Game;
import model.piece.PlayerPiece;

public class WinResult implements ReactionResult {

    /**
     * The PlayerPiece that stabs the opponent's FacePiece with its sword.
     */
    PlayerPiece winningPiece;

    public WinResult(PlayerPiece winningPiece){
        this.winningPiece = winningPiece;
    }

    /**
     * Declare the winner of the game.
     * @param game
     *          Game to declare the winner.
     */
    @Override
    public void execute(Game game) {
        game.playerHasWon();
    }
}
