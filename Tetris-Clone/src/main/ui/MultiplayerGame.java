package ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.Game;

import java.io.IOException;

public class MultiplayerGame extends TerminalGame {
    private Game gameOne;
    private Game gameTwo;

    @Override
    public void start(String difficulty) throws IOException, InterruptedException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();

        TerminalSize terminalSize = screen.getTerminalSize();

        gameOne = new Game();
        gameTwo = new Game();

        if (difficulty.equals("easy")) {
            gameOne.setTicksPerSecond(2);
            gameOne.setWinScore(200);

            gameTwo.setTicksPerSecond(2);
            gameTwo.setWinScore(200);

        } else if (difficulty.equals("normal")) {
            gameOne.setTicksPerSecond(4);
            gameOne.setWinScore(500);

            gameTwo.setTicksPerSecond(4);
            gameTwo.setWinScore(500);

        } else if (difficulty.equals("hard")) {
            gameOne.setTicksPerSecond(6);
            gameOne.setWinScore(700);

            gameTwo.setTicksPerSecond(6);
            gameTwo.setWinScore(700);

        } else {
            System.out.println("Incorrect difficulty read");
            System.exit(0);
        }

        beginTicks();
    }

    @Override
    public void tick() throws IOException {
        handleUserInput();

        gameOne.tick();
        gameTwo.tick();

        screen.setCursorPosition(new TerminalPosition(0,0));
        screen.clear();
        render();
        screen.refresh();

        screen.setCursorPosition(new TerminalPosition(screen.getTerminalSize().getColumns() - 1, 0));

        if (gameOne.isWon()) {
            drawWinScreen("Player 1 Victorious!");
        } else if (gameTwo.isWon()) {
            drawWinScreen("Player 2 Victorious");
        }

    }

    @Override
    public void beginTicks() throws IOException, InterruptedException {
        while (!gameOne.isEnded() || !gameTwo.isEnded() || endGui.getActiveWindow() != null) {
            tick();
            if (gameOne.getTicksPerSecond() == 0 || gameTwo.getTicksPerSecond() == 0) {
                System.exit(0);
            }
            Thread.sleep(1000L / gameOne.getTicksPerSecond());
        }

        System.exit(0);
    }

    @Override
    public void moveToDir(String todo) {
        if (todo == null) {
            return;
        } else if (todo == "upLeft") {
            gameOne.rotateShape();
        } else if (todo == "downLeft") {
            gameOne.dropDown();
        } else if (todo == "rightLeft") {
            gameOne.moveRight();
            if (gameOne.checkHorizontalCollision() || gameOne.checkForTwo(gameOne.getGrid())) {
                gameOne.moveLeft();
            }
        } else if (todo == "leftLeft") {
            gameOne.moveLeft();
            if (gameOne.checkHorizontalCollision() || gameOne.checkForTwo(gameOne.getGrid())) {
                gameOne.moveRight();
            }
        } else if (todo == "upRight") {
            gameTwo.rotateShape();
        } else if (todo == "downRight") {
            gameTwo.dropDown();
        } else if (todo == "rightRight") {
            gameTwo.moveRight();
            if (gameTwo.checkHorizontalCollision() || gameTwo.checkForTwo(gameTwo.getGrid())) {
                gameTwo.moveLeft();
            }
        } else if (todo == "leftRight") {
            gameTwo.moveLeft();
            if (gameTwo.checkHorizontalCollision() || gameTwo.checkForTwo(gameTwo.getGrid())) {
                gameTwo.moveRight();
            }
        }
    }

    @Override
    public String getNextMove(KeyType type) {
        switch (type) {
            case ArrowUp:
                return "upLeft";
            case ArrowDown:
                return "downLeft";
            case ArrowRight:
                return "rightLeft";
            case ArrowLeft:
                return "leftLeft";
            case PageUp:
                return "upRight";
            case PageDown:
                return "downRight";
            case Home:
                return "rightRight";
            case End:
                return "leftRight";
            default:
                return null;
        }
    }

    @Override
    public void render() {
        if (gameOne.isEnded()) {
            if (endGui == null) {
                drawEndScreen("Player 2 Victorious!");
            }

            return;
        } else if (gameTwo.isEnded()) {
            if (endGui == null) {
                drawEndScreen("Player 1 Victorious!");
            }
        }

        drawScoreOne();
        drawScoreTwo();
        drawGameOne();
        drawGameTwo();

        drawLine();
    }

    public void drawLine() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.WHITE);

        for (int i = 3; i < 25; i++){
            text.putString(5, i, "|");
            text.putString(26, i, "|");

            text.putString(35, i, "|");
            text.putString(56, i, "|");

        }
    }

    public void drawWinScreen(String msg) {
        endGui = new MultiWindowTextGUI(screen);

        new MessageDialogBuilder()
                .setTitle("WIN!")
                .setText(msg)
                .addButton(MessageDialogButton.Close)
                .build()
                .showDialog(endGui);
    }

    public void drawEndScreen(String msg) {
        endGui = new MultiWindowTextGUI(screen);

        new MessageDialogBuilder()
                .setTitle("Game Over!")
                .setText("Player 2 Victorious!")
                .addButton(MessageDialogButton.Close)
                .build()
                .showDialog(endGui);
    }

    public void drawScoreOne() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.GREEN);
        text.putString(1, 0, "PLAYER 1 Score: ");

        text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.WHITE);
        text.putString(17, 0, String.valueOf(gameOne.getScore()) + "/" + gameOne.getWinScore());
    }

    public void drawScoreTwo() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.GREEN);
        text.putString(1 + 30, 0, "PLAYER 2 Score: ");

        text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.WHITE);
        text.putString(17 + 30, 0, String.valueOf(gameTwo.getScore()) + "/" + gameTwo.getWinScore());

    }

    public void drawGameOne() {
        int[] pos = new int[2];
        for (int y = gameOne.padding; y < gameOne.verticalSize - gameOne.padding; y++) {
            for (int x = gameOne.padding; x < gameOne.horizontalSize - gameOne.padding; x++) {
                if (gameOne.getGrid()[y][x] == 1) {
                    pos[0] = y;
                    pos[1] = x;
                    drawPosition(pos, colorToTextColor(gameOne.getColorGrid()[y][x]), '█', true);
                }
            }
        }
    }

    public void drawGameTwo() {
        int[] pos = new int[2];
        for (int y = gameTwo.padding; y < gameTwo.verticalSize - gameTwo.padding; y++) {
            for (int x = gameTwo.padding; x < gameTwo.horizontalSize - gameTwo.padding; x++) {
                if (gameTwo.getGrid()[y][x] == 1) {
                    pos[0] = y;
                    pos[1] = x + 15;
                    drawPosition(pos, colorToTextColor(gameTwo.getColorGrid()[y][x]), '█', true);
                }
            }
        }
    }

}
