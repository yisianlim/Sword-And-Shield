package gui.views;

import gui.controllers.Controller;
import resources.ImageResources;

import javax.swing.*;
import java.awt.*;

public class InfoView extends JPanel {

    /**
     * Controller for InfoView.
     */
    private Controller controller;

    /**
     * UI elements.
     */
    private JButton back;

    public InfoView(Controller c){
        this.controller = c;
        setSize(new Dimension(1600, PrimaryView.getPrimaryViewHeight()));
        setLayout(new BorderLayout());
        back = new JButton("Back");
        setupInstructions();
    }

    private void setupInstructions(){
        JLabel boardImage = createImage(ImageResources.BOARD_INFO.img, 2);

        JLabel creationImage = createImage(ImageResources.CREATION.img, 4);
        JLabel instructionCreation = createText("Clicking on your PlayerPiece to create will show up all 4 rotations");

        JSplitPane create = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        create.setTopComponent(creationImage);
        create.setBottomComponent(instructionCreation);

        JLabel createdImage = createImage(ImageResources.CREATED.img, 4);
        JLabel instructionCreated = createText("After choosing your rotation, your piece will show up on the board");

        JSplitPane created = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        created.setTopComponent(createdImage);
        created.setBottomComponent(instructionCreated);

        JSplitPane bottomLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        bottomLeft.setLeftComponent(create);
        bottomLeft.setRightComponent(created);

        JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftPane.setTopComponent(boardImage);
        leftPane.setBottomComponent(bottomLeft);

        JLabel selectedPieceImage = createSquareImage(ImageResources.SELECTED.img, 8);
        JLabel instructionMoveSelected = createText(
                "You can move the pieces on the board by selecting the<br>"
                        + "edges of the pieces or pressing any of the WASD key");
        JLabel wasdKeysImage = createImage(ImageResources.KEY.img, 6);

        JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        right.setTopComponent(instructionMoveSelected);
        right.setBottomComponent(wasdKeysImage);

        JSplitPane topRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topRight.setRightComponent(right);
        topRight.setLeftComponent(selectedPieceImage);


        JLabel movedPieceImage = createSquareImage(ImageResources.MOVED.img, 8);
        JLabel instructionMoved = createText("Pieces highlighted red means it has already been moved!");
        JSplitPane bottomRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        bottomRight.setRightComponent(instructionMoved);
        bottomRight.setLeftComponent(movedPieceImage);

        JSplitPane rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightPane.setTopComponent(topRight);
        rightPane.setBottomComponent(bottomRight);

        back.addActionListener(controller);
        add(back, BorderLayout.PAGE_START);
        add(leftPane, BorderLayout.WEST);
        add(rightPane, BorderLayout.CENTER);
    }

    private JLabel createImage(Image image, int scale){
        ImageIcon icon= new ImageIcon(image
                .getScaledInstance(
                        PrimaryView.getPrimaryViewWidth()/scale,
                        PrimaryView.getPrimaryViewHeight()/scale,
                        java.awt.Image.SCALE_SMOOTH)
        );
        return new JLabel("", icon, JLabel.CENTER);
    }

    private JLabel createSquareImage(Image image, int scale){
        ImageIcon icon= new ImageIcon(image
                .getScaledInstance(
                        PrimaryView.getPrimaryViewWidth()/scale,
                        PrimaryView.getPrimaryViewWidth()/scale,
                        java.awt.Image.SCALE_SMOOTH)
        );
        return new JLabel("", icon, JLabel.CENTER);
    }


    private JLabel createText(String text){
        return new JLabel("<html>" + text + "</html>");
    }

    @Override
    public Dimension getPreferredSize() {
        return PrimaryView.primaryDimension;
    }
}
