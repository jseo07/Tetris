package ui;

import model.Game;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner obj = new Scanner(System.in);
        System.out.println("Enter Difficulty (easy, normal, or hard):");

        String difficulty = obj.nextLine();
        System.out.println(difficulty + " mode playing...");

        TerminalGame gameHandler = new TerminalGame();

        gameHandler.start(difficulty);

    }
}
