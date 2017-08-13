package model.piece;

import model.Position;
import model.player.Player.Direction;

import static model.player.Player.Direction.*;


public class PlayerPiece extends Piece {

    /**
     * Items that are held by the PlayerPiece.
     */
    public enum Item {
        HORIZONTAL_SWORD(1), VERTICAL_SWORD(2), NO_ITEM(3), SHIELD(4);

        private int value;

        Item(int value){
            this.value = value;
        }

        public boolean isSword(){
            switch(value){
                case 1:
                    return true;
                case 2:
                    return true;
                default:
                    return false;
            }
        }

        public boolean isShield(){
            return value == 4;
        }

        public boolean isNothing(){
            return value == 3;
        }

        @Override
        public String toString() {
            switch (value){
                case 1:
                    return "-";
                case 2:
                    return "|";
                case 3:
                    return " ";
                case 4:
                    return "#";
                default:
                    return "Error";
            }
        }

        /**
         * Helper method to rotate the orientation of the Sword, if needed.
         * If it is not a Sword, then it simply returns the same item itself.
         * @return
         *      Rotated item
         */
        public Item rotate() {
            switch (value) {
                case 1:
                    return VERTICAL_SWORD;
                case 2:
                    return HORIZONTAL_SWORD;
                default:
                    return this;
            }
        }
    }

    /**
     * Items that are held by the PlayerPiece at all directions.
     */
    private Item top, left, bottom, right;


    private String letter;
    private Position position;
    public static final Direction[] DIRECTIONS = new Direction[]{UP, LEFT, DOWN, RIGHT};

    public PlayerPiece(Item top, Item left, Item bottom, Item right, String letter){
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.letter = letter;

        updateStringRepresentation();
    }

    /**
     * Usually invoked after the PlayerPiece is rotated or created to update the String representation of the
     * piece.
     */
    private void updateStringRepresentation(){
        m_rep = new String[][]{
                {" ", top.toString(), " "},
                {left.toString(), letter, right.toString()},
                {" ", bottom.toString(), " "}
        };
    }

    /**
     * Rotate the PlayerPiece based on the orientation of the rotation. The rotation is anticlockwise.
     * It updates the fields after rotation.
     * @param rotation
     */
    public void rotate(int rotation) {
        Item temp_item;
        switch(rotation){

            case 0:
                break;

            case 90:
                temp_item = top;
                top = right.rotate();
                right = bottom.rotate();
                bottom = left.rotate();
                left = temp_item.rotate();
                updateStringRepresentation();
                break;

            case 180:
                temp_item = top;
                top = bottom;
                bottom = temp_item;
                temp_item = right;
                right = left;
                left = temp_item;
                updateStringRepresentation();
                break;

            case 270:
                temp_item = top;
                top = left.rotate();
                left = bottom.rotate();
                bottom = right.rotate();
                right = temp_item.rotate();
                updateStringRepresentation();
                break;
        }
    }

    public String getLetter(){
        return letter;
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public Position getPosition(){
        return position;
    }

    /**
     * Return the corresponding item based on the direction.
     * @param direction
     * @return
     */
    public Item getItem(Direction direction){
        switch(direction){
            case UP:
                return top;
            case DOWN:
                return bottom;
            case LEFT:
                return left;
            case RIGHT:
                return right;
            default:
                return null;
        }
    }

    /**
     * @return
     *      true if this PlayerPiece belongs to green player.
     */
    public boolean greenPlayer(){
        return Character.isUpperCase(letter.charAt(0));
    }

    /**
     * @return
     *      false if this PlayerPiece belongs to yellow player.
     */
    public boolean yellowPlayer(){
        return Character.isUpperCase(letter.charAt(0));
    }

    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof PlayerPiece))
            return false;

        PlayerPiece p = (PlayerPiece) o;

        return p.getLetter().equals(getLetter());
    }


    @Override
    public int hashCode(){
        return 31 * 17 + letter.hashCode();
    }

    @Override
    public Piece clone() {
        PlayerPiece clone = new PlayerPiece(top, left, bottom, right, letter);
        clone.setPosition(position);
        return clone;
    }

}
