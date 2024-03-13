package ca.mcmaster.se2aa4.island.team306;

public enum MapValue {
    // General
    UNKNOWN,
    OCEAN,
    OUT_OF_RANGE,
    GROUND,

    // Relevant here
    REGULAR_LAND,
    CREEK,
    EMERGENCY_SITE;

    public boolean isLand(){
        switch(this){
            case UNKNOWN:
            case OCEAN:
            case OUT_OF_RANGE:
                return false;
            default:    
                return true;
        }
    }

   
}