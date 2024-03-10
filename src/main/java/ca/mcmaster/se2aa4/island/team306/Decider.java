package ca.mcmaster.se2aa4.island.team306;

public class Decider {
    private Mover mover;
    private Radar radar;
    private Aborter aborter;
    private PhotoScanner photo;
    private Decision decision;
    private GameTracker tracker;
    private DecisionQueue queue;

    public Decider(Drone drone, Map map){
        this.tracker = new GameTracker();
        this.aborter = new Aborter(drone, map, this.tracker);
        this.radar = new Radar();
        this.mover = new Mover();
        this.photo = new PhotoScanner();
        this.queue = new DecisionQueue();
    }

    public Decision getNewDecision(){
        updateDecision();
        return this.decision;
    }


    private void updateDecision(){
        boolean abortCheck = aborter.abort();
        if (abortCheck){
            this.decision = Aborter.getDecision();
            return;
        }
        if (!queue.isEmpty()){
            this.decision = queue.dequeue();
            return;
        }
        boolean photoCheck = photo.scan();
        if (photoCheck){
            this.decision = PhotoScanner.getDecision();
            return;
        }
        boolean radarCheck = radar.scan();
        if (radarCheck){
            this.decision = radar.deriveDecision();
            return;
        }
        boolean moveCheck = mover.move();
        if (moveCheck){
            this.decision = mover.deriveDecision();
            return;
        }
        else {
            // No decision was made, abort as a failure
            this.tracker.failMission(); // Fail the mission
            this.aborter.abort(); // Log the failure
            this.decision = Aborter.getDecision();
        }
    }

}