package model;

import java.awt.*;
import java.util.ArrayList;

public class Game {

    public static final int padding = 3;
    public static final int POINTS_PER_CLEAR = 100;

    public final int horizontalSize = 16;
    public final int verticalSize = 26;

    private int[][] grid;
    private Color[][] colorGrid;

    private Shape shapeInMove;

    private int score = 0;
    private int ticksPerSecond;
    private int winScore;

    // EFFECTS: Create a new game with a grid of all 0s, empty initial color grid,
    //          add a new shape and initiate the score to be 0.
    public Game() {
        this.grid = new int[verticalSize][horizontalSize];
        this.colorGrid = new Color[verticalSize][horizontalSize];
        shapeInMove = new Shape();
        score = 0;
    }

    // MODIFIES: this
    // EFFECTS: Tick Method:
    //              - Move down the shape in move by 1
    //              - Check if the grid EITHER contains a 2 or a shape has hit bottom
    //                after adding the shape to the game grid.
    //              - If true: revert the grid and color grid back to the grid and color
    //                         grid before moving the shape down
    //              - Check to see if there is a line to be cleared
    public void tick() {
        int[][] tempGrid;
        Color[][] tempColorGrid;
        tempGrid = getCopy(getGrid());
        tempColorGrid = getCopy(getColorGrid());

        this.moveDown();

        if (checkForTwo(this.getGrid()) || this.checkBottomCollision()) {
            this.grid = tempGrid;
            this.colorGrid = tempColorGrid;
            shapeInMove = new Shape();
            addShape(shapeInMove);
        }

        checkLine();
    }

