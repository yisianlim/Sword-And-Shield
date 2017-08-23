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

    public static Dimension primaryDimension = new Dimension(1600,750);

    public static final double ICON_WIDTH_RATIO = 0.0375;
    public static final double ICON_HEIGHT_RATIO = 0.08;
    public static final double PANEL_WIDTH_RATIO = 0.281;
    public static final double PANEL_HEIGHT_RATIO = 0.4;
    public static final double BOARD_WIDTH_RATIO = 0.375;
    public static final double BOARD_HEIGHT_RATIO = 0.8;

    public PrimaryView(Game g) {
        gameModel = g;
        gameModel.addObserver(this);


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
                primaryDimension = new Dimension(width, height);
                gameView.revalidate();
            }
        });

        primaryView.add(mainMenu, "Main Menu");
        primaryView.add(infoView, "Information");
        primaryView.add(gameView, "Game");

        frame.add(primaryView);
        setFocusable(true);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
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
        return primaryDimension;
    }

    public static int getPrimaryViewWidth(){
        return primaryDimension.width;
    }

    public static int getPrimaryViewHeight(){
        return primaryDimension.height;
    }

    public static Dimension getPreferredIconSize(){
        int width = (int) (ICON_WIDTH_RATIO * PrimaryView.getPrimaryViewWidth());
        int height = (int) (ICON_HEIGHT_RATIO * PrimaryView.getPrimaryViewHeight());
        return new Dimension(width, height);
    }

    @Override
    public void update(Observable o, Object arg) {
        gameView.revalidate();
        revalidate();
        repaint();
    }
}
