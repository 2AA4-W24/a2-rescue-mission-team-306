package ca.mcmaster.se2aa4.island.team306;

import org.json.JSONObject;

public class RawResults {
    /*
     * Return value of ParseResults can be modified if necessary. Arbitrarily set as string
     */

    private JSONObject results;

    public RawResults(JSONObject results){
        this.results = results;
    }

    public MapValue parsePhotoScan(){
        return MapValue.OCEAN;
    }

    public ParsedRadarResult parseRadarScan(){
        return new ParsedRadarResult(Direction.NORTH);
    }

    public int parseStatus(){
        return results.getInt("cost");
    }
}
