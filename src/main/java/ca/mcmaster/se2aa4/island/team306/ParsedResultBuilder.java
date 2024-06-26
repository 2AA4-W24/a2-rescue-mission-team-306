package ca.mcmaster.se2aa4.island.team306;


import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;
import java.util.ArrayList;

public class ParsedResultBuilder {
    private final DecisionType decisionType;
    private final Direction direction;
    private boolean hasResults;
    private List<MapValue> values;
    private int cost;
    private int range;
    private String id;

    public ParsedResultBuilder(Decision decision){
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
            case SpiralDecisionType.RADAR: 
                this.values = new ArrayList<>();
                this.range = results.getJSONObject("extras").getInt("range");
                for(int i = 0; i<range; i++){
                    addValue(SpiralMapValue.OCEAN);
                }
                if("GROUND".equals(results.getJSONObject("extras").getString("found"))){
                    addValue(SpiralMapValue.GROUND);
                }
                else {
                    addValue(SpiralMapValue.OUT_OF_RANGE);
                }
                this.id = null;
            break;

            case SpiralDecisionType.PHOTO:
                JSONArray creekList = results.getJSONObject("extras").getJSONArray("creeks");
                JSONArray siteList = results.getJSONObject("extras").getJSONArray("sites");
                JSONArray biomesList = results.getJSONObject("extras").getJSONArray("biomes");
        
        
                if(!creekList.isEmpty()){
                    this.id = creekList.getString(0);
                    addValue(SpiralMapValue.CREEK);
                }else if(!siteList.isEmpty()){
                    this.id = siteList.getString(0);
                    addValue(SpiralMapValue.EMERGENCY_SITE);
                }else if(biomesList.length()>1){
                    addValue(SpiralMapValue.REGULAR_LAND);
                    this.id = null;
                }else if (!"OCEAN".equals(biomesList.getString(0))){
                    addValue(SpiralMapValue.REGULAR_LAND);
                    this.id = null;

                }else{
                    addValue(SpiralMapValue.SCANNED_OCEAN);
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