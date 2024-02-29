package ca.mcmaster.se2aa4.island.team306;

public class Drone {
    int maxRange;
    int energy;
    Coords position;
    Direction heading;
    ParsedResult result;

    public Drone(int maxRange, int energy, Coords position, Direction heading){
        this.maxRange = maxRange; 
        this.energy = energy;
        this.position = position;
        this.heading = heading;
    }

    private void updateEnergy(){
        int cost = this.result.getCost();
        this.energy -= cost;
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

    public ParsedResult getResult(){
        return this.result;
    }

    public void updateResult(ParsedResult r){
        this.result = r;
        updateEnergy();
    }
}