package ca.mcmaster.se2aa4.island.team306;

import org.json.JSONObject;

public class ParseResults {
    private static Direction direction;
    private static Decision decision;
    private static String id;
    
    public static ParsedResult parseResult(String results){
        return new ParsedResult(direction, decision, results);
    }

    public static void setDirection(Direction newDirection){
        direction = newDirection;
    }

    public static void setDecision(Decision newDecision){
        decision = newDecision;
    }

    public static void setID(String newID){
        id = newID;
    }
}
