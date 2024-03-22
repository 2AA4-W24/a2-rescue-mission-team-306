package ca.mcmaster.se2aa4.island.team306;

public enum SpiralMapValue implements MapValue {
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

    /**
     * Checks if the map value represents land.
     *
     * @return True if the map value represents land, false otherwise.
     */
    public boolean isLand(){
        return switch (this) {
            case UNKNOWN, OCEAN, OUT_OF_RANGE, SCANNED_OCEAN -> false;
            default -> true;
        };
    }

    /**
     * Checks if the map value has been scanned.
     *
     * @return True if the map value has been scanned, false otherwise.
     */
    public boolean scanned(){
        return switch (this) {
            case UNKNOWN, OCEAN, OUT_OF_RANGE, GROUND -> false;
            default -> true;
        };
    }

   
}