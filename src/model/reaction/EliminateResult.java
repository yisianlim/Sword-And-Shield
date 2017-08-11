package model.reaction;

import model.Game;
import model.piece.PlayerPiece;

import java.util.List;

/**
 * EliminateResult class stores what piece should be removed from the game and land in the cemetery.
 */
public class EliminateResult implements ReactionResult {

    /**
     * PlayerPiece that should be eliminated due to the sword vs. sword reaction.
     */
    private List<PlayerPiece> toEliminate;

    public EliminateResult(List<PlayerPiece> toEliminate){
        this.toEliminate = toEliminate;
    }

    @Override
    public void execute(Game game) {
        game.eliminate(toEliminate);
    }
}
