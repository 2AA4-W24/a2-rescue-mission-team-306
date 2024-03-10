package ca.mcmaster.se2aa4.island.team306;

public class Decider {
    private Mover mover;
    private Radar radar;
    private Aborter aborter;
    private PhotoScanner photo;
    private Decision decision;
    private Direction direction;
    private GameTracker tracker;

    public Decider(Drone drone, Map map){
        this.aborter = new Aborter(drone, map);
        this.radar = new Radar();
        this.mover = new Mover();
        this.photo = new PhotoScanner();
        this.direction = drone.getHeading();
        this.tracker = new GameTracker();
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
            return;
        }
        boolean radarCheck = radar.scan();
        if (radarCheck){
            this.decision = Decision.RADAR;
            return;
        }
        boolean moveCheck = mover.move();
        if (moveCheck){
            this.decision = mover.deriveDecision();
            return;
        }
        else {
            // No decision was made, abort as a failure
            this.decision = Decision.ABORT;
        }
    }

}