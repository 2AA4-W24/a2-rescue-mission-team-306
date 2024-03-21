package ca.mcmaster.se2aa4.island.team306;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class ParsedResultBuilder {
    private DecisionType decisionType;
    private Direction direction;
    private boolean hasResults;
    private List<MapValue> values;
    private int cost;
    private int range;
    private String id;

    private static final Logger logger = LogManager.getLogger();

    ParsedResultBuilder(Decision decision){
        this.decisionType = decision.getType();
        this.direction = decision.getDirection();
        this.hasResults = false;
        this.values = new ArrayList<>();
    }

    /**
     * Populates the builder with results from a JSON string.
     *
     * @param results The JSON string containing decision results.
     * @return This ParsedResultBuilder instance.
     */
    public ParsedResultBuilder populate(String results){
        return populate(new JSONObject(results));
    }

    /**
     * Populates the builder with results from a JSONObject.
     *
     * @param results The JSONObject containing decision results.
     * @return This ParsedResultBuilder instance.
     */
    public ParsedResultBuilder populate(JSONObject results){
        this.cost = results.getInt("cost");

        switch (decisionType) {
            case DecisionType.FLY_FORWARD:
                this.id = null;
            break;

            case DecisionType.TURN: 
                this.id = null;  
            break;

            case DecisionType.RADAR: 
                this.values = new ArrayList<>();
                this.range = results.getJSONObject("extras").getInt("range");
                for(int i = 0; i<range; i++){
                    addValue(MapValue.OCEAN);
                }
                if(results.getJSONObject("extras").getString("found").equals("GROUND")){
                    addValue(MapValue.GROUND);
                }
                else {
                    addValue(MapValue.OUT_OF_RANGE);
                }
                this.id = null;
            break;

            case DecisionType.PHOTO:
                JSONArray creek_list = results.getJSONObject("extras").getJSONArray("creeks");
                JSONArray site_list = results.getJSONObject("extras").getJSONArray("sites");
                JSONArray biomes_list = results.getJSONObject("extras").getJSONArray("biomes");
        
        
                if(!creek_list.isEmpty()){
                    this.id = creek_list.getString(0);
                    addValue(MapValue.CREEK);
                }else if(!site_list.isEmpty()){
                    this.id = site_list.getString(0);
                    addValue(MapValue.EMERGENCY_SITE);
                }else if(biomes_list.length()>1){
                    addValue(MapValue.REGULAR_LAND);
                    this.id = null;
                }else if (!biomes_list.getString(0).equals("OCEAN")){
                    addValue(MapValue.REGULAR_LAND);
                    this.id = null;

                }else{
                    addValue(MapValue.SCANNED_OCEAN);
                    this.id = null;
                }
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

    /**
     * Builds and returns a ParsedResult object based on the populated builder.
     *
     * @return The built ParsedResult object.
     * @throws AssertionError if the builder does not have any results.
     */
    public ParsedResult build(){
        if(hasResults){
            return new ParsedResult(direction, decisionType, values, id, cost, range);
        }
        else {
            throw new AssertionError("cannot build an result-free parsed result");
        }
    }




}