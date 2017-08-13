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

    /**
     * Executes the command and pushing it into the stack in order to undo the command later on, if needed.
     * @param c
     *      Command to be executed.
     */
    public void executeCommand (Command c) {
        c.execute();
        undos.push(c);
    }

    /**
     * Checks if the undo command is available.
     * @return
     *      true if undo is available, false otherwise.
     */
    public boolean isUndoAvailable(){
        return !undos.isEmpty();
    }

    /**
     * Undo the last command executed by the user.
     */
    public void undo(){
        Command command = undos.pop();
        command.undo();
    }
}
