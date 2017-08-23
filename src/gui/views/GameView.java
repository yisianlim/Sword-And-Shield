package gui.views;

import gui.controllers.BoardController;
import gui.controllers.Controller;
import gui.controllers.PlayerPanelController;
import gui.drawers.*;
import model.Board;
import model.Game;
import model.Position;
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
    private JPanel board, greenPanel, yellowPanel, greenCemetery, yellowCemetery;
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
    public void setupToolbar(){
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
     * Modify the JPanel containing the createBoard based on the state of the Board in gameModel.
     * Return the modified JPanel of createBoard to be rendered.
     * @return
     *      modified JPanel of createBoard.
     */
    public JPanel createBoard(){
        BoardDrawer board = new BoardDrawer(gameModel, this);
        return board.createBoard();
    }

    /**
     * createGreenPanel returns the JPanel for GreenPlayer at the left.
     * @return
     *      Required top JPanel for top component of greenPlayerPane.
     */
    public JPanel createGreenPanel(){
        PlayerPanelDrawer green = new PlayerPanelDrawer(gameModel.getGreenPlayer(), gameModel, this);
        return green.createPanel();
    }

    /**
     * createYellowPanel returns the JPanel for YellowPlayer at the right.
     * @return
     *      Required top JPanel for top component of yellowPlayerPane.
     */
    private JPanel createYellowPanel() {
        PlayerPanelDrawer yellow = new PlayerPanelDrawer(gameModel.getYellowPlayer(), gameModel, this);
        return yellow.createPanel();
    }

    private JPanel createGreenCemetery() {
        CemeteryDrawer green = new CemeteryDrawer(gameModel.getGreenPlayer(), gameModel);
        return green.createCemetery();
    }

    private JPanel createYellowCemetery() {
        CemeteryDrawer yellow = new CemeteryDrawer(gameModel.getYellowPlayer(), gameModel);
        return yellow.createCemetery();
    }

    @Override
    public Dimension getPreferredSize() {
        return PrimaryView.primaryDimension;
    }

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
        if(gameModel.getWarnings() > 0)
            beep(SoundResources.Sound.WARNING);

        topPane.setBottomComponent(status());

        leftPane.setTopComponent(greenPanel);
        leftPane.setBottomComponent(greenCemetery);

        middlePane.setTopComponent(topPane);
        middlePane.setBottomComponent(board);

        rightPane.setTopComponent(yellowPanel);
        rightPane.setBottomComponent(yellowCemetery);

    }

    @Override
    public void revalidate() {
        if(gameModel == null) return;
        createPanels();
        update();
    }

    public void beep(SoundResources.Sound sound){
        new SoundResources(sound);
    }
}
