package ca.mcmaster.se2aa4.island.team306;

import java.util.List;

public class ReportGenerator {
    private Map map;
    private Drone drone;
    private List<DecisionType> decisions;
    private List<Coords> creekCoords;

    public ReportGenerator(Map map, Drone drone, List<DecisionType> decisions, List<Coords> creekCoords) {
        this.map = map;
        this.drone = drone;
        this.decisions = decisions;
        this.creekCoords = creekCoords;
    }

    public String deliverFinalReport() {
        StringBuilder report = new StringBuilder("Final Report\n");

        // Check if the drone reached the creek
        boolean missionSuccess = checkMissionSuccess();

        if (missionSuccess) {
            report.append("Mission Success!\n");
            report.append("Creek Coordinates: ").append(creekCoords).append("\n");
        } else {
            report.append("Mission Failure!\n");
        }

        return report.toString();
    }

    private boolean checkMissionSuccess() {
        Coords currentCoords = drone.getPosition(); // Check if the current coordinates of the drone match any of the creek coordinates
        return creekCoords.contains(currentCoords);
    }
}
