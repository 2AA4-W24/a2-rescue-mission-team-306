package ca.mcmaster.se2aa4.island.team306;

public class Drone {
    private int energy;
    private Coords position;
    private Coords startPosition;
    private Direction heading;
    private ParsedResult result;

    public Drone(int energy, Direction heading){
        this.energy = energy;
        this.position = this.startPosition = new Coords(0, 0);
        this.heading = heading;
    }

    private void moveStep(Direction direction){
        this.position = this.position.step(direction);
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

    public Coords getStartPosition(){
        return this.startPosition;
    }

    public void updateResult(ParsedResult r){
        this.result = r;
        updateEnergy();
    }

    private void updateEnergy(){
        int cost = this.result.getCost();
        this.energy -= cost;
    }
    
    public ParsedResult getLastResult(){
        return this.result;
    }

}