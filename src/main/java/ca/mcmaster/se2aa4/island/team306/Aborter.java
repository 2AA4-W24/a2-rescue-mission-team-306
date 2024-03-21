package ca.mcmaster.se2aa4.island.team306;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Aborter {
    private Drone drone;
    private Map map;
    private GameTracker tracker;
    private int min_energy;
    private static final Decision DECISION = new Decision(DecisionType.ABORT); // The decision to abort the mission


    private static final Logger logger = LogManager.getLogger();

    /**
     * Constructs an Aborter with the specified drone, map, and game tracker instances.
     *
     * @param drone    The drone instance.
     * @param map      The map instance.
     * @param tracker  The game tracker instance.
     */
    public Aborter(Drone drone, Map map, GameTracker tracker){
        min_energy = 0;
        this.drone = drone;
        this.map = map;
        this.tracker = tracker;
    }

    /**
     * Determines whether the mission should be aborted.
     *
     * @return True if the mission should be aborted, false otherwise.
     */
    public boolean abort(){
        switch(tracker.getState()){
            case GameState.FAILURE:
                logger.fatal("Our mission failed");
                return true;
            case GameState.SUCCESS:
                logger.info("Mission accomplished!");
                return true;
            default:
                // Abort based in emergency criteria
        }
        min_energy = findMinEnergy();
        if(min_energy > drone.getEnergy()){
            tracker.failMission();
            logger.fatal("Our mission failed");
            return true;
        }
        return false;
        
    }

    private int findMinEnergy(){
        Coords pos = drone.getPosition();
        Coords base = map.getBase();
        int distance = (int) pos.distance(base);
        return (2*(distance + 5));
    }

    /**
     * Gets the decision to abort the mission.
     *
     * @return The decision to abort the mission.
     */
    public static Decision getDecision(){
        return DECISION;
    }

}
