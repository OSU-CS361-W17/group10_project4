package edu.oregonstate.cs361.battleship;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by michaelhilton on 2/7/17.
 */
class CoordinateTest {
    @Test
    void getDown() {
        Coordinate c = new Coordinate(1,2);
        assertEquals(2,c.getDown());
    }

    @Test
    void setDown() {
        Coordinate c = new Coordinate(1,1);
        c.setDown(9);
        assertEquals(9,c.getDown());
    }

    @Test
    void getAcross() {
        Coordinate c = new Coordinate(1,2);
        assertEquals(1,c.getAcross());
    }

    @Test
    void setAcross() {
        Coordinate c = new Coordinate(1,1);
        c.setAcross(9);
        assertEquals(9,c.getAcross());
    }
    @Test
   void isValid() {
        Coordinate valid = new Coordinate(2, 2);
        assertEquals(true,valid.isValid());
    }
    @Test
    void print() {
        Coordinate myCoord = new Coordinate(2, 2);
         myCoord.print();

    }
    @Test
    void getRandom(){
        Random random = new Random();
        Coordinate randomCoord = Coordinate.getRandom();
        assert(randomCoord.isValid());
    }
    @Test
    void getClose(){
        Coordinate myCoord = new Coordinate(5,5);
        Coordinate closeCoord = Coordinate.getClose(myCoord);
        assert(closeCoord.isValid());
    }
}

