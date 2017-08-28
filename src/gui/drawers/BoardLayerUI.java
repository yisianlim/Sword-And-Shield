package gui.drawers;

import model.Game;
import resources.ImageResources;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.geom.Point2D;

public class BoardLayerUI extends LayerUI<JPanel> {

    /**
     * Model of the GUI.
     */
    private Game gameModel;

    public BoardLayerUI(Game game){
        this.gameModel = game;
    }

    private static final Color GRAY = new Color(127,127,127,200);
    private static final Color TRANSPARENT = new Color(255,255,255,0);
    private static final double GREEN_POSITION_RATIO = 0.15;
    private static final double YELLOW_POSITION_RATIO = 0.85;
    private static final double FACE_IMAGE_SIZE_RATIO = 0.25;
    private static final int CORNER = 100;


    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        Graphics2D g2 = (Graphics2D) g.create();

        // Get board size.
        int w = c.getWidth();
        int h = c.getHeight();

        // Calculate image size.
        int imageSizeX = (int) (FACE_IMAGE_SIZE_RATIO * w);
        int imageSizeY = (int) (FACE_IMAGE_SIZE_RATIO * h);

        // Calculate coordinates.
        int greenX = (int) ((GREEN_POSITION_RATIO * w) - (imageSizeX/2));
        int greenY = (int) ((GREEN_POSITION_RATIO * h) - (imageSizeY/2));
        int yellowX = (int) ((YELLOW_POSITION_RATIO * w) - (imageSizeX/2));
        int yellowY = (int) ((YELLOW_POSITION_RATIO * h) - (imageSizeY/2));

         // Draw two FacePiece.
        g2.drawImage(ImageResources.GREEN.img, greenX, greenY, imageSizeX, imageSizeY, null);
        g2.drawImage(ImageResources.YELLOW.img, yellowX, yellowY, imageSizeX, imageSizeY, null);

        // Determine the winner of the game and then draw the radial gradient paint based on the winner.
        // The area around FacePiece of the winning player will look less greyed out.
        Point2D center;
        if(gameModel.getWinner().isGreen()){
            center = new Point2D.Float(greenX, greenY);
        } else {
            center = new Point2D.Float(yellowX + CORNER, yellowY + CORNER);
        }

        float radius = w;
        // 0 , 0.3 , 0.5 , 1
        float[] dist = {0f, 0.3f, 0.5f, 1f};
        Color[] colors = {
                TRANSPARENT,
                TRANSPARENT,
                GRAY,
                GRAY};
        RadialGradientPaint p =
                new RadialGradientPaint(center, radius, center,
                        dist, colors,
                        MultipleGradientPaint.CycleMethod.NO_CYCLE);
        g2.setPaint(p);
        g2.fillOval(-CORNER,-CORNER, 900, 900);
        g2.dispose();
    }
}
