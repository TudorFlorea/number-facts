package dev.tudorflorea.numberfacts.data;

import org.json.JSONObject;

import dev.tudorflorea.numberfacts.utilities.JsonUtils;
import dev.tudorflorea.numberfacts.utilities.NetworkUtils;


/**
 * Created by Tudor on 12/2/2017.
 */

public class FactFactory extends Fact{

    public static Fact TriviaFact(int number) {
        try{
            JSONObject triviaFactJson = JsonUtils.jsonObjectFromString(NetworkUtils.getTriviaRawJSON(number));
            return setFact(triviaFactJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Fact RandomTriviaFact() {
        try{
            JSONObject randomTriviaFact = JsonUtils.jsonObjectFromString(NetworkUtils.getRandomTriviaRawJSON());
            return setFact(randomTriviaFact);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Fact MathFact(int number) {
        try{
            JSONObject mathFactJson = JsonUtils.jsonObjectFromString(NetworkUtils.getMathRawJSON(number));
            return setFact(mathFactJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Fact RandomMathFact() {
        try{
            JSONObject randomMathFactJson = JsonUtils.jsonObjectFromString(NetworkUtils.getRandomMathRawJSON());
            return setFact(randomMathFactJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Fact DateFact(int month, int day) {
        try{
            JSONObject dateFactJson = JsonUtils.jsonObjectFromString(NetworkUtils.getDateRawJSON(month, day));
            return setFact(dateFactJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Fact RandomDateFact() {
        try{
            JSONObject randomDateFactJson = JsonUtils.jsonObjectFromString(NetworkUtils.getRandomDateRawJSON());
            return setFact(randomDateFactJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Fact YearFact(int year) {
        try{
            JSONObject yearFactJson = JsonUtils.jsonObjectFromString(NetworkUtils.getYearRawJSON(year));
            return setFact(yearFactJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Fact RandomYearFact() {
        try {
            JSONObject randomYearFactJson = JsonUtils.jsonObjectFromString(NetworkUtils.getRandomYearRawJSON());
            return setFact(randomYearFactJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static Fact setFact(JSONObject factParams) {
        Fact fact = new Fact();
        fact.setFactFromJsonObject(factParams);
        return fact;
    }


}

