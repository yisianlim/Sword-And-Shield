package model.command;

import java.util.Stack;

/**
 * Responsible for tracking, executing and undoing Command implementations.
 */
public class CommandManager {

    private Stack<Command> undos;

    public CommandManager(){
        this.undos = new Stack<>();
    }

    public void executeCommand (Command c) {
        c.execute();
        undos.push(c);
    }

    public boolean isUndoAvailable(){
        return !undos.isEmpty();
    }

    public void undo(){
        Command command = undos.pop();
        command.undo();
    }
}