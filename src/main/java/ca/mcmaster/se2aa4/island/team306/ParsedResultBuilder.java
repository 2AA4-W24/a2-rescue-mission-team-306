package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsedResultBuilder {
    private List<MapValue> values = new ArrayList<>();
    private Direction direction = null;
    private String id = null;
    private Decision decision = null;
    private int cost = 0;

    public ParsedResultBuilder(){
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

    public ParsedResultBuilder direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public ParsedResultBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ParsedResultBuilder decision(Decision decision) {
        this.decision = decision;
        return this;
    }

    public ParsedResultBuilder cost(int cost) {
        this.cost = cost;
        return this;
    }

    public ParsedResult build(){
        return new ParsedResult(direction, decision, cost, values, id);
    }

    
}
