package model.reaction;

import model.piece.FacePiece;
import model.piece.PlayerPiece;
import model.piece.PlayerPiece.Item;
import model.player.Player.Direction;

import java.util.Arrays;

/**
 * Reaction class has uses two PlayerPiece to determine the type of reaction that occurs between them.
 *
 */
public class Reaction {

    /**
     * The two PlayerPiece to check against each other for reaction.
     */
    private PlayerPiece pieceOne;
    private PlayerPiece pieceTwo;
    private FacePiece facePiece;

    /**
     * Direction to check for. To make it consistent, this direction is based on pieceOne direction.
     */
    private Direction direction;

    public Reaction(PlayerPiece pieceOne, PlayerPiece pieceTwo, Direction direction){
        this.pieceOne = pieceOne;
        this.pieceTwo = pieceTwo;
        this.direction = direction;
    }

    public Reaction(PlayerPiece pieceOne, FacePiece facePiece, Direction direction){
        this.pieceOne = pieceOne;
        this.facePiece = facePiece;
        this.direction = direction;
    }

    /**
     * Check for the reactions between the two PlayerPiece on pieceOne direction and return the corresponding
     * ReactionResult. Returns null if there are no reactions.
     * @return
     *      ReactionResult which stores what reaction has occured and on what piece.
     */
    public ReactionResult getReactionResult(){
        Item pieceOneItem = pieceOne.getItem(direction);
        Item pieceTwoItem = pieceTwo.getItem(direction.opposite());

        // Sword against nothing case.
        if(pieceOneItem.isSword() && pieceTwoItem.isNothing()){
            return new EliminateResult(Arrays.asList(pieceTwo));
        } else if(pieceTwoItem.isSword() && pieceOneItem.isNothing()){
            return new EliminateResult(Arrays.asList(pieceOne));
        }

        // Sword against sword case.
        if(pieceOneItem.isSword() && pieceTwoItem.isSword()){
            return new EliminateResult(Arrays.asList(pieceOne,pieceTwo));
        }

        // Sword against shield case.
        if(pieceOneItem.isSword() && pieceTwoItem.isShield()){
            return new PushedResult(pieceOne, direction.opposite());
        } else if(pieceTwoItem.isSword() && pieceOneItem.isShield()){
            return new PushedResult(pieceTwo, direction);
        }

        return null;
    }

    /**
     * Check if the reaction of the FacePiece and PlayerPiece will finally declare a winner in the game.
     * Returns a WinResult if there is a winner. Null is the winning status is not happening.
     * @return
     *      ReactionResult of the winning status.
     */
    public ReactionResult getWinningStatus(){
        Item pieceOneItem = pieceOne.getItem(direction);

        // Green player won.
        if(pieceOneItem.isSword() && pieceOne.greenPlayer() && facePiece.yellowPlayer()){
            return new WinResult(pieceOne);
        }

        // Yellow player won.
        if(pieceOneItem.isSword() && pieceOne.yellowPlayer() && facePiece.greenPlayer()){
            return new WinResult(pieceOne);
        }

        return null;
    }
}
