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

    public ParsedResult(Direction direction, Decision decision, int cost, List<MapValue> values, String id){
        this.direction = direction;
        this.decision = decision;
        this.cost = cost;
        this.values = values;
        this.id = id;
    }

    public List<MapValue> getValues(){
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