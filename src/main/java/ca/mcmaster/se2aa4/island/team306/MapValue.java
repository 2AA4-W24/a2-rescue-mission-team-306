package ca.mcmaster.se2aa4.island.team306;

public enum MapValue {
    // General
    UNKNOWN,
    OCEAN,
    OUT_OF_RANGE,
    GROUND,

    // Relevant here
    REGULAR_LAND,
    SCANNED_OCEAN,
    CREEK,
    EMERGENCY_SITE;

    public boolean isLand(){
        return switch (this) {
            case UNKNOWN, OCEAN, OUT_OF_RANGE, SCANNED_OCEAN -> false;
            default -> true;
        };
    }

    public boolean scanned(){
        return switch (this) {
            case UNKNOWN, OCEAN, OUT_OF_RANGE, GROUND -> false;
            default -> true;
        };
    }

   
}