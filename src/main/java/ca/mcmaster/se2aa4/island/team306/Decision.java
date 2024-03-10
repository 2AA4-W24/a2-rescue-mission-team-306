package ca.mcmaster.se2aa4.island.team306;

public class Decision {
    private final DecisionType type;
    private final Direction direction;

    public Decision(DecisionType type){
        this.type = type;
        this.direction = null;
    }

    Decision(DecisionType type, Direction direction){
        this.type = type;
        this.direction = direction;
    }
    
    public DecisionType getType(){
        return this.type;
    }

    public Direction getDirection(){
        return this.direction;
    }


}
