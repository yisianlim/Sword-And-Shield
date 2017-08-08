package model.piece;

import model.Position;

public class PlayerPiece extends Piece {

    public enum Item {
        HORIZONTAL_SWORD(1), VERTICAL_SWORD(2), NO_ITEM(3), SHIELD(4);

        private int value;

        Item(int value){
            this.value = value;
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

    private Item m_top, m_left, m_bottom, m_right;
    private String m_letter;
    private Position m_position;

    public PlayerPiece(Item top, Item left, Item bottom, Item right, String letter){
        m_top = top;
        m_left = left;
        m_bottom = bottom;
        m_right = right;
        m_letter = letter;

        initialise_rep();
    }

    private void initialise_rep(){
        m_rep = new String[][]{
                {" ", m_top.toString(), " "},
                {m_left.toString(), m_letter, m_right.toString()},
                {" ", m_bottom.toString(), " "}
        };
    }

    public String getLetter(){
        return m_letter;
    }

    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof PlayerPiece))
            return false;

        PlayerPiece p = (PlayerPiece) o;

        return p.getLetter().equals(getLetter());
    }

    public void rotate(int rotation) {
        Item temp_item;
        switch(rotation){

            case 0:
                break;

            case 90:
                temp_item = m_top;
                m_top = m_right.rotate();
                m_right = m_bottom.rotate();
                m_bottom = m_left.rotate();
                m_left = temp_item.rotate();
                initialise_rep();
                break;

            case 180:
                temp_item = m_top;
                m_top = m_bottom;
                m_bottom = temp_item;
                temp_item = m_right;
                m_right = m_left;
                m_left = temp_item;
                initialise_rep();
                break;

            case 270:
                temp_item = m_top;
                m_top = m_left.rotate();
                m_left = m_bottom.rotate();
                m_bottom = m_right.rotate();
                m_right = temp_item.rotate();
                initialise_rep();
                break;

            default:
                throw new IllegalArgumentException("Rotation must be either 0, 90, 180 or 270 only");
        }
    }

    @Override
    public int hashCode(){
        return 31 * 17 + m_letter.hashCode();
    }

    public void setPosition(Position position){
        m_position = position;
    }

    public Position getPosition(){
        return m_position;
    }

    @Override
    public Piece clone() {
        PlayerPiece clone = new PlayerPiece(m_top, m_left, m_bottom, m_right, m_letter);
        clone.setPosition(m_position);
        return clone;
    }
}
