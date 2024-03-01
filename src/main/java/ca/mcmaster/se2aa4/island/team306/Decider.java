package ca.mcmaster.se2aa4.island.team306;

public class Decider {
    private Mover mover;
    private Radar radar;
    private Aborter aborter;
    private PhotoScanner photo;
    private Decision decision;
    private Direction direction;
    private Drone drone;

    public Decider(){
        this.decision = Decision.ABORT;
    }

    public Decision getNewDecision(){
        updateDecision();
        return this.decision;
    }

    public Direction getNewDirection(){
        return this.direction;
    }


    private void updateDecision(){
        boolean abortCheck = aborter.abort();
        if (abortCheck){
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
            throw new AssertionError("No direction made");
        }
    }

}