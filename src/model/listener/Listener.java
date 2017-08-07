package model.listener;

import model.Game;

import java.util.Scanner;

/**
 * The Listener class and its subclasses listens to the user input and execute the respective command
 * that changes the game state accordingly.
 */
public abstract class Listener {
    protected Scanner scanner;
    protected Game game;

    public Listener(Scanner scanner, Game game){
        this.scanner = scanner;
        this.game = game;
    }

    public boolean isCreate(){
        return scanner.hasNext("create");
    }

    public boolean isPass(){
        return scanner.hasNext("pass");
    }

    public boolean isRotate(){
        return scanner.hasNext("rotate");
    }

    public boolean isMove(){
        return scanner.hasNext("move");
    }

    public boolean hasNext(String input){
        return scanner.hasNext(input);
    }

    public abstract boolean parse();
}
