package ca.mcmaster.se2aa4.island.team306;

public class Mover {
    private Drone drone;
    private Map map;
    private Radar radar;
    private Direction towards;
    private boolean foundLand;
    private boolean turningAround;

    public Mover(Drone drone, Map map, Radar radar){
        this.drone = drone;
        this.map = map;
        this.radar = radar;
        this.towards = drone.getHeading();
        this.foundLand = false;
        this.turningAround = false;
    }

    public boolean oceanMove(){
        if (foundLand){
            return true;
        }
        if (radar.hasFoundLand()){
            this.towards = radar.scanTowards();
            this.foundLand = true;
            return true;
        }
        if (this.turningAround){
            this.towards = this.drone.getHeading().getRight();
            this.turningAround = false;
            return true;
        }
        Coords next = this.drone.getPosition().step(drone.getHeading());
        if (!(this.map.containsCoords(next))){
            this.towards = this.drone.getHeading().getRight();
            this.turningAround = true;
            return true;
        }
        if (radar.checkScan()){
            Direction heading = drone.getHeading();
            if (radar.scanTowards().getBackwards() == heading){
                this.towards = heading;
                return true;
            }
            return false;
        }
        if (radar.atBinaryFork()){
            if (radar.getForwardCounter() > radar.getBackwardCounter()){
                this.towards = drone.getHeading().getLeft();
            }
            else {
                this.towards = drone.getHeading().getRight();
            }
            return true;
        }
        return false;
    }

    public boolean move(){
        if (this.map.currentTile().getType() == MapValue.OCEAN){
            return this.oceanMove();
        }
        return false;
    }

    public Direction goTowards(){
        return this.towards;
    }
}
