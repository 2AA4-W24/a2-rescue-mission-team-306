package ca.mcmaster.se2aa4.island.team306;

public class Map {
    MapValue tiles[][];
    Coords base;
    
    public Map(Coords base){
        
    }

    public void updateStatus(String results){
        String actionResults = ParseResults.parseScan(results);
        //Return value of ParseResults can be modified if necessary. Arbitrarily set as string
    }

}
