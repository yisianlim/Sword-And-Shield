package gui.drawers;

import gui.views.PrimaryView;
import model.Game;
import model.Position;
import model.piece.Piece;
import model.player.Player;

import javax.swing.*;
import java.awt.*;

public class CemeteryDrawer extends JPanel {

    private Player player;

    private Game gameModel;

    public CemeteryDrawer(Player player, Game gameModel){
        this.player = player;
        this.gameModel = gameModel;
    }

    public JPanel createCemetery(){
        int cemeteryWidth = (int) (PrimaryView.PANEL_WIDTH_RATIO * PrimaryView.getPrimaryViewWidth());
        int cemeteryHeight = (int) (PrimaryView.PANEL_HEIGHT_RATIO * PrimaryView.getPrimaryViewHeight());
        setPreferredSize(new Dimension(cemeteryWidth, cemeteryHeight));
        setLayout(new GridLayout(4, 6, 10,10));
        Piece[][] cemetery = gameModel.getCemetery().getPlayerPiecesInCemetery(player);
        for(int row = 0; row < cemetery.length; row++){
            for(int col = 0; col < cemetery[0].length; col++){
                Position currentPosition = new Position(row, col);
                SquareButton squareButton = new ButtonDrawer(cemetery[row][col],
                        currentPosition,
                        SquareButton.Panel.CEMETERY).makeButton();
                add(squareButton);
            }
        }
        return this;
    }
}
