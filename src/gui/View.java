package gui;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by limyisi on 17/08/17.
 */
public class View extends JComponent implements Observer {

    private static final long serialVersionUID = 1L;
    Game gameModel;

    public View(Game g) {
        gameModel = g;
        gameModel.addObserver(this);

        this.setFocusable(true);
        JFrame f = new JFrame("Sword and Shield Game");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Welcome to Sword and Shield Game");

        f.add(new MainMenu());
        f.pack();
        f.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,600);
    }
}
