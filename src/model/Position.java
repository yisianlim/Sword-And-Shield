package model;

import model.player.Player;

/**
 * Represents an (x,y) position on the game board.
 */
public final class Position {
    private final int x;
    private final int y;

    /**
     * Construct a position on the board
     *
     * @param x
     * @param y
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the X component associated with this position.
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Get the Y component associated with this position.
     *
     * @return
     */
    public int getY() {
        return y;
    }

    public Position moveBy(Player.Direction direction){
        switch(direction){
            case UP:
                return new Position(x-1, y);
            case DOWN:
                return new Position(x+1, y);
            case LEFT:
                return new Position(x, y-1);
            case RIGHT:
                return new Position(x, y+1);
        }
        return null;
    }

    /**
     * Provide a human readable string representing this position.
     */
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
