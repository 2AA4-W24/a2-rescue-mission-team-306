package ca.mcmaster.se2aa4.island.team306;

import org.json.JSONObject;

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

    public static int parseStatus(String results){
        JSONObject jsonObject = new JSONObject(results);
        return jsonObject.getInt("cost");
    }
}
