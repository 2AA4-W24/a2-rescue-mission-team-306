package ca.mcmaster.se2aa4.island.team306;

import org.json.JSONObject;

public class Drone {
    private int maxRange;
    private int energy;
    private Coords position;
    private Direction heading;
    private ParsedResult result;

    public Drone(int energy, Direction heading){
        this.maxRange = 0; 
        this.energy = energy;
        this.position = new Coords(0, 0);
        this.heading = heading;
    }

    private void moveStep(Direction direction){
        switch(direction){
            case NORTH:
                position = position.offset(0, 1);
                break;
            case SOUTH:
                position = position.offset(0, -1);
                break;
            case EAST:
                position = position.offset(1, 0);
                break;
            case WEST:
                position = position.offset(-1, 0);
                break;
            default:
                throw new NullPointerException();
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

    public void updateResult(ParsedResult r){
        this.result = r;
        updateEnergy();
    }

    private void updateEnergy(){
        int cost = this.result.getCost();
        this.energy -= cost;
    }

}