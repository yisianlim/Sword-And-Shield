package game.model.player;

import game.model.Game;
import game.model.Position;
import game.model.piece.EmptyPiece;
import game.model.piece.PlayerPiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static game.model.piece.PlayerPiece.Item.*;

public class GreenPlayer extends Player {

    public static final Position CREATION_GRID = new Position(2,2);

    public GreenPlayer(Game game){
        super("Green", game);
        m_hand = new ArrayList<>(
                Arrays.asList(
                        new PlayerPiece(NO_ITEM, NO_ITEM, SHIELD, NO_ITEM, "A"),
                        new PlayerPiece(NO_ITEM, NO_ITEM, NO_ITEM, NO_ITEM, "B"),
                        new PlayerPiece(SHIELD, NO_ITEM, SHIELD, NO_ITEM, "C"),
                        new PlayerPiece(VERTICAL_SWORD, SHIELD, SHIELD, SHIELD, "D"),
                        new PlayerPiece(NO_ITEM, SHIELD, SHIELD, SHIELD, "E"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, NO_ITEM, HORIZONTAL_SWORD, "F"),
                        new PlayerPiece(SHIELD, SHIELD, SHIELD, SHIELD, "G"),
                        new PlayerPiece(VERTICAL_SWORD, SHIELD, NO_ITEM, NO_ITEM, "H"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, SHIELD, HORIZONTAL_SWORD, "I"),
                        new PlayerPiece(VERTICAL_SWORD, NO_ITEM, NO_ITEM, NO_ITEM, "J"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, VERTICAL_SWORD, NO_ITEM, "K"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, SHIELD, HORIZONTAL_SWORD, "L"),
                        new PlayerPiece(SHIELD, HORIZONTAL_SWORD, NO_ITEM, SHIELD, "M"),
                        new PlayerPiece(SHIELD, NO_ITEM, NO_ITEM, SHIELD, "N"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, VERTICAL_SWORD, SHIELD, "O"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, VERTICAL_SWORD, NO_ITEM, "P"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, SHIELD, SHIELD, "Q"),
                        new PlayerPiece(NO_ITEM, NO_ITEM, SHIELD, HORIZONTAL_SWORD, "R"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, VERTICAL_SWORD, HORIZONTAL_SWORD, "S"),
                        new PlayerPiece(SHIELD, HORIZONTAL_SWORD, SHIELD, HORIZONTAL_SWORD, "T"),
                        new PlayerPiece(VERTICAL_SWORD, SHIELD, NO_ITEM, SHIELD, "U"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, NO_ITEM, SHIELD, "V"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, SHIELD, SHIELD, "W"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, NO_ITEM, SHIELD, "X")
                )
        );
    }

    @Override
    public boolean validCreation() {
        return m_game.getBoard().getSquare(CREATION_GRID) instanceof EmptyPiece;
    }

    @Override
    public Position getCreationGrid() {
        return CREATION_GRID;
    }
}
