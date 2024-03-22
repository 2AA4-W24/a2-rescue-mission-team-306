package ca.mcmaster.se2aa4.island.team306;

public class ReportGenerator {
    private String creekId;

    public ReportGenerator(){ //constructor
        this.creekId = null;
    }

    /**
     * Get the ID of the nearest creek.
     *
     * @return the ID of the nearest creek.
     */
    public String getCreekId(){
        return creekId;
    }

  /**
     * Sets the ID of the closest creek.
     *
     * @param id The ID of the closest creek.
     */
    public void setCreekId(String id){
        this.creekId = id;
    }

    /**
     * Generates and delivers a report based on the closest creek ID.
     *
     * @return A report indicating the closest creek ID, or a message indicating no creek was found.
     */
    public String deliverReport(){
        return creekId == null ? "no creek found" : String.format("closest creek id: %s", creekId);
    }
}
