package Cricket;

import org.junit.Test;

/**
 * Created by mmadhusoodan on 3/19/15.
 */
public class TestScorecard {

    @Test
    public void TestScoreboardDetails0() {

        String resultJSONString = Scorecard.getInstance().getScoreboardDetailsForMatch("186881");
        Scorecard.getInstance().parseJSON(resultJSONString);

    }

    @Test
    public void TestScoreboardDetails1() {

        String resultJSONString = Scorecard.getInstance().getScoreboardDetailsForMatch("186880");
        Scorecard.getInstance().parseJSON(resultJSONString);

    }

    @Test
    public void TestScoreboardDetails2() {

        String resultJSONString = Scorecard.getInstance().getScoreboardDetailsForMatch("186879");
        Scorecard.getInstance().parseJSON(resultJSONString);

    }

    @Test
    public void TestScoreboardDetails3() {

        String resultJSONString = Scorecard.getInstance().getScoreboardDetailsForMatch("186878");
        Scorecard.getInstance().parseJSON(resultJSONString);

    }
}