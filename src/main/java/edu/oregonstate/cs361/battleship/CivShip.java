package edu.oregonstate.cs361.battleship;

public class CivShip extends Ship{


    public void autoSink(){
        // Function that fires at all the ship coords, sinking it. Called when any coord of civship gets hit
    }

    public CivShip(String name, int length,Coordinate start, Coordinate end) {
        this.name = name;
        this.length = length;
        this.start = start;
        this.end = end;
        this.hasStealth = false;
        this.hasArmor = false;
    }

}
