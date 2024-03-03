package ca.mcmaster.se2aa4.island.team306;

public class PhotoScanner implements Scanner{


    private Map map;

    public PhotoScanner(Map map){
        this.map = map;
    }

    public boolean scan(){
        return this.map.currentTile().getType() == MapValue.UNKNOWN;
    }
}
