package gui.controllers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * FunctionalAction acts as a workaround so that AbstractAction can be created using a
 * lambda expression in the BoardController as it is not plausible to make a lambda
 * expression to target an abstract class.
 */
public class FunctionalAction extends AbstractAction {

    private ActionListener actionListener;

    public FunctionalAction(ActionListener actionListener){
        this.actionListener = actionListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionListener.actionPerformed(e);
    }
}
