package gui.drawers;

import resources.ImageResources;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;

public class BoardLayerUI extends LayerUI<JPanel> {

    private static final Color TRANSPARENT_GRAY = new Color(127,127,127,200);
    private static final double GREEN_POSITION_RATIO = 0.15;
    private static final double YELLOW_POSITION_RATIO = 0.85;
    private static final double FACE_IMAGE_SIZE_RATIO = 0.25;


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
        int greenY = (int) ((GREEN_POSITION_RATIO * w) - (imageSizeY/2));
        int yellowX = (int) ((YELLOW_POSITION_RATIO * w) - (imageSizeX/2));
        int yellowY = (int) ((YELLOW_POSITION_RATIO * w) - (imageSizeY/2));

         // Draw two FacePiece.
        g2.drawImage(ImageResources.GREEN.img, greenX, greenY, imageSizeX, imageSizeY, null);
        g2.drawImage(ImageResources.YELLOW.img, yellowX, yellowY, imageSizeX, imageSizeY, null);

        g2.setColor(TRANSPARENT_GRAY);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
    }
}
