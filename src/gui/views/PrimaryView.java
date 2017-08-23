package gui.views;

import gui.controllers.Controller;
import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * This is the primary view which contains other views in its CardLayout such as the MainMenu, InfoView and GameView.
 */
public class PrimaryView extends JComponent implements Observer {

    private static final long serialVersionUID = 1L;
    private Game gameModel;
    private Controller controller;

    private JPanel mainMenu, infoView;
    private GameView gameView;

    private JPanel primaryView;
    private CardLayout currentState;

    public static Dimension PRIMARY_DIMENSION = new Dimension(1600,750);

    public PrimaryView(Game g) {
        gameModel = g;
        gameModel.addObserver(this);
        setFocusable(true);

        controller = new Controller(gameModel, this);

        mainMenu = new MainMenu(controller);
        infoView = new InfoView(controller);
        gameView = new GameView(controller, gameModel);

        JFrame frame = new JFrame("Sword and Shield Game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        currentState = new CardLayout();
        primaryView = new JPanel();
        primaryView.setLayout(currentState);

        primaryView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = e.getComponent().getWidth();
                int height = e.getComponent().getHeight();
                PRIMARY_DIMENSION = new Dimension(width, height);
                gameView.resize();
            }
        });

        primaryView.add(mainMenu, "Main Menu");
        primaryView.add(infoView, "Information");
        primaryView.add(gameView, "Game");

        frame.add(primaryView);
        frame.pack();
        frame.setVisible(true);
    }

    public void showMainMenu(){
        currentState.show(primaryView, "Main Menu");
    }

    public void showInformation(){
        currentState.show(primaryView, "Information");
    }

    public void showGame(){
        currentState.show(primaryView, "Game");
    }

    @Override
    public Dimension getPreferredSize() {
        return PRIMARY_DIMENSION;
    }

    public static int getPrimaryViewWidth(){
        return PRIMARY_DIMENSION.width;
    }

    public static int getPrimaryViewHeight(){
        return PRIMARY_DIMENSION.height;
    }

    @Override
    public void update(Observable o, Object arg) {
        gameView.revalidate();
        revalidate();
        repaint();
    }
}
