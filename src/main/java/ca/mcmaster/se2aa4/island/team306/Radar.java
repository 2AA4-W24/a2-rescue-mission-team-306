package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.List;

public class Radar implements Scanner{
    private Drone drone;
    private Coords beginPos;
    private Direction beginDirection;
    private RadarState state;

    private boolean foundLand;
    private boolean inFirstCycle;
    private int forwardCounter;
    private int backwardCounter;
    private List<Coords> memory;

    public Radar(Drone drone){
        this.drone = drone;
        this.beginPos = this.drone.getPosition();
        this.beginDirection = this.drone.getHeading();
        this.state = RadarState.FORWARD;
        this.forwardCounter = this.backwardCounter = 0;
        this.foundLand = false;
        this.inFirstCycle = true;
        this.memory = new ArrayList<>();
    }
    public boolean checkScan(){
        if (this.foundLand) {
            return false;
        }
        configureInternals();
        if (this.state == RadarState.EXHAUST) {
            this.memory.add(beginPos);
            return false;
        }
        return true;
    }

    public boolean scan(){
        if (!checkScan()) {
            return false;
        }
        if (this.memory.contains(beginPos)){
            return false;
        }
        Direction scanDirection = scanTowards();
        if (this.beginDirection.getBackwards().equals(scanDirection)){
            return false;
        }
        return true;
    }

    public void acknowledgeRadarResult(){
        ParsedResult result = drone.getLastResult();
        this.foundLand = result.hasLand();

        if (inFirstCycle){
            if (this.state == RadarState.FORWARD){
                this.forwardCounter = result.getRange();
            }
            if (this.state == RadarState.BACKWARD){
                this.backwardCounter = result.getRange();
            }
        }

        if(!this.foundLand){
            this.state = this.state.next();
            if (!inFirstCycle && this.state == RadarState.BACKWARD){
                this.state = RadarState.EXHAUST;
            }
        }

    }

    private void configureInternals(){
        Coords pos = drone.getPosition();
        if (!pos.equals(beginPos)){
            this.inFirstCycle = false;
            this.beginDirection = this.drone.getHeading();
            this.beginPos = pos;
            this.state = RadarState.LEFT; // Only one forward (and backward) scan
        }
    }

    public Direction scanTowards(){
       switch(this.state){
            case RadarState.FORWARD:
                return this.beginDirection;
            case RadarState.LEFT:
                return this.beginDirection.getLeft();
            case RadarState.RIGHT:
                return this.beginDirection.getRight();
            case RadarState.BACKWARD:
                return this.beginDirection.getBackwards();
            case RadarState.EXHAUST:
                throw new IllegalStateException();
            default:
                throw new NullPointerException();
       }
    }

    public int getForwardCounter(){
        return this.forwardCounter;
    }

    public int getBackwardCounter(){
        return this.backwardCounter;
    }

    public RadarState getState(){
        return this.state;
    }

    public boolean hasFoundLand(){
        return this.foundLand;
    }

    public boolean onFirstCycle(){
        return this.inFirstCycle;
    }

    public boolean atBinaryFork(){
        return this.inFirstCycle && this.state == RadarState.EXHAUST;
    }
    
}
