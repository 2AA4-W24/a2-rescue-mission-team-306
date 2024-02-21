package ca.mcmaster.se2aa4.island.team306;

public class Decider {
    private Mover mover;
    private Radar radar;
    private Aborter aborter;
    private PhotoScanner photo;
    private Decision decision;
    private Direction jsonDirection;
    private Direction currentDirection;
    private Drone drone;

    public Decider(){
        this.decision = Decision.ABORT;
    }

    public Decision getDecision(){
        updateDecision();
        return this.decision;
    }

    public Direction getJsonDirection(){
        return jsonDirection;
    }

    public Direction getCurrentDirection(){
        return currentDirection;
    }

    public void updateRawResults(RawResults results){
        drone.updateRawResults(results);
    }

    private void updateDecision(){
        boolean abortCheck = aborter.abort();
        boolean moveCheck = mover.move();
        boolean radarCheck = radar.scan();
        boolean photoCheck = photo.scan();
        if (abortCheck){
            this.decision = Decision.ABORT;
            return;
        }
        if (moveCheck){
            Direction towards = mover.goTowards();
            this.jsonDirection = towards;
            if (this.currentDirection == towards){
                this.decision = Decision.FLY_FORWARD;
            }
            else {
                this.currentDirection = towards;
                this.decision = Decision.TURN;
            }
            return;
        }
        if (radarCheck){
            this.jsonDirection = radar.scanTowards();
            this.decision = Decision.RADAR;
            return;
        }
        if (photoCheck){
            this.decision = Decision.PHOTO;
            return;
        }
        throw new AssertionError("No decision made");
    }
}
