package ca.mcmaster.se2aa4.island.team306;

public class Mover {
    private Drone drone;
    private Map map;

    public Mover(){}

    private boolean shouldMove(){
        return false;
    }

    public boolean move(){
        if (shouldMove()){
            Direction d = goTowards();
            if (d == drone.getHeading().getBackwards()){
                return false;
            }
            drone.move(goTowards());
            return true;
        }
        return false;
    }

    public Direction goTowards(){
        return Direction.SOUTH;
    }

    public Decision deriveDecision(){
        Direction d = this.goTowards();
        if (drone.getHeading() == d) {
            return Decision.FLY_FORWARD;
        }
        else {
            return Decision.TURN;
        }
    }


}
