package gui;

import javax.swing.*;
import java.awt.*;

public class InfoView extends JPanel {

    Controller controller;
    JLabel info;

    public InfoView(Controller c){
        this.controller = c;
        info = new JLabel("Information stuffs will be here");
        add(info);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,600);
    }
}
