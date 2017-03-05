package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;

public class CivShip extends Ship {


    public CivShip(String name, int length, Coordinate start, Coordinate end) {
        super(name, length, start, end, false, false);
    }

    // Return all coordinates for use by game model to sink civilian ships in one hit.
    public ArrayList<Coordinate> getSinkCoordinates() {
        ArrayList<Coordinate> coords = new ArrayList<>();
        // Determine ship orientation
        String orientation;

        if(this.getStart().getAcross() != this.getEnd().getAcross()) {
            // Ship is vertical
            orientation = "vertical";
        } else {
            orientation = "horizontal";
        }

        if(orientation == "horizontal") {
            int across = this.getStart().getAcross();
            int initialDown = this.getStart().getDown();
            int length = this.getLength();
            for(int i = 0; i < length; i++) {
                Coordinate coord = new Coordinate(across, initialDown + i);
                coords.add(coord);
            }
        } else {
            int down = this.getStart().getDown();
            int initialAcross = this.getStart().getAcross();
            int length = this.getLength();
            System.out.println("Length: " + length);
            for(int i = 0; i < length; i++) {
                Coordinate coord = new Coordinate(initialAcross + i, down);
                coords.add(coord);
            }
        }

        return coords;
    }

}
