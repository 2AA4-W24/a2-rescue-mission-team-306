package ca.mcmaster.se2aa4.island.team306;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    public static final Decider decider = new Decider();
        

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {
        JSONObject decision_json = new JSONObject();
        Decision decision = decider.getDecision();
        char d = decider.getJsonDirection().toChar();
        switch(decision){
            case ABORT:
                decision_json.put("action", "stop"); // we stop the exploration immediately
                break;
            case FLY_FORWARD:
                decision_json.put("action", "fly"); // we fly forward
                break;
            case TURN:
                decision_json = new JSONObject(String.format(
                    "{ \"action\": \"heading\", \"parameters\": { \"direction\": \"%c\" } }", 
                    d
                ));
                break;
            case PHOTO:
                char d = decider.getJsonDirection().toChar();

                
            default:
                throw new NullPointerException();
        }
            
        logger.info("** Decision: {}",decision_json.toString());
        return decision_json.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);

    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
