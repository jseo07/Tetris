package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game testGame;
    Shape testShape;

    @BeforeEach
    void runBefore() {
        testGame = new Game();
        testShape = new Shape();
    }

    @Test
    void testConstructor() {
        for (int i = 0; i < testGame.verticalSize; i++) {
            for (int j = 0; j < testGame.horizontalSize; j++) {
                assertEquals(0, testGame.getGrid()[i][j]);
                assertEquals(null, testGame.getColorGrid()[i][j]);
            }
        }
        assertEquals(0, testGame.getScore());
    }

    @Test
    void testTick() {
        testMoveDown();
        int[] temp = testGame.getGrid()[10].clone();
        Color[] tempColors = testGame.getColorGrid()[10].clone();

        testGame.tick();

        for (int x = 0; x < testGame.horizontalSize; x++) {
            assertEquals(temp[x], testGame.getGrid()[10][x]);
            assertEquals(tempColors[x], testGame.getColorGrid()[10][x]);
        }

        testCheckForTwo();
        testCheckBottomCollision();
    }

    @Test
    void testTickIfStatement() {
        testGame.addShape(testShape);
        testGame.setShapeInMove(testShape);

        testGame.getGrid()[0][0] = 2;

        int[][] temp = testGame.getGrid().clone();
        Color[][] tempColors = testGame.getColorGrid().clone();

        testGame.tick();

        assertTrue(temp != testGame.getGrid());
        assertTrue(tempColors != testGame.getColorGrid());

    }

    @Test
    void testAddShape() {
        testGame.addShape(testShape);
        assertTrue(checkForOne(testGame.getGrid()));

        int[] temp = new int[2];

        testShape.setCoordinates(temp);

        testGame.addShape(testShape);
        assertTrue(checkForOne(testGame.getGrid()));

        temp[1] = testGame.horizontalSize - 1;
        testShape.setCoordinates(temp);
        testGame.addShape(testShape);
        assertTrue(checkForOne(testGame.getGrid()));

        temp[0] = testGame.verticalSize - 1;
        testShape.setCoordinates(temp);
        testGame.addShape(testShape);
        assertTrue(checkForOne(testGame.getGrid()));

    }

    @Test
    void testRemoveShape() {
        testGame.addShape(testShape);
        testGame.removeShape(testShape);

        assertFalse(checkForOne(testGame.getGrid()));

        int[] temp = new int[2];

        testShape.setCoordinates(temp);

        testGame.removeShape(testShape);
        assertFalse(checkForOne(testGame.getGrid()));

        temp[1] = testGame.horizontalSize - 1;
        testShape.setCoordinates(temp);
        testGame.removeShape(testShape);
        assertFalse(checkForOne(testGame.getGrid()));

        temp[0] = testGame.verticalSize - 1;
        testShape.setCoordinates(temp);
        testGame.removeShape(testShape);
        assertFalse(checkForOne(testGame.getGrid()));
    }

    @Test
    void testCheckLine() {
        testGame.getGrid()[5] = new int[]{0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0};

        testGame.checkLine();

        testClearLines();
    }

    @Test
    void testClearLines() {
        testGame.addShape(testShape);
        ArrayList<Integer> temp = new ArrayList<Integer>();
        temp.add(5);
        testGame.clearLines(temp);

        testRemoveRow();

    }

    @Test
    void testRemoveRow() {
        testGame.addShape(testShape);
        int[] tempGrid = testGame.getGrid()[5 - 1].clone();
        testGame.removeRow(5);

        for (int x = 0; x < testGame.horizontalSize; x++) {
            assertEquals(tempGrid[x], testGame.getGrid()[5][x]);
        }

    }

    @Test
    void testIsEnded() {
        testCheckForTwo();

        testGame.getGrid()[0][10] = 1;

        assertTrue(testGame.isEnded());

        testGame.getGrid()[0][10] = 0;

        assertFalse(testGame.isEnded());
    }

    @Test
    void testIsWon() {
        testGame.setScore(10000);

        assertTrue(testGame.isWon());

        testGame.setScore(1);
        testGame.setWinScore(10000);

        assertFalse(testGame.isWon());
    }

    @Test
    void testCheckHorizontalCollision() {
        testGame.getGrid()[10][1] = 1;
        assertTrue(testGame.checkHorizontalCollision());

        testGame.getGrid()[10][testGame.horizontalSize - 1] = 1;
        testGame.getGrid()[10][1] = 0;
        assertTrue(testGame.checkHorizontalCollision());

        testGame.getGrid()[10][testGame.horizontalSize - 1] = 0;
        testGame.addShape(testShape);
        assertFalse(testGame.checkHorizontalCollision());


    }

    @Test
    void testCheckBottomCollision(){
        testGame.getGrid()[testGame.verticalSize - 1][10] = 1;
        assertTrue(testGame.checkBottomCollision());

        testGame.getGrid()[testGame.verticalSize - 1][10] = 0;
        assertFalse(testGame.checkBottomCollision());
    }

    @Test
    void testCheckForTwo() {
        testGame.getGrid()[10][10] = 2;

        assertTrue(testGame.checkForTwo(testGame.getGrid()));

        testGame.getGrid()[10][10] -= 2;

        assertFalse(testGame.checkForTwo(testGame.getGrid()));

    }

    @Test
    void testMoveRight() {
        testGame.addShape(testShape);
        int[] temp = new int[testGame.verticalSize];
        for (int i = 0; i < testGame.verticalSize; i++) {
            temp[i] = testGame.getGrid()[i][8];
        }
        testGame.setShapeInMove(testShape);

        testGame.moveRight();

        for (int y = 0; y < testGame.verticalSize; y++) {
            assertEquals(temp[y], testGame.getGrid()[y][9]);
        }
    }

    @Test
    void testMoveLeft() {
        testGame.addShape(testShape);
        int[] temp = new int[testGame.verticalSize];
        for (int i = 0; i < testGame.verticalSize; i++) {
            temp[i] = testGame.getGrid()[i][8];
        }
        testGame.setShapeInMove(testShape);

        testGame.moveLeft();

        for (int y = 0; y < testGame.verticalSize; y++) {
            assertEquals(temp[y], testGame.getGrid()[y][7]);
        }

    }

    @Test
    void testMoveDown() {
        testGame.addShape(testShape);
        int[] temp = new int[testGame.horizontalSize];
        for (int i = 0; i < testGame.horizontalSize; i++) {
            temp[i] = testGame.getGrid()[5][i];
        }
        testGame.setShapeInMove(testShape);

        testGame.moveDown();

        for (int x = 0; x < testGame.horizontalSize; x++) {
            assertEquals(temp[x], testGame.getGrid()[6][x]);
        }
    }


    @Test
    void testRotateShape() {
        testGame.addShape(testShape);
        int[] temp = testShape.getShapeType()[0];
        testGame.rotateShape();
        for (int i = 0; i < 4; i++) {
            assertEquals(temp[i], testShape.getShapeType()[i][3]);
        }
    }

    @Test
    void testDropDown() {
        testGame.dropDown();
        Boolean booleanGrid = false;
        Boolean booleanColor = false;
        for (int x = 0; x < testGame.horizontalSize; x++) {
            if (testGame.getGrid()[testGame.verticalSize - testGame.padding - 1][x] == 1) {
                booleanGrid = true;
            }
            if (testGame.getColorGrid()[testGame.verticalSize - testGame.padding - 1][x] != null) {
                booleanColor = true;
            }
        }

        assertTrue(booleanGrid);
        assertTrue(booleanColor);

        testGame.getGrid()[testGame.verticalSize - 1][10] = 1;
        int[][] tempGrid = testGame.getGrid().clone();
        Color[][] tempColorGrid = testGame.getColorGrid().clone();

        testGame.dropDown();

        assertTrue(tempGrid != testGame.getGrid());
        assertTrue(tempColorGrid != testGame.getColorGrid());

        testCheckForTwo();
        testCheckBottomCollision();

    }

    @Test
    void TestGetTicksPerSecond() {
        testGame.setTicksPerSecond(5);
        assertEquals(5, testGame.getTicksPerSecond());

        testGame.setTicksPerSecond(2);
        assertEquals(2, testGame.getTicksPerSecond());
    }

    @Test
    void TestGetWinScore() {
        testGame.setWinScore(500);
        assertEquals(500, testGame.getWinScore());

        testGame.setWinScore(200);
        assertEquals(200, testGame.getWinScore());
    }


    // Test Helper:
    // EFFECTS: Checks if there is a one in any coordinates of the grid
    private Boolean checkForOne(int[][] grid) {
        Boolean result = false;
        for (int i = 0; i < testGame.verticalSize; i++) {
            for (int j = 0; j < testGame.horizontalSize; j++) {
                if (grid[i][j] == 1) {
                    result = true;
                }
            }
        }
        return result;
    }


}