package model.reaction;

import model.Game;

/**
 * ReactionResult simply just stores what reaction just occured between the two PlayerPiece. It is used to retrieve
 * the information on what sort of reaction just occured as well as which piece should be rotated or move back
 * after the reaction.
 */
public interface ReactionResult {
    /**
     * Execute the reaction based on the result.
     */
    void execute(Game game);
}
