package gui.views;

import gui.controllers.ButtonController;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the main menu to the user.
 */
public class MainMenu extends JPanel{

    /**
     * ButtonController of MainMenu view.
     */
    private ButtonController buttonController;

    /**
     * UI elements.
     */
    private JLabel title;
    private JButton begin, info, quit;

    public MainMenu(ButtonController c){
        this.buttonController = c;

        // Setup the layouts.
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Render components in each rows.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make buttons have the same size.
        gbc.insets = new Insets(5, 0, 5, 0); // Forms a margin between components.

        // Create buttons.
        title = new JLabel("Welcome to Sword and Shield");
        begin = new JButton("Begin");
        info = new JButton("Info");
        quit = new JButton("Quit");

        // Add controllers.
        begin.addActionListener(buttonController);
        info.addActionListener(buttonController);
        quit.addActionListener(buttonController);

        // Add buttons to this JPanel.
        add(title, gbc);
        add(begin, gbc);
        add(info, gbc);
        add(quit, gbc);
    }

    @Override
    public Dimension getPreferredSize() {
        return PrimaryView.primaryDimension;
    }
}
