package ca.mcmaster.se2aa4.island.team306;

public class Mover {
    private Drone drone;
    private Map map;
    private Radar radar;
    private Direction towards;

    public Mover(Drone drone, Map map, Radar radar){
        this.drone = drone;
        this.map = map;
        this.radar = radar;
        this.towards = drone.getHeading();
    }

    public boolean move(){
        if (this.map.currentTile().getType() == MapValue.OCEAN){
            if (radar.hasFoundLand()){
                this.towards = radar.scanTowards();
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
        }
        return false;
    }

    public Direction goTowards(){
        return this.towards;
    }
}
