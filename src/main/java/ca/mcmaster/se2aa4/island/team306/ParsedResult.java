package ca.mcmaster.se2aa4.island.team306;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsedResult{
    private final List<MapValue> values;
    private final Direction direction;
    private final String id;
    private final Decision decision;
    private final int cost;

    public static ParsedResultBuilder builder(Direction direction, Decision decision){
        return new ParsedResultBuilder(direction, decision);
    }

    ParsedResult(Direction direction, Decision decision, List<MapValue> values, String id, int cost){
        this.direction = direction;
        this.decision = decision;
        this.values = values;
        this.id = id;
        this.cost = cost;
    }

    public List<MapValue> getValues(){
        if (values == null) return null;
        return Collections.unmodifiableList(values);
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