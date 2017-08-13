package model.reaction;

import model.Game;
import model.piece.PlayerPiece;
import model.player.Player.Direction;

/**
 * PushedResult stores the PlayerPiece that is pushed and what direction it should be pushed by to.
 */
public class PushedResult implements ReactionResult {

    /**
     * PlayerPiece that is pushed back due to the sword vs. shield reaction.
     */
    private PlayerPiece pushedBack;

    /**
     * The direction that the PlayerPiece should be pushed to.
     */
    private Direction direction;

    public PushedResult(PlayerPiece pushedBack, Direction direction){
        this.pushedBack = pushedBack;
        this.direction = direction;
    }

    /**
     * Push the PlayerPiece due to the reaction.
     * @param game
     *          game to push the PlayerPiece in the board.
     */
    @Override
    public void execute(Game game) {
        game.push(pushedBack, direction);
    }
}
