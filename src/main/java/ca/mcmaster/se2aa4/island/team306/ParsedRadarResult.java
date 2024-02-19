package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParsedRadarResult{
    private final List<MapValue> values;
    private final Direction direction;

    public ParsedRadarResult(Direction direction){
        this.direction = direction;
        this.values = new ArrayList<>();
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
}