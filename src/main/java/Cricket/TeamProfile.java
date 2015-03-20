package Cricket;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by mmadhusoodan on 3/18/15.
 */
public class TeamProfile extends AbsractServicesBaseClass {

    private static Logger log = Logger.getLogger(TeamProfile.class);
    private static TeamProfile instance;
    protected String resultJSONString = "";

    private TeamProfile() {
    }

    public static synchronized TeamProfile getInstance() {
        if (instance == null) {
            instance = new TeamProfile();
        }
        return instance;
    }

    public String getCricketTeamProfile(String profileId) {

        String baseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
        String query = "select * from cricket.team.profile where player_id=" + profileId;
        String format = "&format=json&diagnostics=true&env=store://0TxIGQMQbObzvU4Apia0V0";
        String fullUrlStr = null;
        try {
            fullUrlStr = baseUrl + URLEncoder.encode(query, "UTF-8") + format;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info(fullUrlStr);
        try {
            resultJSONString = getJSONFromURL(httpclient, fullUrlStr);
            log.info(resultJSONString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJSONString;
    }

    public void parseJSON(String resultJSON) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(resultJSON);
            JSONObject level0Object = (JSONObject) obj;

            JSONObject level1Object = (JSONObject) level0Object.get("query");
            JSONObject results = (JSONObject) level1Object.get("results");

            JSONObject player = (JSONObject) results.get("Player");

            log.info(player.get("personid"));
            JSONObject playerDetails = (JSONObject) player.get("PersonalDetails");

            log.info(playerDetails.get("FirstName") + " " + playerDetails.get("LastName"));
            log.info(playerDetails.get("DateOfBirth"));

            log.info(player.get("Role"));

            JSONObject style = (JSONObject) player.get("Style");
            log.info(style.get("Batting"));
            log.info(style.get("Bowling"));

            log.info(player.get("Team"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String teamId = "4";
        String resultJSONString = TeamProfile.getInstance().getCricketTeamProfile(teamId);
        Player.getInstance().parseJSON(resultJSONString);

    }
}
