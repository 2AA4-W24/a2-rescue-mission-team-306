package ca.mcmaster.se2aa4.island.team306;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;
import java.util.ArrayList;

public class ParsedResultBuilder {
    private Decision decision;
    private Direction direction;
    private boolean hasResults;
    private List<MapValue> values;
    private int cost;
    private String id;
    private boolean containsLand;
    private int range;

    ParsedResultBuilder(Direction direction, Decision decision){
        this.direction = direction;
        this.decision = decision;
        this.hasResults = false;
        this.containsLand = false;
        this.range = 0;
    }

    public ParsedResultBuilder populate(String results){
        return populate(new JSONObject(results));
    }

    public ParsedResultBuilder populate(JSONObject results){
        this.cost = results.getInt("cost");

        switch (decision) {
            case Decision.FLY_FORWARD:
                this.id = null;
            break;

            case Decision.TURN: 
                this.id = null;  
            break;

            case Decision.RADAR: 
                this.values = new ArrayList<>();
                this.range = results.getJSONObject("extras").getInt("range");
                for(int i = 0; i<range; i++){
                    addValue(MapValue.OCEAN);
                }
                if(results.getJSONObject("extras").getString("found").equals("GROUND")){
                    addValue(MapValue.GROUND);
                    this.containsLand = true;
                }
                this.id = null;
            break;

            case Decision.PHOTO:
                JSONObject extras = results.getJSONObject("extras");
                JSONArray creek_list = extras.getJSONArray("creeks");
                JSONArray site_list = extras.getJSONArray("sites");
                JSONArray biome_list = extras.getJSONArray("biomes");
        
                if(creek_list.length() != 0){
                    this.id = creek_list.getString(0);
                    addValue(MapValue.CREEK);
                }else if(site_list.length() != 0){
                    this.id = site_list.getString(0);
                    addValue(MapValue.EMERGENCY_SITE);
                }else{
                    this.id = null;
                    addValue(MapValue.ORDINARY_LAND);
                }

                this.containsLand = biome_list.length() > 0;
            break;

            default:
                this.id = null;
            break;
        }
        this.hasResults = true;
        return this;
    }

    private void addValue(MapValue value){
        values.add(value);
    }

    public ParsedResult build(){
        if(hasResults){
            return new ParsedResult(direction, decision, values, id, cost, containsLand, range);
        }
        else {
            throw new AssertionError("cannot build an result-free parsed result");
        }
    }




}