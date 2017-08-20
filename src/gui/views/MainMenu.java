package gui.views;

import gui.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the main menu to the user.
 */
public class MainMenu extends JPanel{

    JLabel title;
    JButton begin, info, quit;
    Controller controller;

    public MainMenu(Controller c){
        this.controller = c;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Render components in each rows.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make buttons have the same size.
        gbc.insets = new Insets(5, 0, 5, 0); // Forms a margin between components.

        title = new JLabel("Welcome to Sword and Shield");
        begin = new JButton("Begin");
        info = new JButton("Info");
        quit = new JButton("Quit");

        begin.addActionListener(controller);
        info.addActionListener(controller);
        quit.addActionListener(controller);

        add(title, gbc);
        add(begin, gbc);
        add(info, gbc);
        add(quit, gbc);
    }

    @Override
    public Dimension getPreferredSize() {
        return PrimaryView.PRIMARY_DIMENSION;
    }
}
