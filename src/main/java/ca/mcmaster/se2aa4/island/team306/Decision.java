package ca.mcmaster.se2aa4.island.team306;

public class Decision {
    private final DecisionType type;
    private final Direction direction;

    /**
     * Constructs a Decision object with the given decision type.
     *
     * @param type The type of decision.
     */
    public Decision(DecisionType type){
        this.type = type;
        this.direction = null;
    }

    Decision(DecisionType type, Direction direction){
        this.type = type;
        this.direction = direction;
    }
    
    /**
     * Gets the type of the decision.
     *
     * @return The decision type.
     */
    public DecisionType getType(){
        return this.type;
    }

    /**
     * Gets the direction associated with the decision.
     *
     * @return The direction of the decision, or null if no direction is associated.
     */
    public Direction getDirection(){
        return this.direction;
    }


}
