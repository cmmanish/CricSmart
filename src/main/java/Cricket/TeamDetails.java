package Cricket;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TeamDetails extends AbsractServicesBaseClass {

    private static Logger log = Logger.getLogger(TeamDetails.class);
    private static TeamDetails instance;
    private String resultJSONString = "";

    private TeamDetails() {
    }

    public static synchronized TeamDetails getInstance() {
        if (instance == null) {
            instance = new TeamDetails();
        }
        return instance;
    }

    public String getCricketTeams() {

        String baseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
        String query = "select * from cricket.teams";
        String format = "&format=json&diagnostics=true&env=store://0TxIGQMQbObzvU4Apia0V0";
        String fullUrlStr = null;
        try {
            fullUrlStr = baseUrl + URLEncoder.encode(query, "UTF-8") + format;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info(fullUrlStr);
        try {
            resultJSONString = getJSONFromURL(AbsractServicesBaseClass.httpclient, fullUrlStr);
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
//            String teamCount = level1Object.get("count").toString();

            JSONObject results = (JSONObject) level1Object.get("results");

            JSONArray TeamArray = (JSONArray) results.get("Team");

            for (int i = 0; i < TeamArray.size(); i++) {

                log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                JSONObject team = (JSONObject) TeamArray.get(i);
                log.info(team.get("TeamName") + " " + team.get("ShortName"));

                JSONArray captain = (JSONArray) team.get("Captain");
                //    JSONArray ranking = (JSONArray) team.get("Ranking");
                JSONObject coach = (JSONObject) team.get("Coach");

                JSONObject cTest = (JSONObject) captain.get(0);
                log.info("Test Captain: " + cTest.get("FirstName") + " " + cTest.get("LastName"));

                JSONObject cOdi = (JSONObject) captain.get(1);
                log.info("Odi Captain: " + cOdi.get("FirstName") + " " + cOdi.get("LastName"));

                if (captain.size() > 2) {
                    JSONObject cT20 = (JSONObject) captain.get(2);
                    log.info("T20 Captain: " + cT20.get("FirstName") + " " + cT20.get("LastName"));
                }

                log.info("Coach: " + coach.get("FirstName") + " " + coach.get("LastName"));
//                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String resultJSONString = TeamDetails.getInstance().getCricketTeams();
        TeamDetails.getInstance().parseJSON(resultJSONString);

    }
}
