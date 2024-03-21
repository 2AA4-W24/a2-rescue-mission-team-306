package ca.mcmaster.se2aa4.island.team306;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

public class Drone {
    private int energy;
    private Coords position;
    private Direction heading;
    private ParsedResult result;
    private final Logger logger = LogManager.getLogger();

    /**
     * Constructs a Drone object with the specified energy level and heading direction.
     *
     * @param energy  The initial energy level of the drone.
     * @param heading The initial heading direction of the drone.
     */
    public Drone(int energy, Direction heading){
        this.energy = energy;
        this.position = new Coords(0, 0);
        this.heading = heading;
    }

    private void moveStep(Direction direction){
        position = position.step(direction);
    }

    /**
     * Moves the drone in the specified direction, updates its position and heading, and adjusts energy accordingly.
     *
     * @param direction The direction in which to move the drone.
     */
    public void move(Direction direction){
        moveStep(heading);
        if(direction != heading){
            this.heading = direction;
            moveStep(heading);
        }
    }

    /**
     * Retrieves the current energy level of the drone.
     *
     * @return The current energy level of the drone.
     */
    public int getEnergy(){
        return this.energy;
    }

    /**
     * Retrieves the current heading direction of the drone.
     *
     * @return The current heading direction of the drone.
     */
    public Direction getHeading(){
        return this.heading;
    }

    /**
     * Retrieves the current position of the drone.
     *
     * @return The current position of the drone.
     */
    public Coords getPosition(){
        return this.position;
    }

    /**
     * Updates the drone's parsed result and adjusts its energy level and position based on the result.
     *
     * @param r The parsed result obtained from executing a decision.
     */
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