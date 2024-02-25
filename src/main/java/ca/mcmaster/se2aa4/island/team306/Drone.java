package ca.mcmaster.se2aa4.island.team306;

import org.json.JSONObject;

public class Drone {
    int maxRange;
    int energy;
    Coords position;
    Direction heading;
    private RawResults rawResults;

    public Drone(int maxRange, int energy, Coords position, Direction heading){
        this.maxRange = maxRange; 
        this.energy = energy;
        this.position = position;
        this.heading = heading;
    }

    public void updateRawResults(RawResults results){
        this.rawResults = results;
    }

    public void updateEnergy(){
        int cost = rawResults.parseStatus();
        this.energy -= cost;
    }

    private void moveStep(Direction direction){
        switch (direction) {
            case Direction.NORTH:
                this.position = new Coords(position.x, position.y+1);
                break;
            case Direction.SOUTH:
                this.position = new Coords(position.x, position.y-1);
                break;
            case Direction.EAST:
                this.position = new Coords(position.x+1, position.y);
                break;
            case Direction.WEST:
                this.position = new Coords(position.x-1, position.y);
                break;
        
        }
    }

    public void move(Direction direction){
        moveStep(heading);
        if(direction != heading){
            this.heading = direction;
            moveStep(heading);
        }
    }

    public int getEnergy(){
        return this.energy;
    }

    public Direction getHeading(){
        return this.heading;
    }

    public Coords getPosition(){
        return this.position;
    }

    
}