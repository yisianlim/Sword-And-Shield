package model.player;

import java.util.List;
import model.Game;
import model.Position;
import model.piece.EmptyPiece;
import model.piece.PlayerPiece;

import java.util.ArrayList;
import java.util.Arrays;

import static model.piece.PlayerPiece.Item.*;

public class YellowPlayer extends Player {

    public static final Position CREATION_GRID = new Position(7,7);

    public YellowPlayer(Game game){
        super("Yellow", game);
        List<PlayerPiece> pieces = new ArrayList<>(
                Arrays.asList(
                        new PlayerPiece(NO_ITEM, NO_ITEM, SHIELD, NO_ITEM, "a"),
                        new PlayerPiece(NO_ITEM, NO_ITEM, NO_ITEM, NO_ITEM, "b"),
                        new PlayerPiece(SHIELD, NO_ITEM, SHIELD, NO_ITEM, "c"),
                        new PlayerPiece(VERTICAL_SWORD, SHIELD, SHIELD, SHIELD, "d"),
                        new PlayerPiece(NO_ITEM, SHIELD, SHIELD, SHIELD, "e"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, NO_ITEM, HORIZONTAL_SWORD, "f"),
                        new PlayerPiece(SHIELD, SHIELD, SHIELD, SHIELD, "g"),
                        new PlayerPiece(VERTICAL_SWORD, SHIELD, NO_ITEM, NO_ITEM, "h"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, SHIELD, HORIZONTAL_SWORD, "i"),
                        new PlayerPiece(VERTICAL_SWORD, NO_ITEM, NO_ITEM, NO_ITEM, "j"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, VERTICAL_SWORD, NO_ITEM, "k"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, SHIELD, HORIZONTAL_SWORD, "l"),
                        new PlayerPiece(SHIELD, HORIZONTAL_SWORD, NO_ITEM, SHIELD, "m"),
                        new PlayerPiece(SHIELD, NO_ITEM, NO_ITEM, SHIELD, "n"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, VERTICAL_SWORD, SHIELD, "o"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, VERTICAL_SWORD, NO_ITEM, "p"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, SHIELD, SHIELD, "q"),
                        new PlayerPiece(NO_ITEM, NO_ITEM, SHIELD, HORIZONTAL_SWORD, "r"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, VERTICAL_SWORD, HORIZONTAL_SWORD, "s"),
                        new PlayerPiece(SHIELD, HORIZONTAL_SWORD, SHIELD, HORIZONTAL_SWORD, "t"),
                        new PlayerPiece(VERTICAL_SWORD, SHIELD, NO_ITEM, SHIELD, "u"),
                        new PlayerPiece(NO_ITEM, HORIZONTAL_SWORD, NO_ITEM, SHIELD, "v"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, SHIELD, SHIELD, "w"),
                        new PlayerPiece(VERTICAL_SWORD, HORIZONTAL_SWORD, NO_ITEM, SHIELD, "x")
                )
        );
        hand = new Hand(pieces);
    }

    @Override
    public boolean validCreation() {
        return game.getBoard().getSquare(CREATION_GRID) instanceof EmptyPiece;
    }

    @Override
    public Position getCreationGrid() {
        return CREATION_GRID;
    }
}
