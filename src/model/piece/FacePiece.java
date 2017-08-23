package model.piece;

import gui.drawers.SquareButton;
import gui.views.PrimaryView;
import model.Position;
import resources.ImageResources;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents an FacePiece in the Board to display the player symbol.
 * 1 for Green & 0 for Yellow.
 * Anytime a PlayerPiece moves into a FacePiece, it ends up in the cemetery.
 */
public class FacePiece extends Piece {

    private String player;

    public FacePiece(String player){
        this.player = player;
        m_rep = new String[][]{
                {" ", " ", " "},
                {" ", player, " "},
                {" ", " ", " "}
        };
    }

    public boolean greenPlayer(){
        return player.equals("1");
    }

    public boolean yellowPlayer(){
        return player.equals("0");
    }

    @Override
    public SquareButton createButton(Position position, SquareButton.Panel squareType) {
        SquareButton squareButton = new SquareButton(this, position, squareType);
        Image img = greenPlayer() ? ImageResources.GREEN.img : ImageResources.YELLOW.img;
        int width = (int) (0.0375 * PrimaryView.getPrimaryViewWidth());
        int height = (int) (0.08 * PrimaryView.getPrimaryViewHeight());
        img = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH) ;
        squareButton.setIcon(new ImageIcon(img));
        squareButton.setOpaque(false);
        squareButton.setBorderPainted(false);
        return squareButton;
    }
}
