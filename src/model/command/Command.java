package model.command;

/**
 * Command pattern is used to separate the actions performed by user input from user interface code.
 * Separating action code can be used to track every change that happens to the state of a game in order to
 * reverse the changes in the game.
 */
public interface Command {
    public void execute();
    public void undo();
}
