package ca.mcmaster.se2aa4.island.team306;

public class Decider {
    private final Mover mover;
    private final Radar radar;
    private final Aborter aborter;
    private final PhotoScanner photo;
    private Decision decision;
    private final GameTracker tracker;
    private final DecisionQueue queue;

    public Decider(Drone drone, Map map){
        this.queue = new DecisionQueue();
        this.tracker = new GameTracker(drone, map, queue);
        this.aborter = new Aborter(drone, map, tracker);
        this.radar = new Radar(drone, map, queue, tracker);
        this.mover = new Mover(drone, map, queue, tracker);
        this.photo = new PhotoScanner(map, tracker);
    }

    public Decision getNewDecision(){
        tracker.update();
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
        }
        else {
            // No decision was made, abort as a failure
            this.tracker.failMission(); // Fail the mission
            this.aborter.abort(); // Log the failure
            this.decision = Aborter.getDecision();
        }
    }

}