package ca.mcmaster.se2aa4.island.team306;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpiralAborter implements Aborter {

    private final Drone drone;
    private final GameMap map;
    private final GameTracker tracker;
    private static final Decision DECISION = new SpiralDecision(SpiralDecisionType.ABORT);

    private static final Logger logger = LogManager.getLogger();

    /**
     * Constructs an Aborter with the specified drone, map, and game tracker instances.
     *
     * @param drone    The drone instance.
     * @param map      The map instance.
     * @param tracker  The game tracker instance.
     */
    public SpiralAborter(Drone drone, GameMap map, GameTracker tracker){
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
            case SpiralGameState.FAILURE:
                logger.info("Our mission failed");
                return true;
            case SpiralGameState.SUCCESS:
                logger.info("Mission accomplished!");
                return true;
            default:
                // Abort based in emergency criteria
        }
        int minEnergy = findMinEnergy();
        if(minEnergy > drone.getEnergy()){
            tracker.failMission();
            logger.info("Our mission failed");
            return true;
        }
        return false;
        
    }

    private int findMinEnergy(){
        Coords pos = drone.getPosition();
        Coords base = map.getBase();
        int distance = (int) pos.distance(base);
        return 2 * (distance + 5);
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
