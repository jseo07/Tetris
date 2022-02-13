package ui;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner obj = new Scanner(System.in);
        System.out.println("Enter Game Mode (singleplayer or multiplayer): ");
        String gameMode = obj.nextLine();
        System.out.println("Enter Difficulty (easy, normal, or hard):");

        String difficulty = obj.nextLine();
        System.out.println(difficulty + " mode playing...");

        if (gameMode.equals("singleplayer")) {
            TerminalGame gameHandler = new TerminalGame();
            gameHandler.start(difficulty);
        } else if (gameMode.equals("multiplayer")) {
            MultiplayerGame gameHandler = new MultiplayerGame();

            gameHandler.start(difficulty);
        }


    }
}
