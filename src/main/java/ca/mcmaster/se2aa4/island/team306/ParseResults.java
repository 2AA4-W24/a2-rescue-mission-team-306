package ca.mcmaster.se2aa4.island.team306;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParseResults {
    private static Direction direction;
    private static Decision decision;
    
    public static ParsedResult parse(String results){
        String id;
        int cost;
        ParsedResultBuilder builder = new ParsedResultBuilder();
        JSONObject jsonObject = new JSONObject(results);
        cost = jsonObject.getInt("cost");

        switch (decision) {
            case FLY_FORWARD:
                id = null;
            break;

            case TURN: 
                id = null;  
            break;

            case RADAR:
                int distance = jsonObject.getJSONObject("extras").getInt("range");
                for(int i = 0; i<distance; i++){
                    builder.addValue(MapValue.OCEAN);
                }
                if(jsonObject.getJSONObject("extras").getString("found").equals("GROUND")){
                    builder.addValue(MapValue.GROUND);
                }
                id = null;
            break;

            case PHOTO:
                JSONArray creek_list = jsonObject.getJSONObject("extras").getJSONArray("creeks");
                JSONArray site_list = jsonObject.getJSONObject("extras").getJSONArray("sites");
        
                if(creek_list.length() != 0){
                    id = creek_list.getString(0);
                    builder.addValue(MapValue.CREEK);
                }else if(site_list.length() != 0){
                    id = site_list.getString(0);
                    builder.addValue(MapValue.EMERGENCY_SITE);
                }else{
                    id = null;
                }
            break;

            default:
                id = null;
            break;
        }

        return builder
            .cost(cost)
            .decision(decision)
            .direction(direction)
            .id(id)
            .build();
    }

    public static void setDirection(Direction newDirection){
        direction = newDirection;
    }

    public static void setDecision(Decision newDecision){
        decision = newDecision;
    } 
}
