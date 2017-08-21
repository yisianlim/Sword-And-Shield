package gui.views;

import gui.controllers.BoardController;
import gui.controllers.Controller;
import gui.controllers.GreenPanelController;
import gui.controllers.YellowPanelController;
import gui.drawers.ButtonDrawer;
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
    Controller controller;
    GreenPanelController greenPanelController;
    YellowPanelController yellowPanelController;
    BoardController boardController;

    /**
     * Model of the GUI.
     */
    Game gameModel;

    /**
     * JComponents for the GameView.
     */
    JToolBar toolBar;
    JPanel greenCemetery, yellowCemetery;
    JSplitPane entirePane, bottomPane, greenPlayerPane, boardPane, yellowPlayerPane, topPane;

    public GameView(Controller c, Game g) {
        this.controller = c;
        this.greenPanelController = new GreenPanelController(g);
        this.yellowPanelController = new YellowPanelController(g);
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
                        SquareButton.Panel.BOARD)
                        .makeButton();

                if(gameBoard.selectedSquare(squareButton.getPosition())){
                    squareButton.highlight();
                }

                squareButton.addActionListener(boardController);
                board.add(squareButton);
            }
        }
        return board;
    }

    /**
     * Return a JPanel that should be rendered as the top component of the greenPlayerPane.
     * There are two types of panel to be rendered out based on the phase of the game.
     *      - CREATE: Display all the orientations of the selected PlayerPiece to the user.
     *      - Other phases: Display all the PlayerPiece in the creation shelf.
     * @return
     *      Required top JPanel for top component of greenPlayerPane.
     */
    public JPanel greenPanel(){
        Game.Phase phase = gameModel.getGamePhase();

        if(phase.equals(CREATE) && gameModel.getCurrentPlayer().isGreen()){
            // Display all the orientations of the selected PlayerPiece.
            JPanel greenPanelCreateMode = new JPanel();
            greenPanelCreateMode.setLayout(new GridLayout(1,4));
            greenPanelCreateMode.setPreferredSize(new Dimension(450, 300));

            // Get all the selected PlayerPiece with the various orientation.
            List<PlayerPiece> allOrientations = gameModel.getCurrentPlayer().hand.getSelectedInAllOrientations();

            // Create a custom, responsive SquareButton for each PlayerPiece for the panel.
            // Add the SquareButton into JPanel greenPanelCreateMode.
            for(int i = 0; i < 4; i++){
                PlayerPiece current = allOrientations.get(i);
                SquareButton squareButton = new ButtonDrawer(current,
                        new Position(0, i),
                        SquareButton.Panel.TRAINING).makeButton();
                squareButton.addActionListener(greenPanelController);
                greenPanelCreateMode.add(squareButton);
            }
            return greenPanelCreateMode;
        } else {
            // Display all the PlayerPiece available in the creation shelf.
            JPanel greenPanelDisplayMode = new JPanel();
            greenPanelDisplayMode.setPreferredSize(new Dimension(450, 300));
            greenPanelDisplayMode.setLayout(new GridLayout(4, 6, 10, 10));

            // Get all the Piece that the green player currently have on hand.
            Piece[][] hand = gameModel.getGreenPlayer().hand.getArrayRepresentation();

            // Create a custom SquareButton for each piece in hand for the panel.
            // Add the SquareButton into JPanel greenPanelDisplayMode.
            for (int row = 0; row < hand.length; row++) {
                for (int col = 0; col < hand[0].length; col++) {
                    Position currentPosition = new Position(row, col);
                    SquareButton squareButton = new ButtonDrawer(
                            hand[row][col],
                            currentPosition,
                            SquareButton.Panel.CREATION_SHELF)
                            .makeButton();
                    squareButton.addActionListener(greenPanelController);
                    greenPanelDisplayMode.add(squareButton);
                }
            }
            return greenPanelDisplayMode;
        }
    }

    /**
     * Return a JPanel that should be rendered as the top component of the yellowPlayerPane.
     * There are two types of panel to be rendered out based on the phase of the game.
     *      - CREATE: Display all the orientations of the selected PlayerPiece to the user.
     *      - Other phases: Display all the PlayerPiece in the creation shelf.
     * @return
     *      Required top JPanel for top component of yellowPlayerPane.
     */
    private JPanel yellowPanel() {
        Game.Phase phase = gameModel.getGamePhase();
        if(phase.equals(CREATE) && gameModel.getCurrentPlayer().isYellow()){
            // Display all the orientations of the selected PlayerPiece.
            JPanel yellowPanelCreateMode = new JPanel();
            yellowPanelCreateMode.setLayout(new GridLayout(1,4));
            yellowPanelCreateMode.setPreferredSize(new Dimension(450, 300));

            // Get all the selected PlayerPiece with the various orientation.
            List<PlayerPiece> allOrientations = gameModel.getCurrentPlayer().hand.getSelectedInAllOrientations();

            // Create a custom, responsive SquareButton for each PlayerPiece for the panel.
            // Add the SquareButton into JPanel yellowPanelCreateMode.
            for(int i = 0; i < 4; i++){
                PlayerPiece current = allOrientations.get(i);
                SquareButton squareButton = new ButtonDrawer(current,
                        new Position(0, i),
                        SquareButton.Panel.TRAINING).makeButton();
                squareButton.addActionListener(yellowPanelController);
                yellowPanelCreateMode.add(squareButton);
            }
            return yellowPanelCreateMode;
        } else {
            // Display all the PlayerPiece available in the creation shelf.
            JPanel yellowPanelDisplayMode = new JPanel();
            yellowPanelDisplayMode.setPreferredSize(new Dimension(450, 300));
            yellowPanelDisplayMode.setLayout(new GridLayout(4, 6, 10, 10));

            // Get all the Piece that the yellow player currently have on hand.
            Piece[][] hand = gameModel.getYellowPlayer().hand.getArrayRepresentation();

            // Create a custom SquareButton for each piece in hand for the panel.
            // Add the SquareButton into JPanel yellowPanelDisplayMode.
            for (int row = 0; row < hand.length; row++) {
                for (int col = 0; col < hand[0].length; col++) {
                    Position currentPosition = new Position(row, col);
                    SquareButton squareButton = new ButtonDrawer(hand[row][col],
                            currentPosition,
                            SquareButton.Panel.CREATION_SHELF).makeButton();
                    squareButton.addActionListener(yellowPanelController);
                    yellowPanelDisplayMode.add(squareButton);
                }
            }
            return yellowPanelDisplayMode;
        }
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
