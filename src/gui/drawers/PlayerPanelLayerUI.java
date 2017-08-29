package gui.drawers;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;

/**
 * PlayerPanelLayerUI serves as a helper class in order to create a smooth graphical transition when the player
 * switches panel from display -> create (when the same piece of all 4 rotations are laid out)
 */
public class PlayerPanelLayerUI extends LayerUI<JPanel> {

    /**
     * Alpha component of the layer on top of the panel.
     */
    private int alpha = 200;

    /**
     * Decrease the alpha component.
     */
    public void decreaseAlpha(){
        alpha = alpha > 0 ? --alpha : alpha;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        // Get the panel size.
        int w = c.getWidth();
        int h = c.getHeight();

        // Draw a gray screen with a decrementing alpha component.
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(23, 23, 23, alpha));
        g2.fillRect(0,0, w, h);
        g2.dispose();
    }
}
