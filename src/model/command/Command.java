package model.command;

/**
 * Command pattern is used to separate the actions performed by user input from user interface code.
 * Separating action code can be used to track every change that happens to the state of a game in order to
 * reverse the changes in the game.
 */
public interface Command {
    public void execute();

    /**
     * Undo the command. It restores the current game state to the previous game state resulting in the command
     * that was executed to be undone.
     */
    public void undo();
}
