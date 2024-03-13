package ca.mcmaster.se2aa4.island.team306;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private Decider decider;
    private Drone drone;
    private Map map;
    private Direction prevDirection;
    private Decision prevDecision;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        prevDirection = Direction.fromChar(direction.toUpperCase().charAt(0));
        drone = new Drone(batteryLevel, prevDirection);
        map = new Map(drone);
        decider = new Decider(drone, map);
        

    }

    @Override
    public String takeDecision() {
        JSONObject decision_json = new JSONObject();
        Decision decision = decider.getNewDecision();
        this.prevDecision = decision;
        this.prevDirection = decision.getDirection();
        DecisionType type = decision.getType();
        switch(type){
            case DecisionType.ABORT:
                decision_json.put("action", "stop"); // we stop the exploration immediately
                break;
            case DecisionType.FLY_FORWARD:
                decision_json.put("action", "fly"); // we fly forward
                break;
            case DecisionType.TURN:
                decision_json = new JSONObject(String.format(
                    "{ \"action\": \"heading\", \"parameters\": { \"direction\": \"%c\" } }", 
                    prevDirection.toChar()
                )); // we set the heading for direction d
                break;
            case DecisionType.RADAR:
                decision_json = new JSONObject(String.format(
                    "{ \"action\": \"echo\", \"parameters\": { \"direction\": \"%c\" } }",
                    prevDirection.toChar()
                )); // we use radar scan for direction d
                break;
            case DecisionType.PHOTO:
                decision_json.put("action", "scan"); // we use photo scan
                break;
            default:
                throw new NullPointerException();
        }
            
        logger.info("** Decision: {}",decision_json.toString());
        return decision_json.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        // Starter code
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);

        // Important code
        ParsedResult r = ParsedResult.builder(prevDecision).populate(s).build();
        drone.updateResult(r);
        map.updateStatus(r);
    }

    @Override
    public String deliverFinalReport() {
        ReportGenerator report = new ReportGenerator(map, drone);
        return report.deliverFinalReport();

    }



}