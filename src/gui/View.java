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
    Controller controller;
    JPanel currentPanel, mainMenu, infoView;
    CardLayout gamePhase;

    public View(Game g) {
        gameModel = g;
        gameModel.addObserver(this);
        controller = new Controller(gameModel, this);

        mainMenu = new MainMenu(controller);
        infoView = new InfoView(controller);

        this.setFocusable(true);
        JFrame frame = new JFrame("Sword and Shield Game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gamePhase = new CardLayout();
        currentPanel = new JPanel();
        currentPanel.setLayout(gamePhase);

        currentPanel.add(mainMenu, "Main Menu");
        currentPanel.add(infoView, "Information");

        frame.add(currentPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void showMainMenu(){
        gamePhase.show(currentPanel, "Main Menu");
    }

    public void showInformation(){
        gamePhase.show(currentPanel, "Information");
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