    // REQUIRES: shape != null
    // MODIFIES: this
    // EFFECTS: Add shape to grid by adding the shapetype and color into the grid and
    //          color grid at shape.coordinates
    public void addShape(Shape shape) {
        int[] addCoordinates = addLocation(shape);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (! (addCoordinates[0] >= verticalSize) && ! (addCoordinates[1] >= horizontalSize)) {
                    if (shape.getShapeType()[y][x] == 1) {
                        grid[addCoordinates[0]][addCoordinates[1]] += 1;
                        colorGrid[addCoordinates[0]][addCoordinates[1]] = shape.getColor();
                    }
                    addCoordinates[1] += 1;
                }
            }
            addCoordinates[0] += 1;
            addCoordinates[1] -= 4;
        }
        this.shapeInMove = shape;
    }

    // REQUIRES: shape != null
    // MODIFIES: this
    // EFFECTS: remove the given shape from the grid
    public void removeShape(Shape tempShape) {
        int[] addCoordinates = addLocation(tempShape);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (! (addCoordinates[0] >= verticalSize) && ! (addCoordinates[1] >= horizontalSize)) {
                    if (tempShape.getShapeType()[y][x] == 1) {
                        grid[addCoordinates[0]][addCoordinates[1]] = 0;
                        colorGrid[addCoordinates[0]][addCoordinates[1]] = null;
                    }
                    addCoordinates[1] += 1;
                }
            }
            addCoordinates[0] += 1;
            addCoordinates[1] -= 4;
        }
    }

    // EFFECTS: Evaluate the shape's coordinates and return an array of integers
    //          representing the grid coordinates where the shape's top left will be
    public int[] addLocation(Shape shape) {
        int[] addCoordinates = new int[2];
        for (int y = 0; y < verticalSize; y++) {
            for (int x = 0; x < horizontalSize; x++) {
                if ((y == shape.getCoordinates()[0] + 1) && (x == shape.getCoordinates()[1] + 2)) {
                    addCoordinates[0] = y;
                    addCoordinates[1] = x;
                }
            }
        }
        return addCoordinates;
    }

    // EFFECTS: If there is a horizontal line of 1's in grid, collect
    //          their vertical indexes into an arrayList
    public void checkLine() {

        ArrayList<Integer> tempYValues = new ArrayList<>();

        for (int y = 0; y < verticalSize; y++) {
            Boolean temp = false;
            for (int x = padding; x < horizontalSize - padding; x++) {
                if (grid[y][x] != 1) {
                    break;
                } else if (x == horizontalSize - padding - 1) {
                    tempYValues.add(y);
                }
            }
        }

        if (!tempYValues.isEmpty()) {
            clearLines(tempYValues);
        }
    }

    // REQUIRED: tempYValues != empty
    // EFFECTS: Call removeRow() for all elements of tempYValues
    public void clearLines(ArrayList<Integer> tempYValues) {
        for (int i = 0; i < tempYValues.size(); i++) {
            removeRow(tempYValues.get(i));
        }
    }

    // REQUIRED: index >= padding && index < verticalSize
    // MODIFIES: this
    // EFFECTS: Remove the row at given index and pull the elements in the upper rows down
    public void removeRow(int index) {
        for (int y = index - 1; y > 0; y--) {
            grid[y + 1] = grid[y];
        }
        score += POINTS_PER_CLEAR;
    }

    // EFFECTS: Check if:
    //                  1. There is a 2 in the grid
    //                  2. There is a non-zero element in the top-padding of the grid
    public Boolean isEnded() {
        Boolean result = checkForTwo(this.getGrid());
        for (int y = 0; y < padding; y++) {
            for (int x = padding; x < horizontalSize - padding; x++) {
                if (this.grid[y][x] >= 1) {
                    result = true;
                }
            }
        }
        return result;
    }

    // EFFECTS: Check if the score >= winScore
    public boolean isWon() {
        return score >= winScore;
    }

    // EFFECTS: Check if there is a non-zero element in the padding of the grid
    public Boolean checkHorizontalCollision() {
        Boolean result = false;

        for (int y = padding - 1; y < verticalSize - padding; y++) {
            for (int x = 0; x < horizontalSize; x++) {
                if (x >= padding && x < horizontalSize - padding) {
                    ;
                } else if (this.grid[y][x] >= 1) {
                    result = true;
                }
            }
        }
        return result;
    }

    // EFFECTS: Check if there is a non-zero element in the bottom-padding of the grid
    public Boolean checkBottomCollision() {
        Boolean result = false;

        for (int y = verticalSize - padding; y < verticalSize; y++) {
            for (int x = padding - 1; x < horizontalSize - padding; x++) {
                if (this.grid[y][x] >= 1) {
                    result = true;
                }
            }
        }
        return result;
    }

    // EFFECTS: check if there is a 2 in the grid (representing a collision between shapes)
    public Boolean checkForTwo(int[][] grid) {
        Boolean result = false;
        for (int i = 0; i < verticalSize; i++) {
            for (int j = 0; j < horizontalSize; j++) {
                if (grid[i][j] == 2) {
                    result = true;
                }
            }
        }
        return result;
    }

    // EFFECTS: returns a copy of the grid (not a reference)
    public int[][] getCopy(int[][] grid) {
        int[][] result = new int[verticalSize][horizontalSize];
        for (int i = 0; i < verticalSize; i++) {
            result[i] = grid[i].clone();
        }
        return result;
    }

    // EFFECTS: returns a copy of the colorGrid (not a reference)
    private Color[][] getCopy(Color[][] colorGrid) {
        Color[][] result = new Color[verticalSize][horizontalSize];
        for (int i = 0; i < verticalSize; i++) {
            result[i] = colorGrid[i].clone();
        }
        return result;
    }

    // MODIFIES: this
    // EFFECTS: remove the current shapeInMove from the grid and add a shape
    //          with its coordinates moved to the right
    public void moveRight() {
        removeShape(shapeInMove);
        this.shapeInMove.addCoordinatesHorizontal(true);
        addShape(shapeInMove);
    }

    // MODIFIES: this
    // EFFECTS: remove the current shapeInMove from the grid and add a shape
    //          with its coordinates moved to the left
    public void moveLeft() {
        removeShape(shapeInMove);
        this.shapeInMove.addCoordinatesHorizontal(false);
        addShape(shapeInMove);
    }

    // MODIFIES: this
    // EFFECTS: remove the current shapeInMove from the grid and add a shape
    //          with its coordinates moved down
    public void moveDown() {
        removeShape(shapeInMove);
        this.shapeInMove.addCoordinatesVertical(true);
        addShape(shapeInMove);
    }

    // MODIFIES: this
    // EFFECTS: Remove the current shapeInMove, rotate the shapeInMove and add it to grid
    public void rotateShape() {
        this.removeShape(shapeInMove);
        this.shapeInMove.rotateShape();
        this.addShape(shapeInMove);
    }

    // this
    // EFFECTS: moveDown() until the shape EITHER:
    //                                  hits bottom OR
    //                                  hits another shape
    public void dropDown() {
        int[][] tempGrid = new int[verticalSize][horizontalSize];
        Color[][] tempColorGrid = new Color[verticalSize][horizontalSize];

        while (!checkBottomCollision() && !checkForTwo(this.getGrid())) {
            tempGrid = getCopy(getGrid());
            tempColorGrid = getCopy(getColorGrid());

            this.moveDown();
        }
        this.grid = tempGrid;
        this.colorGrid = tempColorGrid;

    }

    public int[][] getGrid() {
        return this.grid;
    }

    public Color[][] getColorGrid() {
        return this.colorGrid;
    }

    public int getScore() {
        return this.score;
    }

    public long getTicksPerSecond() {
        return this.ticksPerSecond;
    }

    public int getWinScore() {
        return this.winScore;
    }

    public void setTicksPerSecond(int num) {
        this.ticksPerSecond = num;
    }

    public void setWinScore(int num) {
        this.winScore = num;
    }

    public void setScore(int num) {
        this.score = num;
    }

    public void setShapeInMove(Shape shape) {
        this.shapeInMove = shape;
    }
}
