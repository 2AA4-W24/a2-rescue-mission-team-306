package ca.mcmaster.se2aa4.island.team306;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;
import java.util.ArrayList;

public class ParsedResultBuilder {
    private DecisionType decisionType;
    private Direction direction;
    private boolean hasResults;
    private List<MapValue> values;
    private int cost;
    private String id;

    ParsedResultBuilder(Decision decision){
        this.direction = decision.getDirection();
        this.decisionType = decision.getType();
        this.hasResults = false;
    }

    public ParsedResultBuilder populate(String results){
        return populate(new JSONObject(results));
    }

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
                int distance = results.getJSONObject("extras").getInt("range");
                for(int i = 0; i<distance; i++){
                    addValue(MapValue.OCEAN);
                }
                if(results.getJSONObject("extras").getString("found").equals("GROUND")){
                    addValue(MapValue.GROUND);
                }
                this.id = null;
            break;

            case DecisionType.PHOTO:
                JSONArray creek_list = results.getJSONObject("extras").getJSONArray("creeks");
                JSONArray site_list = results.getJSONObject("extras").getJSONArray("sites");
        
                if(creek_list.length() != 0){
                    this.id = creek_list.getString(0);
                    addValue(MapValue.CREEK);
                }else if(site_list.length() != 0){
                    this.id = site_list.getString(0);
                    addValue(MapValue.EMERGENCY_SITE);
                }else{
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

    public ParsedResult build(){
        if(hasResults){
            return new ParsedResult(direction, decisionType, values, id, cost);
        }
        else {
            throw new AssertionError("cannot build an result-free parsed result");
        }
    }




}