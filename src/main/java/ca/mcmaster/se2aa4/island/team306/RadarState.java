package ca.mcmaster.se2aa4.island.team306;

public enum RadarState {
    FORWARD, LEFT, RIGHT, BACKWARD, EXHAUST;

    public RadarState next(){
        switch(this){
            case FORWARD:
                return LEFT;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return BACKWARD;
            case BACKWARD:
                return EXHAUST;
            case EXHAUST:
                throw new IllegalStateException();
            default:
                throw new NullPointerException();
        }
    }
}
