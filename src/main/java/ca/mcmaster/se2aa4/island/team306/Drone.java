package ca.mcmaster.se2aa4.island.team306;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

public class Drone {
    private int energy;
    private Coords position;
    private Direction heading;
    private ParsedResult result;
    private final Logger logger = LogManager.getLogger();

    public Drone(int energy, Direction heading){
        this.energy = energy;
        this.position = new Coords(0, 0);
        this.heading = heading;
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
        if(result.getType() == DecisionType.FLY_FORWARD || result.getType() == DecisionType.TURN){
            move(result.getDirection());
            logger.info(String.format("(%d, %d)", position.x, position.y));
        }
    }

    private void updateEnergy(){
        int cost = this.result.getCost();
        this.energy -= cost;
    }

   

}