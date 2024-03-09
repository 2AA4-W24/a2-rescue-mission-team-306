package ca.mcmaster.se2aa4.island.team306;

public class Aborter {
    private Drone drone;
    private Map map;
    private int min_energy;

    public Aborter(Drone drone, Map map){
        min_energy = 0;
        this.drone = drone;
        this.map = map;

    }
    public boolean abort(){
        min_energy = findMinEnergy();
        if(min_energy > drone.getEnergy()){
            return true;
        }
        return false;
        
    }
    private int findMinEnergy(){
        Coords pos = drone.getPosition();
        Coords base = map.getBase();
        int distance = (int) pos.distance(base);
        return (2*(distance + 5));
    }
}
