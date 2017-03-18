package edu.oregonstate.cs361.battleship;

import java.util.Random;

/**
 * Created by michaelhilton on 1/8/17.
 */
public class Coordinate {
    private int Across;
    private int Down;

    public Coordinate(int across, int down) {
        Across = across;
        Down = down;
    }

    public int getDown() {
        return Down;
    }

    public void setDown(int down) {
        Down = down;
    }

    public int getAcross() {
        return Across;
    }

    public void setAcross(int across) {
        Across = across;
    }

    public boolean isValid() {
        return (Across > 0 && Across <= 10) && (Down > 0 && Down <= 10);
    }

    public void print() {
        System.out.println(this.Across + ", " + this.Down);
    }

    public static Coordinate getRandom() {
        Random random = new Random();
        int min = 1;
        int max = 10;
        int randRow = random.nextInt(max - min + 1) + min;
        int randCol = random.nextInt(max - min + 1) + min;

        return new Coordinate(randCol, randRow);
    }

    // Target random locations near supplied coordinate
    public static Coordinate getClose(Coordinate coord) {
        Random random = new Random();
        int min = 1;
        int max = 10;
        int rowMin = clamp(coord.getAcross() - 2, min, max);
        int rowMax = clamp(coord.getAcross() + 2, min, max);
        int colMin = clamp(coord.getDown() - 2, min, max);
        int colMax = clamp(coord.getDown() + 2, min, max);

        // Get random row target +- 3 tiles from coord row
        int randRow = random.nextInt(rowMax - rowMin + 1) + rowMin;
        // Get random col target +- 3 tiles from coord col
        int randCol = random.nextInt(colMax - colMin + 1) + colMin;

        return new Coordinate(randRow, randCol);
    }

    private static int clamp(int val, int min, int max) {
        return val > max ? max : val < min ? min : val;
    }

    @Override
    public boolean equals(Object obj) {
        Coordinate coord = (Coordinate) obj;
        return (coord.getAcross() == this.getAcross()) && (coord.getDown() == this.getDown());
    }
}
