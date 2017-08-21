package gui.views;

import gui.controllers.Controller;
import model.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * This is the primary view which contains other views in its CardLayout such as the MainMenu, InfoView and GameView.
 */
public class PrimaryView extends JComponent implements Observer {

    private static final long serialVersionUID = 1L;
    private Game gameModel;
    private Controller controller;

    private JPanel mainMenu, infoView, gameView;

    private JPanel primaryView;
    private CardLayout currentState;

    public static final Dimension PRIMARY_DIMENSION = new Dimension(1600,750);

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

    @Override
    public void update(Observable o, Object arg) {
        gameView.revalidate();
        revalidate();
        repaint();
    }
}
