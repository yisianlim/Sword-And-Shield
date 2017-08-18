package gui.views;

import gui.Controller;
import model.Game;

import javax.swing.*;
import java.awt.*;

/**
 * This is the primary view which contains other views in its CardLayout such as the MainMenu, InfoView and GameView.
 */
public class PrimaryView extends JComponent {

    private static final long serialVersionUID = 1L;
    private Game gameModel;
    private Controller controller;

    private JPanel mainMenu, infoView, gameView;

    private JPanel primaryView;
    private CardLayout gamePhase;

    public PrimaryView(Game g) {
        gameModel = g;
        controller = new Controller(gameModel, this);

        mainMenu = new MainMenu(controller);
        infoView = new InfoView(controller);
        gameView = new GameView(controller, gameModel);

        this.setFocusable(true);
        JFrame frame = new JFrame("Sword and Shield Game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gamePhase = new CardLayout();
        primaryView = new JPanel();
        primaryView.setLayout(gamePhase);

        primaryView.add(mainMenu, "Main Menu");
        primaryView.add(infoView, "Information");
        primaryView.add(gameView, "Game");

        frame.add(primaryView);
        frame.pack();
        frame.setVisible(true);
    }

    public void showMainMenu(){
        gamePhase.show(primaryView, "Main Menu");
    }

    public void showInformation(){
        gamePhase.show(primaryView, "Information");
    }

    public void showGame(){
        gamePhase.show(primaryView, "Game");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000,1000);
    }
}
