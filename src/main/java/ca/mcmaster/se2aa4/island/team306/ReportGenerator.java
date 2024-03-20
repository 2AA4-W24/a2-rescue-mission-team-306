package ca.mcmaster.se2aa4.island.team306;

import java.util.List;

public class ReportGenerator {
    private String creekId;

    public ReportGenerator(){
        this.creekId = null;
    }

    public String getCreekId(){
        return creekId;
    }

    public void setCreekId(String id){
        this.creekId = id;
    }

    public String deliverReport(){
        return creekId == null ? "no creek found" : String.format("closest creek id: %s", creekId);
    }
}
