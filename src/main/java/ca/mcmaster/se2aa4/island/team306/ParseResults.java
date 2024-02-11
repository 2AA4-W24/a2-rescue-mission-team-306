package ca.mcmaster.se2aa4.island.team306;


public class ParseResults {
    /*
     * Return value of ParseResults can be modified if necessary. Arbitrarily set as string
     */

    public static MapValue parsePhotoScan(String results){
        return MapValue.OCEAN;
    }

    public static ParsedRadarResult parseRadarScan(String results){
        return new ParsedRadarResult(Direction.NORTH);
    }

    public static String parseStatus(String results){
        return "";
    }
}
