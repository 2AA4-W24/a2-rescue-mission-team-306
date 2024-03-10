package ca.mcmaster.se2aa4.island.team306;

public class PhotoScanner implements Scanner{
    private static final Decision DECISION = new Decision(DecisionType.PHOTO);

    public boolean scan(){
        return false;
    }

    public static Decision getDecision(){
        return DECISION;
    }
}
