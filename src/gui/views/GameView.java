package gui.views;

import gui.controllers.BoardController;
import gui.controllers.Controller;
import gui.controllers.PlayerPanelController;
import gui.drawers.ButtonDrawer;
import gui.drawers.PlayerPanelDrawer;
import gui.drawers.SquareButton;
import model.Board;
import model.Game;
import model.Position;
import model.piece.Piece;
import model.piece.PlayerPiece;
import resources.SoundResources;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static model.Game.Phase.*;

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
    private JToolBar toolBar;
    private JPanel greenCemetery, yellowCemetery;
    private JSplitPane entirePane, bottomPane, greenPlayerPane, boardPane, yellowPlayerPane, topPane;

    public GameView(Controller c, Game g) {
        this.controller = c;
        this.playerPanelController = new PlayerPanelController(g);
        this.boardController = new BoardController(g);
        this.gameModel = g;

        setupToolbar();
        drawGreenCemetery();
        drawYellowCemetery();

        entirePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        add(entirePane, BorderLayout.CENTER);

        bottomPane = new JSplitPane();
        entirePane.setRightComponent(bottomPane);

        greenPlayerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        greenPlayerPane.setTopComponent(greenPanel());
        greenPlayerPane.setBottomComponent(greenCemetery);
        bottomPane.setLeftComponent(greenPlayerPane);

        boardPane = new JSplitPane();
        boardPane.setLeftComponent(board());
        bottomPane.setRightComponent(boardPane);

        yellowPlayerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        yellowPlayerPane.setTopComponent(yellowPanel());
        yellowPlayerPane.setBottomComponent(yellowCemetery);
        boardPane.setRightComponent(yellowPlayerPane);

        topPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        topPane.setTopComponent(toolBar);
        topPane.setBottomComponent(status());
        entirePane.setLeftComponent(topPane);
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
        toolBar = new JToolBar();

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
        toolBar.add(back);
        toolBar.add(pass);
        toolBar.add(undo);
        toolBar.add(surrender);
    }

    /**
     * Modify the JPanel containing the board based on the state of the Board in gameModel.
     * Return the modified JPanel of board to be rendered.
     * @return
     *      modified JPanel of board.
     */
    public JPanel board(){
        JPanel board = new JPanel();
        board.setPreferredSize(new Dimension(600,600));
        board.setLayout(new GridLayout(10,10));
        Board gameBoard = gameModel.getBoard();

        // Create a custom, responsive SquareButton for each Piece in gameBoard.
        // Add the SquareButton into JPanel board.
        for(int row = 0; row < gameBoard.ROWS; row++){
            for(int col = 0; col < gameBoard.COLS; col++){
                Position currentPosition = new Position(row, col);
                SquareButton squareButton = new ButtonDrawer(
                        gameBoard.getSquare(currentPosition),
                        currentPosition,
                        SquareButton.Panel.BOARD_DISPLAY)
                        .makeButton();

                if(gameBoard.selectedSquare(squareButton.getPosition())){
                    squareButton.setPanelType(SquareButton.Panel.BOARD_SELECTED);
                    squareButton.highlight();
                    squareButton.addMouseListener(boardController);
                }

                squareButton.addActionListener(boardController);
                board.add(squareButton);
            }
        }
        return board;
    }

    /**
     * greenPanel returns the JPanel for GreenPlayer at the left.
     * @return
     *      Required top JPanel for top component of greenPlayerPane.
     */
    public JPanel greenPanel(){
        PlayerPanelDrawer green = new PlayerPanelDrawer(gameModel.getGreenPlayer(), gameModel, this);
        return green.createPanel();
    }

    /**
     * yellowPanel returns the JPanel for YellowPlayer at the right.
     * @return
     *      Required top JPanel for top component of yellowPlayerPane.
     */
    private JPanel yellowPanel() {
        PlayerPanelDrawer yellow = new PlayerPanelDrawer(gameModel.getYellowPlayer(), gameModel, this);
        return yellow.createPanel();
    }

    private void drawGreenCemetery() {
        greenCemetery = new JPanel();
        greenCemetery.setPreferredSize(new Dimension(450, 300));
        greenCemetery.setLayout(new GridLayout(4, 6, 10,10));
        Piece[][] cemetery = gameModel.getCemetery().getGreenPiecesInCemetery();
        for(int row = 0; row < cemetery.length; row++){
            for(int col = 0; col < cemetery[0].length; col++){
                Position currentPosition = new Position(row, col);
                SquareButton squareButton = new ButtonDrawer(cemetery[row][col],
                        currentPosition,
                        SquareButton.Panel.CEMETERY).makeButton();
                greenCemetery.add(squareButton);
            }
        }
    }

    private void drawYellowCemetery() {
        yellowCemetery = new JPanel();
        yellowCemetery.setPreferredSize(new Dimension(450, 300));
        yellowCemetery.setLayout(new GridLayout(4, 6, 10,10));
        Piece[][] cemetery = gameModel.getCemetery().getYellowPiecesInCemetery();
        for(int row = 0; row < cemetery.length; row++){
            for(int col = 0; col < cemetery[0].length; col++){
                Position currentPosition = new Position(row, col);
                SquareButton squareButton = new ButtonDrawer(cemetery[row][col],
                        currentPosition,
                        SquareButton.Panel.CEMETERY).makeButton();
                yellowCemetery.add(squareButton);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return PrimaryView.PRIMARY_DIMENSION;
    }

    /**
     * Update the components in GameView after a change in the state of gameModel have been modified.
     */
    private void update(){
        if(gameModel.getWarnings() > 0)
            beep(SoundResources.Sound.WARNING);

        greenPlayerPane.setTopComponent(greenPanel());
        yellowPlayerPane.setTopComponent(yellowPanel());
        topPane.setBottomComponent(status());
        boardPane.setLeftComponent(board());
    }

    @Override
    public void revalidate() {
        if(gameModel == null) return;
        update();
    }

    public void beep(SoundResources.Sound sound){
        new SoundResources(sound);
    }
}
