package ca.mcmaster.se2aa4.island.team306;

import java.util.List;

public class ReportGenerator {
    private Map map;
    private Drone drone;

    public ReportGenerator(Map map, Drone drone) {
        this.map = map;
        this.drone = drone;
    }

    public String deliverFinalReport() {
        StringBuilder report = new StringBuilder("Final Report\n");

        boolean missionSuccess = checkMissionSuccess();

        if (missionSuccess) {
            report.append("Mission Success!\n");
            Coords creekCoords = findCreekCoords();
            if (creekCoords != null) {
                Tile creekTile = map.getTileAt(creekCoords);
                if (creekTile != null) {
                    String creekID = creekTile.getID();
                    report.append("Creek ID: ").append(creekID).append("\n");
                } else {
                    report.append("Creek ID not found!\n");
                }
            }
        } else {
            report.append("Mission Failure!\n");
        }

        return report.toString();
    }

    private boolean checkMissionSuccess() {
        Coords currentCoords = drone.getPosition();
        return map.checkCoords(currentCoords) == MapValue.CREEK;
    }

    private Coords findCreekCoords() {
        return map.findNearestTile(MapValue.CREEK);
    }
}
