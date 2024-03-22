package ca.mcmaster.se2aa4.island.team306;

import java.util.Collections;
import java.util.List;

public class ParsedResult{
    private final List<MapValue> values;
    private final Direction direction;
    private final String id;
    private final DecisionType type;
    private final int cost;
    private final int range;

    public static ParsedResultBuilder builder(Decision decision){
        return new ParsedResultBuilder(decision);
    }

    ParsedResult(Direction direction, DecisionType decision, List<MapValue> values,
         String id, int cost, int range){
        this.direction = direction;
        this.type = decision;
        this.values = values;
        this.id = id;
        this.cost = cost;
        this.range = range;
    }

    /**
     * Returns the list of map values associated with the parsed result.
     *
     * @return The list of map values.
     */
    public List<MapValue> getValues(){
        if (values == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(values);
    }

    public Direction getDirection(){
        return this.direction;
    }

    public DecisionType getType(){
        return this.type;
    }

    public String getID(){
        return this.id;
    }

    public int getCost(){
        return this.cost;
    }

    public int getRange(){
        return this.range;
    }
    
    /**
     * Checks if the parsed result indicates the presence of land.
     *
     * @return True if land is found in the parsed result, false otherwise.
     */
    public boolean foundLand(){
        return this.values.getLast().equals(SpiralMapValue.GROUND);
    }
}