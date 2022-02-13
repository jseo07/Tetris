package ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.Game;

import java.awt.*;
import java.io.IOException;

public class TerminalGame {
    protected Game game;
    protected Screen screen;
    protected WindowBasedTextGUI endGui;

    public void start(String difficulty) throws IOException, InterruptedException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();

        TerminalSize terminalSize = screen.getTerminalSize();

        game = new Game();

        if (difficulty.equals("easy")) {
            game.setTicksPerSecond(2);
            game.setWinScore(200);
        } else if (difficulty.equals("normal")) {
            game.setTicksPerSecond(4);
            game.setWinScore(500);
        } else if (difficulty.equals("hard")) {
            game.setTicksPerSecond(6);
            game.setWinScore(700);
        } else {
            System.out.println("Incorrect difficulty read");
            System.exit(0);
        }

        beginTicks();
    }

    public void beginTicks() throws IOException, InterruptedException {
        while (!game.isEnded() || endGui.getActiveWindow() != null) {
            tick();
            if (game.getTicksPerSecond() == 0) {
                System.exit(0);
            }
            Thread.sleep(1000L / game.getTicksPerSecond());
        }

        System.exit(0);
    }

    public void tick() throws IOException {
        handleUserInput();

        game.tick();

        screen.setCursorPosition(new TerminalPosition(0,0));
        screen.clear();
        render();
        screen.refresh();

        screen.setCursorPosition(new TerminalPosition(screen.getTerminalSize().getColumns() - 1, 0));

        if (game.isWon()) {
            drawWinScreen();
        }

    }

    public void handleUserInput() throws IOException {
        KeyStroke stroke = screen.pollInput();

        if (stroke == null) {
            return;
        }
        if (stroke.getCharacter() != null) {
            return;
        }

        String todo = getNextMove(stroke.getKeyType());

        moveToDir(todo);
    }

    public void moveToDir(String todo) {
        if (todo == null) {
            return;
        } else if (todo == "up") {
            game.rotateShape();
        } else if (todo == "down") {
            game.dropDown();
        } else if (todo == "right") {
            game.moveRight();
            if (game.checkHorizontalCollision() || game.checkForTwo(game.getGrid())) {
                game.moveLeft();
            }
        } else if (todo == "left") {
            game.moveLeft();
            if (game.checkHorizontalCollision() || game.checkForTwo(game.getGrid())) {
                game.moveRight();
            }
        }

        //while (!game.checkHorizontalCollision()) {

    }



    public String getNextMove(KeyType type) {
        switch (type) {
            case ArrowUp:
                return "up";
            case ArrowDown:
                return "down";
            case ArrowRight:
                return "right";
            case ArrowLeft:
                return "left";
            default:
                return null;
        }
    }

    public void render() {
        if (game.isEnded()) {
            if (endGui == null) {
                drawEndScreen();
            }

            return;
        }

        drawScore();
        drawGame();
    }

    public void drawWinScreen() {
        endGui = new MultiWindowTextGUI(screen);

        new MessageDialogBuilder()
                .setTitle("WIN!")
                .setText("You reached the milestone! Challenge yourself with a higher level!")
                .addButton(MessageDialogButton.Close)
                .build()
                .showDialog(endGui);
    }

    public void drawEndScreen() {
        endGui = new MultiWindowTextGUI(screen);

        new MessageDialogBuilder()
                .setTitle("Game Over!")
                .setText("Your block hit the top!")
                .addButton(MessageDialogButton.Close)
                .build()
                .showDialog(endGui);
    }

    public void drawScore() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.GREEN);
        text.putString(1, 0, "Score: ");

        text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.WHITE);
        text.putString(8, 0, String.valueOf(game.getScore()) + "/" + game.getWinScore());

    }

    public void drawGame() {
        int[] pos = new int[2];
        for (int y = game.padding; y < game.verticalSize - game.padding; y++) {
            for (int x = game.padding; x < game.horizontalSize - game.padding; x++) {
                if (game.getGrid()[y][x] == 1) {
                    pos[0] = y;
                    pos[1] = x;
                    drawPosition(pos, colorToTextColor(game.getColorGrid()[y][x]), '█', true);
                }
            }
        }
    }

    public TextColor colorToTextColor(Color color) {
        if (color == Color.CYAN) {
            return TextColor.ANSI.CYAN;
        } else if (color == Color. GREEN) {
            return TextColor.ANSI.GREEN;
        } else if (color == Color. MAGENTA) {
            return TextColor.ANSI.MAGENTA;
        } else if (color == Color. YELLOW) {
            return TextColor.ANSI.YELLOW;
        } else if (color == Color. RED) {
            return TextColor.ANSI.RED;
        } else {
            return TextColor.ANSI.WHITE;
        }
    }

    public void drawPosition(int[] pos, TextColor color, char c, boolean wide) {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(color);
        text.putString(pos[1] * 2, pos[0] + 1, String.valueOf(c));

        if (wide) {
            text.putString(pos[1] * 2 + 1, pos[0] + 1, String.valueOf(c));
        }

    }


}
