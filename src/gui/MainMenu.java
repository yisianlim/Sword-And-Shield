package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by limyisi on 17/08/17.
 */
public class MainMenu extends JPanel{

    JLabel title;
    JButton begin, info, quit;

    public MainMenu(){
        setLayout(new ); //PAGE_AXIS
        title = new JLabel("Welcome to Sword and Shield");
        begin = new JButton("Begin");
        info = new JButton("Info");
        quit = new JButton("Quit");
        alignComponents();
        add(title);
        add(begin);
        add(info);
        add(quit);

    }

    private void alignComponents() {
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setAlignmentY(Component.CENTER_ALIGNMENT);
        begin.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        quit.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,600);
    }
}
