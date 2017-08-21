package gui.views;

import gui.controllers.Controller;

import javax.swing.*;
import java.awt.*;

public class InfoView extends JPanel {

    Controller controller;
    JLabel info;
    JButton back;

    public InfoView(Controller c){
        this.controller = c;
        setLayout(new BorderLayout());
        back = new JButton("Back");
        info = new JLabel("Information stuffs will be here");

        back.addActionListener(controller);

        add(back, BorderLayout.PAGE_START);
        add(info, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return PrimaryView.PRIMARY_DIMENSION;
    }
}
