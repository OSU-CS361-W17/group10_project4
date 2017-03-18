package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.HashSet;
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
        if(hardMode) {
            placeComputerShipsHard();
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
    
    // placeComputerShipsHard: if hard mode is selected, then the computer places ships in random locations across
    // the board (with no overlapping)
    private void placeComputerShipsHard() {
        int baseX = 0;                                        // x coordinate of "base" square of given ship
        int baseY = 0;                                        // y coordinate of "base" square of given ship
        Random ran = new Random();
        // current points that ships have been placed on
        HashSet<ArrayList<Integer>> covered = new HashSet<>();
        // proposed new points to place current ship on and add to covered points
        HashSet<ArrayList<Integer>> pointsToAdd = new HashSet<>();

        // first, place computer_aircraftCarrier, length 5
        boolean valid = false;                                // whether we may finish placing current ship
        int orientation = ran.nextInt(2);              // orientation of ship: 0 == horizontal, 1 == vertical
        while (!valid) {
            valid = true;                                     // set to true preemptively
            if (orientation == 0) {                           // orientation: horizontal
                baseX = ran.nextInt(6);
                baseY = ran.nextInt(10);
                for (int i = 0; i < 5; i++) {
                    ArrayList<Integer> pointToAdd = new ArrayList<Integer>();
                    pointToAdd.add(baseX + i);
                    pointToAdd.add(baseY);
                    pointsToAdd.add(pointToAdd);              // add preemptively
                    if (covered.contains(pointToAdd)) {
                        valid = false;
                        pointsToAdd.clear();
                        break;
                    }
                }
            } else {                                          // orientation: vertical
                baseX = ran.nextInt(10);
                baseY = ran.nextInt(6);
                for (int i = 0; i < 5; i++) {
                    ArrayList<Integer> pointToAdd = new ArrayList<Integer>();
                    pointToAdd.add(baseX);
                    pointToAdd.add(baseY + i);
                    pointsToAdd.add(pointToAdd);              // add preemptively
                    if (covered.contains(pointToAdd)) {
                        valid = false;
                        pointsToAdd.clear();
                        break;
                    }
                }
            }
        }
        // all points in pointsToAdd are valid, so add them to covered
        for (ArrayList<Integer> point : pointsToAdd) {
            covered.add(point);
        }
        // update current ship's coordinates
        // using baseX and baseY and ship's orientation, update current ship's location with setLocation:
        // TODO: test if the points are correct by logging - Coordinate(Across, Down) ?
        // setLocation(new Coordinate(rowInt, colInt), new Coordinate(rowInt, colInt));
        if (orientation == 0) {
            computer_aircraftCarrier.setLocation(new Coordinate(baseY, baseX),
                                                 new Coordinate(baseY, baseX + 4));
        } else {
            computer_aircraftCarrier.setLocation(new Coordinate(baseY, baseX),
                                                 new Coordinate(baseY + 4, baseX));
        }

        
        // next, place computer_battleship, length 4
        valid = false;
        orientation = ran.nextInt(2);
        while (!valid) {
            valid = true;
            if (orientation == 0) {
                baseX = ran.nextInt(7);
                baseY = ran.nextInt(10);
                for (int i = 0; i < 4; i++) {
                    ArrayList<Integer> pointToAdd = new ArrayList<Integer>();
                    pointToAdd.add(baseX + i);
                    pointToAdd.add(baseY);
                    pointsToAdd.add(pointToAdd);
                    if (covered.contains(pointToAdd)) {
                        valid = false;
                        pointsToAdd.clear();
                        break;
                    }
                }
            } else {
                baseX = ran.nextInt(10);
                baseY = ran.nextInt(7);
                for (int i = 0; i < 4; i++) {
                    ArrayList<Integer> pointToAdd = new ArrayList<Integer>();
                    pointToAdd.add(baseX);
                    pointToAdd.add(baseY + i);
                    pointsToAdd.add(pointToAdd);
                    if (covered.contains(pointToAdd)) {
                        valid = false;
                        pointsToAdd.clear();
                        break;
                    }
                }
            }
        }
        for (ArrayList<Integer> point : pointsToAdd) {
            covered.add(point);
        }
        if (orientation == 0) {
            computer_battleship.setLocation(new Coordinate(baseY, baseX),
                                            new Coordinate(baseY, baseX + 3));
        } else {
            computer_battleship.setLocation(new Coordinate(baseY, baseX),
                                            new Coordinate(baseY + 3, baseX));
        }

        
        // next, place computer_clipper, length 3
        valid = false;
        orientation = ran.nextInt(2);
        while (!valid) {
            valid = true;
            if (orientation == 0) {
                baseX = ran.nextInt(8);
                baseY = ran.nextInt(10);
                for (int i = 0; i < 3; i++) {
                    ArrayList<Integer> pointToAdd = new ArrayList<Integer>();
                    pointToAdd.add(baseX + i);
                    pointToAdd.add(baseY);
                    pointsToAdd.add(pointToAdd);
                    if (covered.contains(pointToAdd)) {
                        valid = false;
                        pointsToAdd.clear();
                        break;
                    }
                }
            } else {
                baseX = ran.nextInt(10);
                baseY = ran.nextInt(8);
                for (int i = 0; i < 3; i++) {
                    ArrayList<Integer> pointToAdd = new ArrayList<Integer>();
                    pointToAdd.add(baseX);
                    pointToAdd.add(baseY + i);
                    pointsToAdd.add(pointToAdd);
                    if (covered.contains(pointToAdd)) {
                        valid = false;
                        pointsToAdd.clear();
                        break;
                    }
                }
            }
        }
        for (ArrayList<Integer> point : pointsToAdd) {
            covered.add(point);
        }
        if (orientation == 0) {
            computer_clipper.setLocation(new Coordinate(baseY, baseX),
                                         new Coordinate(baseY, baseX + 2));
        } else {
            computer_clipper.setLocation(new Coordinate(baseY, baseX),
                                         new Coordinate(baseY + 2, baseX));
        }
        
        
        // next, place computer_dinghy, length 1
        valid = false;
        while (!valid) {
            valid = true;
            baseX = ran.nextInt(10);
            baseY = ran.nextInt(10);
            ArrayList<Integer> pointToAdd = new ArrayList<Integer>();
            pointToAdd.add(baseX);
            pointToAdd.add(baseY);
            pointsToAdd.add(pointToAdd);
            if (covered.contains(pointToAdd)) {
                valid = false;
                pointsToAdd.clear();
            }
        }
        for (ArrayList<Integer> point : pointsToAdd) {
            covered.add(point);
        }
        computer_dinghy.setLocation(new Coordinate(baseY, baseX), new Coordinate(baseY, baseX));
 
        
        // finally, place computer_submarine, length 2
        valid = false;
        orientation = ran.nextInt(2);
        while (!valid) {
            valid = true;
            if (orientation == 0) {
                baseX = ran.nextInt(9);
                baseY = ran.nextInt(10);
                for (int i = 0; i < 2; i++) {
                    ArrayList<Integer> pointToAdd = new ArrayList<Integer>();
                    pointToAdd.add(baseX + i);
                    pointToAdd.add(baseY);
                    pointsToAdd.add(pointToAdd);
                    if (covered.contains(pointToAdd)) {
                        valid = false;
                        pointsToAdd.clear();
                        break;
                    }
                }
            } else {
                baseX = ran.nextInt(10);
                baseY = ran.nextInt(9);
                for (int i = 0; i < 2; i++) {
                    ArrayList<Integer> pointToAdd = new ArrayList<Integer>();
                    pointToAdd.add(baseX);
                    pointToAdd.add(baseY + i);
                    pointsToAdd.add(pointToAdd);
                    if (covered.contains(pointToAdd)) {
                        valid = false;
                        pointsToAdd.clear();
                        break;
                    }
                }
            }
        }
        for (ArrayList<Integer> point : pointsToAdd) {
            covered.add(point);
        }
        if (orientation == 0) {
            computer_aircraftCarrier.setLocation(new Coordinate(baseY, baseX),
                new Coordinate(baseY, baseX + 1));
        } else {
            computer_aircraftCarrier.setLocation(new Coordinate(baseY, baseX),
                new Coordinate(baseY + 1, baseX));
        }
    }
    
    
}