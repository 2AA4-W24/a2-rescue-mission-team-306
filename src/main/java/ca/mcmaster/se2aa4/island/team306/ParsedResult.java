package ca.mcmaster.se2aa4.island.team306;

import java.util.Collections;
import java.util.List;

public class ParsedResult{
    private final List<MapValue> values;
    private final Direction direction;
    private final String id;
    private final Decision decision;
    private final int cost;
    private final boolean containsLand;
    private final int range;

    public static ParsedResultBuilder builder(Direction direction, Decision decision){
        return new ParsedResultBuilder(direction, decision);
    }

    ParsedResult(Direction direction, Decision decision, List<MapValue> values, String id,
     int cost, boolean containsLand, int range){
        this.direction = direction;
        this.decision = decision;
        this.values = values;
        this.id = id;
        this.cost = cost;
        this.containsLand = containsLand;
        this.range = range;
    }

    public List<MapValue> getValues(){
        if (values == null) return null;
        return Collections.unmodifiableList(values);
    }

    public boolean hasLand(){
        return this.containsLand;
    }

    public int getRange(){
        return this.range;
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