package ca.mcmaster.se2aa4.island.team306;

public class SpiralDecider implements Decider{
    private final Mover mover;
    private final RadarScanner radar;
    private final Aborter aborter;
    private final PhotoScanner photo;
    private Decision decision;
    private final SpiralGameTracker tracker;
    private final DecisionQueue queue;

    /**
     * Constructs a Decider object with the given drone and map.
     *
     * @param drone The drone object.
     * @param map   The map object.
     */
    public SpiralDecider(Drone drone, GameMap map){
        this.queue = new DecisionQueue();
        this.tracker = new SpiralGameTracker(drone, map, queue);
        this.aborter = new SpiralAborter(drone, map, tracker);
        this.radar = new SpiralRadar(drone, map, queue, tracker);
        this.mover = new SpiralMover(drone, map, queue, tracker);
        this.photo = new SpiralPhotoScanner(map, tracker);
    }

    /**
     * Gets a new decision for the drone based on the current game state.
     *
     * @return The decision for the drone to execute.
     */
    public Decision getNewDecision(){
        tracker.update();
        updateDecision();
        return this.decision;
    }


    private void updateDecision(){
        boolean abortCheck = aborter.abort();
        if (abortCheck){
            this.decision = SpiralAborter.getDecision();
            return;
        }
        if (!queue.isEmpty()){
            this.decision = queue.dequeue();
            return;
        }
        boolean photoCheck = photo.scan();
        if (photoCheck){
            this.decision = SpiralPhotoScanner.getDecision();
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
            this.decision = SpiralAborter.getDecision();
        }
    }

}