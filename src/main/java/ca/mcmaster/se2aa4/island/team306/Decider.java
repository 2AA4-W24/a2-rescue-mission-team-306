package ca.mcmaster.se2aa4.island.team306;

public class Decider {
    private Mover mover;
    private Radar radar;
    private Aborter aborter;
    private PhotoScanner photo;
    private Decision decision;
    private Direction direction;

    public Decider(){
        this.decision = Decision.ABORT;
    }

    public Decision getDecision(){
        updateDecision();
        return this.decision;
    }

    private void updateDecision(){
        boolean abortCheck = aborter.abort();
        boolean moveCheck = mover.move();
        boolean radarCheck = radar.scan();
        boolean photoCheck = photo.scan();
        this.decision = Decision.ABORT;

        if (abortCheck){
            this.decision = Decision.ABORT;
        } else if (moveCheck){
            this.decision = Decision.MOVE;
        } else if (radarCheck){
            this.decision = Decision.RADAR_SCAN;
        } else if (photoCheck){
            this.decision = Decision.PHOTO_SCAN;
        }
    }

    private Direction getDirection(Decision decision){
        this.decision = decision;

        if (decision == Decision.MOVE){
            this.direction = mover.goTowards();
        } else if (decision == Decision.RADAR_SCAN){
            this.direction = radar.goTowards();
        }

        return direction;
    }
}
