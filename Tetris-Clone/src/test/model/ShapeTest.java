package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShapeTest {
    Shape testShape;

    @BeforeEach
    void runBefore() {
        testShape = new Shape();
    }

    @Test
    void testConstructor(){
        int[] coordinates = testShape.getCoordinates();
        assertEquals(Game.padding, coordinates[0]);
        assertTrue(coordinates[1] >= 2 && coordinates[1] <= 8);
        assertTrue(testShape.colorList.contains(testShape.getColor()));
        assertTrue(testShape.shapeList.contains(testShape.getShapeType()));
    }

    @Test
    void testRotate() {
        int[][] tempShape = testShape.getShapeType();
        int[] temp = testShape.getShapeType()[0];
        testShape.rotateShape();
        for (int i = 0; i < 4; i++) {
            assertEquals(temp[i], testShape.getShapeType()[i][3]);
        }

        temp = testShape.getShapeType()[3];
        testShape.rotateShape();
        for (int i = 0; i < 4; i++) {
            assertEquals(temp[i], testShape.getShapeType()[i][0]);
        }
    }

    @Test
    void testAddCoordinatesVertical() {
        testShape.addCoordinatesVertical(true);
        assertEquals(Game.padding + 1, testShape.getCoordinates()[0]);

        testShape.addCoordinatesVertical(true);
        assertEquals(Game.padding + 2, testShape.getCoordinates()[0]);

        testShape.addCoordinatesVertical(false);
        assertEquals(Game.padding + 1, testShape.getCoordinates()[0]);

        testShape.addCoordinatesVertical(false);
        assertEquals(Game.padding, testShape.getCoordinates()[0]);

        testShape.addCoordinatesVertical(false);
        assertEquals(Game.padding - 1, testShape.getCoordinates()[0]);
    }

    @Test
    void testAddCoordinatesHorizontal() {
        int temp = testShape.getCoordinates()[1];
        testShape.addCoordinatesHorizontal(true);
        assertEquals(temp + 1, testShape.getCoordinates()[1]);

        testShape.addCoordinatesHorizontal(true);
        assertEquals(temp + 2, testShape.getCoordinates()[1]);

        testShape.addCoordinatesHorizontal(false);
        assertEquals(temp + 1, testShape.getCoordinates()[1]);

        testShape.addCoordinatesHorizontal(false);
        assertEquals(temp, testShape.getCoordinates()[1]);
    }
}
