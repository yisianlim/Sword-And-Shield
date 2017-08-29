package gui.drawers;

import model.Game;
import resources.ImageResources;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * BoardLayerUI serves as a helper class which extends LayerUI
 * to render the board with a grey layer of radial gradient paint when the game is over.
 */
public class BoardLayerUI extends LayerUI<JPanel> {

    /**
     * Model of the GUI.
     */
    private Game gameModel;

    /**
     * Colors for layer.
     */
    private static final Color GRAY = new Color(127,127,127,200);
    private static final Color TRANSPARENT = new Color(255,255,255,0);

    /**
     * Ratio and position constants.
     */
    private static final double GREEN_POSITION_RATIO = 0.15;
    private static final double YELLOW_POSITION_RATIO = 0.85;
    private static final double FACE_IMAGE_SIZE_RATIO = 0.25;
    private static final int CORNER = 150;

    /**
     * Distance from the center for the RadialGradientPaint.
     */
    private float dist1, dist2;

    public BoardLayerUI(Game game){
        this.gameModel = game;
        this.dist1 = 0.01f;
        this.dist2 = 0.02f;
    }

    /**
     * increaseDist helps to make the area of the winning face being less greyed out by increasing the dist.
     */
    public void increaseDist(){
        dist1 = dist1 < 0.3f ? dist1 + 0.01f : dist1;
        dist2 = dist2 < 0.5f ? dist1 + 0.01f : dist2;
    }

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

        // Finally, we create the specified RadialGradientPaint.
        float radius = w;
        float[] dist = {0f, dist1, dist2, 1f};
        Color[] colors = {
                TRANSPARENT,
                TRANSPARENT,
                GRAY,
                GRAY};
        RadialGradientPaint p = new RadialGradientPaint(
                center, radius, center,
                dist, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE
        );
        g2.setPaint(p);

        // Draw the grey layer.
        g2.fillOval(-CORNER,-CORNER, 900, 900);
        g2.dispose();
    }
}
