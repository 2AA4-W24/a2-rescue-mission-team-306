package ca.mcmaster.se2aa4.island.team306;

public class Aborter {
    private Drone drone;
    private Map map;

    public boolean abort(){
       if (this.map.currentTile().getType() != MapValue.OCEAN){
            return true;
       }
       return false;
    }
}
