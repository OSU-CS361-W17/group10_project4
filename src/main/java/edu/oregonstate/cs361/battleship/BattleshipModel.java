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
    private Ship clipper = new CivShip("Clipper",3, new Coordinate(0,0),new Coordinate(0,0));
    private Ship dinghy = new CivShip("Dinghy",1, new Coordinate(0,0),new Coordinate(0,0));
    private Ship submarine = new Ship("Submarine",3, new Coordinate(0,0),new Coordinate(0,0), true, true);

    private Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,6), false, true);
    private Ship computer_battleship = new Ship("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(5,8), true, true);
    private Ship computer_clipper = new CivShip("Computer_Clipper",3, new Coordinate(4,1),new Coordinate(4,3));
    private Ship computer_dinghy = new CivShip("Computer_Dinghy",1, new Coordinate(7,3),new Coordinate(7,3));
    private Ship computer_submarine = new Ship("Computer_Submarine",3, new Coordinate(9,6),new Coordinate(9,8), true, true);

    private Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,7), false, true);
    private Ship computer_battleship = new Ship("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(6,8), true, true);
    private CivShip computer_clipper = new CivShip("Computer_Clipper",3, new Coordinate(4,1),new Coordinate(4,4));
    private CivShip computer_dinghy = new CivShip("Computer_Dinghy",1, new Coordinate(7,3),new Coordinate(7,5));
    private Ship computer_submarine = new Ship("Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,8), true, true);
  

    ArrayList<Coordinate> playerHits;
    private ArrayList<Coordinate> playerMisses;
    ArrayList<Coordinate> computerHits;
    private ArrayList<Coordinate> computerMisses;

    boolean scanResult = false;

    

    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();
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
    
    public BattleshipModel placeComputerShip(String shipName) {
        // first, check that 
    }

    public void shootAtComputer(int row, int col) {
        Coordinate coor = new Coordinate(row,col);
        if(computer_aircraftCarrier.covers(coor)){
            computerHits.add(coor);
        }else if (computer_battleship.covers(coor)){
            computerHits.add(coor);
        }else if (computer_dinghy.covers(coor)){
            // Civilian Ship - sink immediately
            sinkShip(false, computer_dinghy.getSinkCoordinates());
        }else if (computer_clipper.covers(coor)){
            // Civilian Ship - sink immediately
            sinkShip(false, computer_clipper.getSinkCoordinates());
        }else if (computer_submarine.covers(coor)){
            computerHits.add(coor);
        } else {
            computerMisses.add(coor);
        }
    }

    public void shootAtPlayer() {
        int max = 10;
        int min = 1;
        Random random = new Random();
        int randRow = random.nextInt(max - min + 1) + min;
        int randCol = random.nextInt(max - min + 1) + min;

        Coordinate coor = new Coordinate(randRow,randCol);
        playerShot(coor);
    }

    void playerShot(Coordinate coor) {
        if(playerMisses.contains(coor)){
            System.out.println("Dupe");
        }

        if(aircraftCarrier.covers(coor)){
            playerHits.add(coor);
        }else if (battleship.covers(coor)){
            playerHits.add(coor);
        }else if (dinghy.covers(coor)){
            // Civilian Ship - sink immediately
            sinkShip(true, dinghy.getSinkCoordinates());
        }else if (clipper.covers(coor)){
            // Civilian Ship - sink immediately
            sinkShip(true, clipper.getSinkCoordinates());
            playerHits.add(coor);
        }else if (submarine.covers(coor)){
            playerHits.add(coor);
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

    
    private boolean hardMode = false;
    
    public void setDifficulty(String difficulty) {
        // set difficulty to hard, and place computer ships randomly, if user pressed the 'hard' button
        if (difficulty == "hard") {
            hardMode = true;
            placeComputerShipsHard();
        }
    }
    
    // overlaps: expects 8 ints representing the row and column numbers for four total points (two from a ship a, and
    // two from a ship b), and returns a boolean of whether the ships represented by the coordinates overlap
    private boolean overlaps(int x1a, int y1a, int x2a, int y2a, int x1b, int y1b, int x2b, int y2b) {
        int shipALen = 
    }
    
    private void placeComputerShipsHard() {
        // hard mode: the computer places ships in random locations across the board; no overlapping allowed
        // first, place the computer ships in a 
        // investigating hashsets...
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


    private Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,7), false, true);
    private Ship computer_battleship = new Ship("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(6,8), true, true);
    private CivShip computer_clipper = new CivShip("Computer_Clipper",3, new Coordinate(4,1),new Coordinate(4,4));
    private CivShip computer_dinghy = new CivShip("Computer_Dinghy",1, new Coordinate(7,3),new Coordinate(7,5));
    private Ship computer_submarine = new Ship("Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,8), true, true);
    
}