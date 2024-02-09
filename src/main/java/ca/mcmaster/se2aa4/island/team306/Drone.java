package ca.mcmaster.se2aa4.island.team306;

public class Drone {
    int maxRange;
    int energy;
    Coords position;
    Direction heading;

    public Drone(int maxRange, int energy, Coords position, Direction heading){
        this.maxRange = maxRange; 
        this.energy = energy;
        this.position = position;
        this.heading = heading;
    }

    public void updateStatus(String results){
        String newInfo = ParseResults.parseStatus(results);
        //Return value of ParseResults can be modified if necessary. Arbitrarily set as string
    }

    
}