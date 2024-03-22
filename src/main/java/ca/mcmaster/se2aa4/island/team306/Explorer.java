package ca.mcmaster.se2aa4.island.team306;

import java.io.StringReader;
import java.util.Locale;

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
    private Decision prevDecision;

    private ReportGenerator generator;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info(String.format("** Initialization info:\n %s", info.toString(2)));
        String direction = info.getString("heading");
        int batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        generator = new ReportGenerator();

        Direction prevDirection = Direction.fromChar(direction.toUpperCase(Locale.ENGLISH).charAt(0));
        drone = new Drone(batteryLevel, prevDirection);
        map = new Map(drone, generator);
        decider = new Decider(drone, map);
        

    }

    @Override
    public String takeDecision() {
        JSONObject decisionJson = new JSONObject();
        Decision decision = decider.getNewDecision();
        this.prevDecision = decision;
        Direction prevDirection = decision.getDirection();
        DecisionType type = decision.getType();
        switch(type){
            case DecisionType.ABORT:
                decisionJson.put("action", "stop"); // we stop the exploration immediately
                map.setReportCreek();
                break;
            case DecisionType.FLY_FORWARD:
                decisionJson.put("action", "fly"); // we fly forward
                break;
            case DecisionType.TURN:
                decisionJson = new JSONObject(String.format(
                    "{ \"action\": \"heading\", \"parameters\": { \"direction\": \"%c\" } }", 
                    prevDirection.toChar()
                )); // we set the heading for direction d
                break;
            case DecisionType.RADAR:
                decisionJson = new JSONObject(String.format(
                    "{ \"action\": \"echo\", \"parameters\": { \"direction\": \"%c\" } }",
                    prevDirection.toChar()
                )); // we use radar scan for direction d
                break;
            case DecisionType.PHOTO:
                decisionJson.put("action", "scan"); // we use photo scan
                break;
            default:
                throw new NullPointerException();
        }
            
        logger.info(String.format("** Decision: %s", decisionJson));
        return decisionJson.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        ParsedResult r = ParsedResult.builder(prevDecision).populate(s).build();
        drone.updateResult(r);
        map.updateStatus(r);
    }

    @Override
    public String deliverFinalReport() {
        String dump =  generator.deliverReport();
        logger.info(dump);
        return dump;
    }



}