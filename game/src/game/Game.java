package game;

import java.util.Scanner;

public abstract class Game {
    private String name;
    Scanner scanner;

    public Game(String name) {
        this.name = name;
        this.scanner = new Scanner(System.in);
    }

    public abstract void play();

    public String getName() {
        return name;
    }
}
