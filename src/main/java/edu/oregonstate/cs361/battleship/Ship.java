package edu.oregonstate.cs361.battleship;

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
}
