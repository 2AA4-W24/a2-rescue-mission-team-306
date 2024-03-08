package ca.mcmaster.se2aa4.island.team306;

public class Decider {
    private Mover mover;
    private Radar radar;
    private Aborter aborter;
    private PhotoScanner photo;
    private Decision decision;
    private Direction direction;
    private Drone drone;

    public Decider(Drone drone, Map map){
        this.aborter = new Aborter();
        this.radar = new Radar();
        this.mover = new Mover();
        this.photo = new PhotoScanner();
        this.drone = drone;
        this.direction = drone.getHeading();
    }

    public Decision getNewDecision(){
        updateDecision();
        if((this.decision == Decision.FLY_FORWARD) || (this.decision == Decision.TURN)){
            drone.move(direction);
        }
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
            return;
        }
        boolean radarCheck = radar.scan();
        if (radarCheck){
            this.decision = Decision.RADAR;
            return;
        }
        boolean moveCheck = mover.move();
        if (moveCheck){
            this.direction = mover.goTowards();
            if (drone.getHeading() == this.direction) {
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