package ca.mcmaster.se2aa4.island.team306;

public class SpiralDecision implements Decision {
    private final SpiralDecisionType type;
    private final Direction direction;

    /**
     * Constructs a Decision object with the given decision type.
     *
     * @param type The type of decision.
     */
    SpiralDecision(SpiralDecisionType type){
        this.type = type;
        this.direction = null;
    }

    SpiralDecision(SpiralDecisionType type, Direction direction){
        this.type = type;
        this.direction = direction;
    }
    
    /**
     * Gets the type of the decision.
     *
     * @return The decision type.
     */
    public SpiralDecisionType getType(){
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

    @Override
    public String toString(){
        return String.format("Decision(%s, %s)", this.type, this.direction);
    }


}
