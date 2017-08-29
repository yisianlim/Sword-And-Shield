package gui.views;

import gui.controllers.Controller;
import model.Board;
import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * This is the primary view which contains other views in its CardLayout such as the MainMenu, InfoView and GameView.
 * Using its CardLayout, we can determine what views to show to the user without killing JPanels.
 */
public class PrimaryView extends JComponent implements Observer {

    private static final long serialVersionUID = 1L;

    /**
     * Dimension ratios and constant for each panels.
     */
    public static final double ICON_WIDTH_RATIO = 0.0375;
    public static final double ICON_HEIGHT_RATIO = 0.08;
    public static final double PANEL_WIDTH_RATIO = 0.281;
    public static final double PANEL_HEIGHT_RATIO = 0.4;
    public static final double BOARD_WIDTH_RATIO = 0.375;
    public static final double BOARD_HEIGHT_RATIO = 0.8;
    public static final Dimension MINIMUM_DIMENSION = new Dimension(700,400);

    /**
     * Model of the GUI.
     */
    private static Game gameModel;

    /**
     * Controller of PrimaryView.
     */
    private static Controller controller;

    /**
     * UI elements.
     */
    private MainMenu mainMenu;
    private InfoView infoView;
    private static GameView gameView;
    private static JPanel primaryView;
    private static CardLayout viewCards;

    /**
     * Dimension of this view. Subject to change as the user resizes the panel.
     */
    public static Dimension primaryDimension = new Dimension(1600,750);

    public PrimaryView(Game g) {
        gameModel = g;
        gameModel.addObserver(this);

        controller = new Controller(gameModel);

        mainMenu = new MainMenu(controller);
        infoView = new InfoView(controller);
        gameView = new GameView(controller, gameModel);

        JFrame frame = new JFrame("Sword and Shield Game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        viewCards = new CardLayout();
        primaryView = new JPanel();
        primaryView.setLayout(viewCards);

        primaryView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = e.getComponent().getWidth();
                int height = e.getComponent().getHeight();
                primaryDimension = new Dimension(width, height);;
                gameView.revalidate();
            }
        });

        primaryView.add(mainMenu, "Main Menu");
        primaryView.add(infoView, "Information");
        primaryView.add(gameView, "Game");

        frame.add(primaryView);
        setFocusable(true);
        frame.setMinimumSize(MINIMUM_DIMENSION);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Restart the game by creating a new Game for gameModel and updating the Game constraint
     * in the CardLayout.
     */
    public static void restartGame(){
        // Reset the gameModel to a new game.
        Board board = new Board();
        gameModel.reset(board);

        // Update the gameView inside primaryView CardLayouts.
        controller = new Controller(gameModel);
        gameView = new GameView(controller, gameModel);
        primaryView.remove(gameView);
        primaryView.add(gameView, "Game");

        // Display the main menu.
        viewCards.show(primaryView, "Main Menu");
    }

    /**
     * Display the main menu.
     */
    public static void showMainMenu(){
        viewCards.show(primaryView, "Main Menu");
    }

    /**
     * Display the information.
     */
    public static void showInformation(){
        viewCards.show(primaryView, "Information");
    }

    /**
     * Display the game.
     */
    public static void showGame(){
        viewCards.show(primaryView, "Game");
    }

    /**
     * @return
     *      width of current panel.
     */
    public static int getPrimaryViewWidth(){
        return primaryDimension.width;
    }

    /**
     * @return
     *      height of current panel.
     */
    public static int getPrimaryViewHeight(){
        return primaryDimension.height;
    }

    /**
     * @return
     *      preferred Dimension for the Icon for PlayerPiece and FacePiece.
     */
    public static Dimension getPreferredIconSize(){
        int width = (int) (ICON_WIDTH_RATIO * PrimaryView.getPrimaryViewWidth());
        int height = (int) (ICON_HEIGHT_RATIO * PrimaryView.getPrimaryViewHeight());
        return new Dimension(width, height);
    }

    @Override
    public Dimension getPreferredSize() {
        return primaryDimension;
    }

    @Override
    public void update(Observable o, Object arg) {
        gameView.revalidate();
        revalidate();
        repaint();
    }
}
