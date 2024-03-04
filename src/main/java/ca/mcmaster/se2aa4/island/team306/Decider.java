package ca.mcmaster.se2aa4.island.team306;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Decider {
    private Mover mover;
    private Radar radar;
    private Aborter aborter;
    private PhotoScanner photo;
    private Decision decision;
    private Direction direction;

    private final Logger logger = LogManager.getLogger();

    public Decider(Drone drone, Map map){
        this.aborter = new Aborter(drone, map);
        this.photo = new PhotoScanner(map);
        this.radar = new Radar(drone);
        this.mover = new Mover(drone, map, radar);
    }

    public Decision getNewDecision(){
        updateDecision();
        logger.info(this.decision);
        return this.decision;
    }

    public Direction getNewDirection(){
        return this.direction;
    }


    private void updateDecision(){
        boolean abortCheck = aborter.abort();
        if (abortCheck){
           logger.info("We found land!");
            this.decision = Decision.ABORT;
            return;
        }
        boolean photoCheck = photo.scan();
        if (photoCheck){
            this.decision = Decision.PHOTO;
        }
        boolean radarCheck = radar.scan();
        if (radarCheck){
            this.decision = Decision.RADAR;
            return;
        }
        boolean moveCheck = mover.move();
        if (moveCheck){
            if (mover.goTowards() == this.direction) {
                this.decision = Decision.FLY_FORWARD;
            }
            else {
                this.decision = Decision.TURN;
            }
            return;
        }
        else {
            this.decision = Decision.ABORT;
            logger.warn("Aborting here :(");
            return;
        }
    }

}