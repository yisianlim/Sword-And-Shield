package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by limyisi on 17/08/17.
 */
public class MainMenu extends JPanel{

    JLabel title;
    JButton begin, info, quit;
    Controller controller;

    public MainMenu(Controller c){
        this.controller = c;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        return new Dimension(600,600);
    }
}
