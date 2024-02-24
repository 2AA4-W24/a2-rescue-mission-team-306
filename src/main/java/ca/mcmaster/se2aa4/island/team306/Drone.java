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
        this.position = new Coords(0, 0);
        this.heading = heading;
    }

    public void updateRawResults(RawResults results){
        this.rawResults = results;
    }

    public void updateEnergy(){
        int cost = rawResults.parseStatus();
        this.energy -= cost;
    }

    public void move(Direction direction){
        switch (heading) {
            case NORTH:
                position.y++;
                break;
            case SOUTH:
                position.y--;
                break;
            case EAST:
                position.x++;
                break;
            default:
                position.x--;
                break;
        
        }
        if(direction != heading){
            this.heading = direction;
            switch (heading) {
                case NORTH:
                    position.y++;
                    break;
                case SOUTH:
                    position.y--;
                    break;
                case EAST:
                    position.x++;
                    break;
                default:
                    position.x--;
                    break;
            }
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