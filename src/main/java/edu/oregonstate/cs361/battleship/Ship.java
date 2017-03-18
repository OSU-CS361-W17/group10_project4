package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;

/**
 * Created by michaelhilton on 1/5/17.
 */
public class Ship {
    protected String name;
    protected int length;
    protected Coordinate start;
    protected Coordinate end;
    protected boolean hasStealth;
    protected boolean hasArmor;

    public Coordinate getStart() { return this.start; }
    public Coordinate getEnd() { return this.end; }
    public int getLength() { return this.length; }

    public Ship(String name, int length,Coordinate start, Coordinate end, boolean stealth, boolean armor) {
        this.name = name;
        this.length = length;
        this.start = start;
        this.end = end;
        this.hasArmor = armor;
        this.hasStealth = stealth;
    }


    public void setLocation(Coordinate s, Coordinate e) {
        start = s;
        end = e;

    }

    public boolean covers(Coordinate test) {
        //horizontal
        if(start.getAcross() == end.getAcross()){
            if(test.getAcross() == start.getAcross()){
                if((test.getDown() >= start.getDown()) &&
                (test.getDown() <= end.getDown()))
                return true;
            } else {
                return false;
            }
        }
        //vertical
        else{
            if(test.getDown() == start.getDown()){
                if((test.getAcross() >= start.getAcross()) &&
                        (test.getAcross() <= end.getAcross()))
                    return true;
            } else {
                return false;
            }

        }
        return false;
    }

    public String getName() {
        return name;
    }

    // Iterate through ship-occupied coordinates to determine if any haven't been hit yet
    public boolean isSunk(ArrayList<Coordinate> hitCoords) {
        boolean sunk = true;
        ArrayList<Coordinate> coveredCoords = this.getCoveredCoordinates();
        for(int j = 0; j < coveredCoords.size(); j++) {
            coveredCoords.get(j).print();
        }
        for (Coordinate coveredCoord : coveredCoords) {
            if(!hitCoords.contains(coveredCoord)) {
                sunk = false;
                System.out.print("NOT SUNK: ");
                coveredCoord.print();
            }
        }

        return sunk;
    }


    public boolean scan(Coordinate coor) {
        if(covers(coor) && !(this.hasStealth)){
            return true;
        }
        if(covers(new Coordinate(coor.getAcross()-1,coor.getDown())) && !(this.hasStealth)){
            return true;
        }
        if(covers(new Coordinate(coor.getAcross()+1,coor.getDown())) && !(this.hasStealth)){
            return true;
        }
        if(covers(new Coordinate(coor.getAcross(),coor.getDown()-1)) && !(this.hasStealth)){
            return true;
        }
        if(covers(new Coordinate(coor.getAcross(),coor.getDown()+1)) && !(this.hasStealth)){
            return true;
        }
        return false;
    }

    // Return all coordinates for use by game model to sink civilian ships in one hit.
    public ArrayList<Coordinate> getCoveredCoordinates() {
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
