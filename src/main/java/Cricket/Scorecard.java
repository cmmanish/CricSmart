package Cricket;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by mmadhusoodan on 3/18/15.
 */
public class Scorecard extends AbsractServicesBaseClass {

    private static Logger log = Logger.getLogger(Scorecard.class);
    private static Scorecard instance;
    private String resultJSONString = "";

    private Scorecard() {
    }

    public static synchronized Scorecard getInstance() {
        if (instance == null) {
            instance = new Scorecard();
        }
        return instance;
    }

    public String getScoreboardDetailsForMatch(String matchId) {

        String baseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
        String query = "select * from cricket.scorecard where match_id=" + matchId;
        String format = "&format=json&env=store://0TxIGQMQbObzvU4Apia0V0";
        String fullUrlStr = "";
        try {
            fullUrlStr = baseUrl + URLEncoder.encode(query, "UTF-8") + format;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            resultJSONString = getJSONFromURL(httpclient, fullUrlStr);
            log.info(resultJSONString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJSONString;
    }

    public void parseJSON(String resultJSON) {
        //Scorecard has series, place teams[] toss past_ings
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(resultJSON);
            JSONObject level0Object = (JSONObject) obj;

            JSONObject query = (JSONObject) level0Object.get("query");
            JSONObject results = (JSONObject) query.get("results");
            JSONObject Scorecard = (JSONObject) results.get("Scorecard");

            //series
            JSONObject series = (JSONObject) Scorecard.get("series");
            log.info("series: " + series.get("series_name"));

            //place
            JSONObject place = (JSONObject) Scorecard.get("place");
            log.info("stadium: " + place.get("stadium"));

            String mn = Scorecard.get("mn").toString();
            log.info("match " + mn);

            String ms = Scorecard.get("ms").toString();

            if (ms.equalsIgnoreCase("Match Ended")) {
                log.info("match status: " + ms);

                JSONArray TeamsArray = (JSONArray) Scorecard.get("teams");

                for (int i = 0; i < TeamsArray.size(); i++) {
                    JSONObject eachTeam = (JSONObject) TeamsArray.get(i);
                    log.info("Team " + (i + 1) + " " + eachTeam.get("fn"));
                    JSONArray squadArray = (JSONArray) eachTeam.get("squad");

                    for (int j = 0; j < squadArray.size(); j++) {
                        JSONObject eachSquad = (JSONObject) squadArray.get(j);
//                    log.info("Name: " + eachSquad.get("full") + " Age: " + eachSquad.get("age"));
                    }
                }

                JSONObject result = (JSONObject) Scorecard.get("result");
                if (result != null) {
                    log.info("mom: " + ((JSONObject) result.get("mom")).get("fn"));
                }

                JSONArray pastInningsArray = (JSONArray) Scorecard.get("past_ings");

                //pastInningsArray is stores 2nd innings followed by 1st innings
                for (int z = pastInningsArray.size() - 1; z >= 0; z--) {
                    log.info("Innings ");
                    JSONObject Innings = (JSONObject) pastInningsArray.get(z);

                    JSONObject sObject = (JSONObject) Innings.get("s");
                    JSONObject dObject = (JSONObject) Innings.get("d");
                    JSONObject fwObject = (JSONObject) Innings.get("fw");

                    JSONArray fwArray = (JSONArray) ((JSONObject) dObject.get("a")).get("t");

                    //Umpires and Details

                    // Fall of wicket
                    for (int w = 0; w < fwArray.size(); w++) {
                        JSONObject wicket = (JSONObject) fwArray.get(w);
                        if (wicket.get("c") != null)
                            log.info((w + 1) + " " + wicket.get("c") + " " + wicket.get("r") + "(" + wicket.get("b") + ")" + " sr: " + wicket.get("sr"));
                    }
                    // Fall of wicket Details
                    if (fwObject.get("d") instanceof JSONArray) {
                        JSONArray fallOfWicketsDetailArray = (JSONArray) fwObject.get("d");
                        for (int w = 0; w < fallOfWicketsDetailArray.size(); w++) {
                            JSONObject fwDetailObject = (JSONObject) fallOfWicketsDetailArray.get(w);
                            String wicketDetail = fwDetailObject.get("c").toString();
                            log.info((w + 1) + " " + wicketDetail);
                        }
                    } else {//only 1 wicket so the response is not an array but JSONObject
                        JSONObject fwDetailObject = (JSONObject) fwObject.get("d");
                        String wicketDetail = fwDetailObject.get("c").toString();
                        log.info(wicketDetail);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}