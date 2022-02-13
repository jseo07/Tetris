package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Shape {
    public final Color[] colors = {Color.CYAN, Color.RED, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.ORANGE};
    public final ArrayList<Color> colorList = new ArrayList<>(Arrays.asList(colors)); //Arraylist of colors
    public final int[] [] [] shapes = { // List of Shapes
            {
                    {0, 1, 0, 0},
                    {0, 1, 0, 0},
                    {0, 1, 0, 0},
                    {0, 1, 0, 0}
            }, // Stick Shape
            {
                    {0, 0, 0, 0},
                    {0, 1, 1, 0},
                    {0, 1, 1, 0},
                    {0, 0, 0, 0},
            }, // Square Shape
            {
                    {0, 0, 0, 0},
                    {0, 1, 1, 0},
                    {1, 1, 0, 0},
                    {0, 0, 0, 0}
            }, // Zig (Right) Shape
            {
                    {0, 0, 0, 0},
                    {0, 1, 1, 0},
                    {0, 0, 1, 1},
                    {0, 0, 0, 0}
            }, // Zig (Left) Shape
            {
                    {0, 1, 0, 0},
                    {0, 1, 0, 0},
                    {0, 1, 1, 0},
                    {0, 0, 0, 0}
            }, // L (Right) Shape
            {
                    {0, 0, 1, 0},
                    {0, 0, 1, 0},
                    {0, 1, 1, 0},
                    {0, 0, 0, 0}
            }, // L (Left) Shape
            {
                    {0, 0, 0, 0},
                    {0, 1, 1, 1},
                    {0, 0, 1, 0},
                    {0, 0, 0, 0}
            }, // Trimmed-F Shape
    };
    public final ArrayList<int[][]> shapeList = new ArrayList<>(Arrays.asList(shapes)); // Arraylist of Shapes

    private int[] [] shapeType;
    private int[] coordinates = new int[2];
    private Color color;

    // EFFECTS: Create a new shape with random shape, random color, and coordinates with
    //          y coordinates = Game padding AND x coordinates = random number between 2 and 8.
    public Shape() {
        this.shapeType = shapes [(int) (Math.random() * shapes.length) ];
        this.color = colors [(int) (Math.random() * colors.length)];
        this.coordinates [0] = Game.padding;
        this.coordinates [1] = ThreadLocalRandom.current().nextInt(2, 8);
    }

    // MODIFIES: this.shapeType
    // EFFECTS: Rotate this.shapeType by 90 degrees
    public void rotateShape() {
        int[][] tempShapeType = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tempShapeType[j][3 - i] = shapeType[i][j];
            }
        }
        shapeType = tempShapeType;
    }

    // MODIFIES: this.coordinates
    // EFFECTS: if true, add 1 to y coordinates, else subtract 1
    public void addCoordinatesVertical(Boolean dir) {
        if (dir) {
            this.coordinates[0] += 1;
        } else {
            this.coordinates[0] -= 1;
        }
    }

    // MODIFIES: this.coordinates
    // EFFECTS: if true, add 1 to x coordinates, else subtract 1
    public void addCoordinatesHorizontal(Boolean dir) {
        if (dir) {
            this.coordinates[1] += 1;
        } else {
            this.coordinates[1] -= 1;
        }
    }

    public int[] getCoordinates() {
        return this.coordinates;
    }

    public Color getColor() {
        return this.color;
    }

    public int[][] getShapeType() {
        return this.shapeType;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates[0] = coordinates[0];
        this.coordinates[1] = coordinates[1];
    }
}
