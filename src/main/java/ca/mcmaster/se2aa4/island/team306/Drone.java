package ca.mcmaster.se2aa4.island.team306;

public class Drone {
    private int maxRange;
    private int energy;
    private Coords position;
    private Direction heading;
    private ParsedResult result;
    private GameState state;

    public Drone(int energy, Direction heading){
        this.maxRange = 0; 
        this.energy = energy;
        this.position = new Coords(0, 0);
        this.heading = heading;
        this.state = GameState.SETUP;
    }

    private void moveStep(Direction direction){
        position = position.step(direction);
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

    public GameState getState(){
        return this.state;
    }

}