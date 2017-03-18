package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
public class BattleshipModel {

    private Ship aircraftCarrier = new Ship("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0), false, true);
    private Ship battleship = new Ship("Battleship",4, new Coordinate(0,0),new Coordinate(0,0), true, true);
    private CivShip clipper = new CivShip("Clipper",3, new Coordinate(0,0),new Coordinate(0,0));
    private CivShip dinghy = new CivShip("Dinghy",1, new Coordinate(0,0),new Coordinate(0,0));
    private Ship submarine = new Ship("Submarine",3, new Coordinate(0,0),new Coordinate(0,0), true, true);

//    private Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,6), false, true);
//    private Ship computer_battleship = new Ship("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(5,8), true, true);
//    private Ship computer_clipper = new CivShip("Computer_Clipper",3, new Coordinate(4,1),new Coordinate(4,3));
//    private Ship computer_dinghy = new CivShip("Computer_Dinghy",1, new Coordinate(7,3),new Coordinate(7,3));
//    private Ship computer_submarine = new Ship("Computer_Submarine",3, new Coordinate(9,6),new Coordinate(9,8), true, true);

    private Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,7), false, true);
    private Ship computer_battleship = new Ship("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(6,8), true, true);
    private CivShip computer_clipper = new CivShip("Computer_Clipper",3, new Coordinate(4,1),new Coordinate(4,4));
    private CivShip computer_dinghy = new CivShip("Computer_Dinghy",1, new Coordinate(7,3),new Coordinate(7,5));
    private Ship computer_submarine = new Ship("Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,8), true, true);
  

    ArrayList<Coordinate> playerHits;
    private ArrayList<Coordinate> playerMisses;
    ArrayList<Coordinate> computerHits;
    private ArrayList<Coordinate> computerMisses;

    // Track ship hit by CPU in hard mode to determine when sunk (resume random firing)
    private Ship targetShip;
    private Coordinate previousHit;

    boolean scanResult = false;
    
    private boolean hardMode;


    public BattleshipModel(String difficulty) {
        // Set game mode
        if(difficulty.equals("hard")) {
            this.hardMode = true;
        } else {
            this.hardMode = false;
        }

        // Initialize hits + misses
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();
        previousHit = new Coordinate(0, 0);

        // Initialize CPU ship placements
        initializeCpuShips();
    }

    private void initializeCpuShips() {
        if(hardMode) {
            // Randomly place ships
        } else {
            // Place ships in predefined locations (or do nothing, utilizing existing hardcoded placements)
        }
    }


    public Ship getShip(String shipName) {
        if (shipName.equalsIgnoreCase("aircraftcarrier")) {
            return aircraftCarrier;
        } if(shipName.equalsIgnoreCase("battleship")) {
            return battleship;
        } if(shipName.equalsIgnoreCase("clipper")) {
        return clipper;
        } if(shipName.equalsIgnoreCase("dinghy")) {
            return dinghy;
        }if(shipName.equalsIgnoreCase("submarine")) {
            return submarine;
        } else {
            return null;
        }
    }

    public BattleshipModel placeShip(String shipName, String row, String col, String orientation) {
        int rowint = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);
        if(orientation.equals("horizontal")){
            if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+5));
            } if(shipName.equalsIgnoreCase("battleship")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+4));
            } if(shipName.equalsIgnoreCase("clipper")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+3));
            } if(shipName.equalsIgnoreCase("dinghy")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+2));
            }if(shipName.equalsIgnoreCase("submarine")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt + 2));
            }
        }else{
            //vertical
                if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+5,colInt));
                } if(shipName.equalsIgnoreCase("battleship")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+4,colInt));
                } if(shipName.equalsIgnoreCase("clipper")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+3,colInt));
                } if(shipName.equalsIgnoreCase("dinghy")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+2,colInt));
                }if(shipName.equalsIgnoreCase("submarine")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint + 2, colInt));
                }
        }
        return this;
    }

    public void shootAtComputer(int row, int col) {
        Coordinate coor = new Coordinate(row,col);
        if(computer_aircraftCarrier.covers(coor)){
            computerHits.add(coor);
        }else if (computer_battleship.covers(coor)){
            computerHits.add(coor);
        }else if (computer_dinghy.covers(coor)){
            // Civilian Ship - sink immediately
            sinkShip(false, computer_dinghy.getCoveredCoordinates());
        }else if (computer_clipper.covers(coor)){
            // Civilian Ship - sink immediately
            sinkShip(false, computer_clipper.getCoveredCoordinates());
        }else if (computer_submarine.covers(coor)){
            computerHits.add(coor);
        } else {
            computerMisses.add(coor);
        }
    }

    public void shootAtPlayer() {
        Coordinate coor;

        if(hardMode) {
            // "Smart" firing - Track previous hits and fire nearby

            // Clear target if previously hit ship is sunk
            if(targetShip != null && targetShip.isSunk(playerHits)) {
                targetShip = null;
                previousHit = new Coordinate(0, 0);
            }

            // Otherwise, fire away
            if(targetShip != null && !targetShip.isSunk(playerHits) && previousHit.isValid()) {
                System.out.println("Firing close");
                coor = Coordinate.getClose(previousHit);
                System.out.println(coor.getAcross() + ", " + coor.getDown());
            } else {
                System.out.println("Firing randomly");
                // Fire randomly, no previous hit to track
                coor = Coordinate.getRandom();
            }
        } else {
            // "Dumb" firing - Fire in pattern
            coor = Coordinate.getRandom();
        }

        playerShot(coor);
    }


    void playerShot(Coordinate coor) {
        if(playerMisses.contains(coor)){
            System.out.println("Dupe");
            return;
        }

        if(aircraftCarrier.covers(coor)){
            playerHits.add(coor);
            previousHit = coor;
            targetShip = aircraftCarrier;
        }else if (battleship.covers(coor)){
            playerHits.add(coor);
            previousHit = coor;
            targetShip = battleship;
        }else if (dinghy.covers(coor)){
            // Civilian Ship - sink immediately
            sinkShip(true, dinghy.getCoveredCoordinates());
            targetShip = null;
            playerHits.add(coor);
            previousHit = new Coordinate(0, 0);
        }else if (clipper.covers(coor)){
            // Civilian Ship - sink immediately
            sinkShip(true, clipper.getCoveredCoordinates());
            playerHits.add(coor);
            targetShip = null;
            playerHits.add(coor);
            previousHit = new Coordinate(0, 0);
        }else if (submarine.covers(coor)){
            playerHits.add(coor);
            previousHit = coor;
            targetShip = submarine;
        } else {
            playerMisses.add(coor);
        }
    }

    private void sinkShip(boolean isPlayerShip, ArrayList<Coordinate> coords) {
        coords.forEach(coord -> {
            // Player ship sunk
            if(isPlayerShip) {
                playerHits.add(coord);

                // Computer ship sunk
            } else {
                computerHits.add(coord);
            }
        });
    }

    public void scan(int rowInt, int colInt) {
        Coordinate coor = new Coordinate(rowInt,colInt);
        scanResult = false;
        if(computer_aircraftCarrier.scan(coor)){
            scanResult = true;
        }
        else if (computer_battleship.scan(coor)){
            scanResult = true;
        }else if (computer_dinghy.scan(coor)){
            scanResult = true;
        }else if (computer_clipper.scan(coor)){
            scanResult = true;
        }else if (computer_submarine.scan(coor)){
            scanResult = true;
        } else {
            scanResult = false;
        }
    }

    public boolean getScanResult() {
        return scanResult;
    }
}