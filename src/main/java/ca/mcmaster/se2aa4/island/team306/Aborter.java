package ca.mcmaster.se2aa4.island.team306;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Aborter {
    private final Drone drone;
    private final Map map;
    private final GameTracker tracker;
    private static final Decision DECISION = new Decision(DecisionType.ABORT);

    private static final Logger logger = LogManager.getLogger();

    public Aborter(Drone drone, Map map, GameTracker tracker){
        this.drone = drone;
        this.map = map;
        this.tracker = tracker;
    }
    public boolean abort(){
        switch(tracker.getState()){
            case GameState.FAILURE:
                logger.info("Our mission failed");
                return true;
            case GameState.SUCCESS:
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

    public static Decision getDecision(){
        return DECISION;
    }

}
