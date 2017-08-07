package model.player;

import model.Game;
import model.Position;
import model.piece.EmptyPiece;
import model.piece.PlayerPiece;

import java.util.ArrayList;
import java.util.Arrays;

public class GreenPlayer extends Player {

    public static final Position CREATION_GRID = new Position(2,2);

    public GreenPlayer(Game game){
        super("Green", game);
        m_hand = new ArrayList<>(
                Arrays.asList(
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.SHIELD, PlayerPiece.Item.NO_ITEM, "A"),
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.NO_ITEM, "B"),
                        new PlayerPiece(PlayerPiece.Item.SHIELD, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.SHIELD, PlayerPiece.Item.NO_ITEM, "C"),
                        new PlayerPiece(PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.SHIELD, "D"),
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.SHIELD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.SHIELD, "E"),
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.HORIZONTAL_SWORD, "F"),
                        new PlayerPiece(PlayerPiece.Item.SHIELD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.SHIELD, "G"),
                        new PlayerPiece(PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.NO_ITEM, "H"),
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.HORIZONTAL_SWORD, "I"),
                        new PlayerPiece(PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.NO_ITEM, "J"),
                        new PlayerPiece(PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.NO_ITEM, "K"),
                        new PlayerPiece(PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.HORIZONTAL_SWORD, "L"),
                        new PlayerPiece(PlayerPiece.Item.SHIELD, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.SHIELD, "M"),
                        new PlayerPiece(PlayerPiece.Item.SHIELD, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.SHIELD, "N"),
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.SHIELD, "O"),
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.NO_ITEM, "P"),
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.SHIELD, "Q"),
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.SHIELD, PlayerPiece.Item.HORIZONTAL_SWORD, "R"),
                        new PlayerPiece(PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.HORIZONTAL_SWORD, "S"),
                        new PlayerPiece(PlayerPiece.Item.SHIELD, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.HORIZONTAL_SWORD, "T"),
                        new PlayerPiece(PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.SHIELD, "U"),
                        new PlayerPiece(PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.SHIELD, "V"),
                        new PlayerPiece(PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.SHIELD, PlayerPiece.Item.SHIELD, "W"),
                        new PlayerPiece(PlayerPiece.Item.VERTICAL_SWORD, PlayerPiece.Item.HORIZONTAL_SWORD, PlayerPiece.Item.NO_ITEM, PlayerPiece.Item.SHIELD, "X")
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
