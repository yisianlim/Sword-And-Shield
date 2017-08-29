package gui.views;

import gui.controllers.BoardController;
import gui.controllers.Controller;
import gui.controllers.PlayerPanelController;
import gui.drawers.BoardDrawer;
import gui.drawers.CemeteryDrawer;
import gui.drawers.Dialogs;
import gui.drawers.PlayerPanelDrawer;
import model.Game;
import resources.SoundResources;

import javax.swing.*;
import java.awt.*;

/**
 * GameView renders the state of the game to the user.
 */
public class GameView extends JPanel {

    /**
     * Controller of the GUI.
     */
    public Controller controller;
    public PlayerPanelController playerPanelController;
    public BoardController boardController;

    /**
     * Model of the GUI.
     */
    private Game gameModel;

    /**
     * UI elements
     */
    private JToolBar toolbar;
    private JComponent greenPanel, yellowPanel, board;
    private JPanel greenCemetery, yellowCemetery;
    private JSplitPane leftPane, rightPane, middlePane, topPane;

    public GameView(Controller c, Game g) {
        this.controller = c;
        this.playerPanelController = new PlayerPanelController(g);
        this.boardController = new BoardController(g);
        this.gameModel = g;

        setupToolbar();
        createPanels();

        topPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        topPane.setTopComponent(toolbar);
        topPane.setBottomComponent(status());

        leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftPane.setTopComponent(greenPanel);
        leftPane.setBottomComponent(greenCemetery);

        middlePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        middlePane.setTopComponent(topPane);
        middlePane.setBottomComponent(board);

        rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightPane.setTopComponent(yellowPanel);
        rightPane.setBottomComponent(yellowCemetery);

        add(leftPane, BorderLayout.WEST);
        add(middlePane, BorderLayout.CENTER);
        add(rightPane, BorderLayout.EAST);

        setFocusable(true);
    }

    /**
     * Modify the JLabel based on the status of the game. Return the
     * modified JLabel that is to be shown to the user.
     * @return
     *      Modified JLabel.
     */
    private JLabel status(){
        return new JLabel(gameModel.getStatus());
    }

    /**
     * Setup the JToolbar before adding it to GameView to be rendered out to the user.
     */
    private void setupToolbar(){
        toolbar = new JToolBar();

        // Setup buttons.
        JButton pass = new JButton("Pass");
        JButton undo = new JButton("Undo");
        JButton surrender = new JButton("Surrender");
        JButton back = new JButton("Back");

        // Allow buttons to respond.
        pass.addActionListener(controller);
        undo.addActionListener(controller);
        surrender.addActionListener(controller);
        back.addActionListener(controller);

        // Add the buttons into the toolbar.
        toolbar.add(back);
        toolbar.add(pass);
        toolbar.add(undo);
        toolbar.add(surrender);
    }

    /**
     * Modify the JPanel containing the createDisplayBoard based on the state of the Board in gameModel.
     * Return the modified JPanel of createDisplayBoard to be rendered.
     * @return
     *      modified JPanel of createDisplayBoard.
     */
    private JComponent createBoard(){
        BoardDrawer board = new BoardDrawer(gameModel, this);
        return board.createBoard();
    }

    /**
     * createGreenPanel returns the JPanel for GreenPlayer at the left.
     * @return
     *      Required top JPanel for top component of greenPlayerPane.
     */
    private JComponent createGreenPanel(){
        PlayerPanelDrawer green = new PlayerPanelDrawer(gameModel.getGreenPlayer(), gameModel, this);
        return green.createPanel();
    }

    /**
     * createYellowPanel returns the JPanel for YellowPlayer at the right.
     * @return
     *      Required top JPanel for top component of yellowPlayerPane.
     */
    private JComponent createYellowPanel() {
        PlayerPanelDrawer yellow = new PlayerPanelDrawer(gameModel.getYellowPlayer(), gameModel, this);
        return yellow.createPanel();
    }

    /**
     * createGreenCemetery returns the JPanel for GreenPlayer's cemetery.
     * @return
     *      Required JPanel for the green player.
     */
    private JPanel createGreenCemetery() {
        CemeteryDrawer green = new CemeteryDrawer(gameModel.getGreenPlayer(), gameModel);
        return green.createCemetery();
    }

    /**
     * createYellowCemetery returns the JPanel for YellowPlayer's cemetery.
     * @return
     *      Required JPanel for the yellow player.
     */
    private JPanel createYellowCemetery() {
        CemeteryDrawer yellow = new CemeteryDrawer(gameModel.getYellowPlayer(), gameModel);
        return yellow.createCemetery();
    }

    /**
     * Initialise all of the panels in GameView.
     */
    private void createPanels(){
        this.board = createBoard();
        this.greenPanel = createGreenPanel();
        this.yellowPanel = createYellowPanel();
        this.yellowCemetery = createYellowCemetery();
        this.greenCemetery = createGreenCemetery();
    }

    /**
     * Update the components in GameView after a change in the state of gameModel have been modified.
     */
    private void update(){
        // Warn player if needed.
        warnPlayer();

        topPane.setBottomComponent(status());

        leftPane.setTopComponent(greenPanel);
        leftPane.setBottomComponent(greenCemetery);

        middlePane.setTopComponent(topPane);
        middlePane.setBottomComponent(board);

        rightPane.setTopComponent(yellowPanel);
        rightPane.setBottomComponent(yellowCemetery);

    }

    /**
     * warnPlayer warns the player in the event when they attempts to click on their creation
     * shelf multiple times when their creation grid is already occupied.
     */
    private void warnPlayer() {
        if(gameModel.getWarnings() == 1)
            beep(SoundResources.Sound.WARNING);

        if(gameModel.getWarnings() == 2)
            beep(SoundResources.Sound.SECOND_WARNING);

        if(gameModel.getWarnings() >= 3){
            Dialogs.creationErrorDialog("Your creation grid is occupied!\n" +
                    "Hint: Pass your creation phase");
        }
    }

    /**
     * beep method loads the sound that needs to be played during the game.
     * @param sound
     *          SoundResources of the sound that is to be played.
     */
    private void beep(SoundResources.Sound sound){
        new SoundResources(sound);
    }

    @Override
    public void revalidate() {
        if(gameModel == null) return;
        createPanels();
        update();
    }

    @Override
    public Dimension getPreferredSize() {
        return PrimaryView.primaryDimension;
    }
}
