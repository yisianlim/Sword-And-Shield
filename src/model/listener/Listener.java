package model.listener;

import model.Game;

import java.util.Scanner;

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
