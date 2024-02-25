package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsedResult{
    private List<MapValue> values = new ArrayList<>();
    private final Direction direction;
    private final String id;
    private final Decision decision;
    private final int cost;

    public ParsedResult(Direction direction, Decision decision, String results){
        this.direction = direction;
        this.decision = decision;

        JSONObject jsonObject = new JSONObject(results);
        this.cost = jsonObject.getInt("cost");

        switch (decision) {
            case FLY_FORWARD:
                this.id = null;
            break;

            case TURN: 
                this.id = null;  
            break;

            case RADAR:
                int distance = jsonObject.getJSONObject("extras").getInt("range");
                for(int i = 0; i<distance; i++){
                    addValue(MapValue.OCEAN);
                }
                if(jsonObject.getJSONObject("extras").getString("found").equals("GROUND")){
                    addValue(MapValue.GROUND);
                }
                this.id = null;
            break;

            case PHOTO:
                JSONArray creek_list = jsonObject.getJSONObject("extras").getJSONArray("creeks");
                JSONArray site_list = jsonObject.getJSONObject("extras").getJSONArray("sites");
        
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
    }

    public List<MapValue> getValues(){
        return Collections.unmodifiableList(values);
    }

    public void addValue(MapValue value){
        switch(value){
            case MapValue.OCEAN:
            case MapValue.GROUND:
                values.add(value);
                break;
            default:
                throw new IllegalArgumentException(value.toString());
        }
    }

    public Direction getDirection(){
        return this.direction;
    }

    public Decision getDecision(){
        return this.decision;
    }

    public String getID(){
        return this.id;
    }

    public int getCost(){
        return this.cost;
    }
}