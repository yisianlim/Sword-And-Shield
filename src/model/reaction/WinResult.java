package model.reaction;

import model.Game;
import model.piece.PlayerPiece;

public class WinResult implements ReactionResult {

    PlayerPiece winningPiece;

    public WinResult(PlayerPiece winningPiece){
        this.winningPiece = winningPiece;
    }

    @Override
    public void execute(Game game) {
        game.playerHasWon();
    }
}
