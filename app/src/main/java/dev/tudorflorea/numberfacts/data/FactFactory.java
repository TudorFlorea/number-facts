package dev.tudorflorea.numberfacts.data;

import android.database.Cursor;

import org.json.JSONObject;

import java.util.ArrayList;

import dev.tudorflorea.numberfacts.database.FactContract;
import dev.tudorflorea.numberfacts.utilities.JsonUtils;
import dev.tudorflorea.numberfacts.utilities.NetworkUtils;


/**
 * Created by Tudor on 12/2/2017.
 */

public class FactFactory extends Fact{

    public static Fact fromCursor(Cursor cursor) {

        Fact fact;

        long databaseId = cursor.getLong(cursor.getColumnIndex(FactContract.FactEntry._ID));
        String text = cursor.getString(cursor.getColumnIndex(FactContract.FactEntry.COLUMN_FACT));
        String number = cursor.getString(cursor.getColumnIndex(FactContract.FactEntry.COLUMN_NUMBER));
        boolean found = cursor.getInt(cursor.getColumnIndex(FactContract.FactEntry.COLUMN_FOUND)) == 1;
        String type = cursor.getString(cursor.getColumnIndex(FactContract.FactEntry.COLUMN_TYPE));
        String timestamp = cursor.getString(cursor.getColumnIndex(FactContract.FactEntry.COLUMN_TIMESTAMP));

        fact = new Fact(number, text, found, type, timestamp, databaseId);

        return fact;

    }

    public static ArrayList<Fact> listFromCursor(Cursor cursor) {

        ArrayList<Fact> facts = new ArrayList<>();
        while (cursor.moveToNext()) {
            facts.add(fromCursor(cursor));
        }

        return facts;
    }

    public static Fact TriviaFact(String number) {
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

    public static Fact MathFact(String number) {
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

    public static Fact YearFact(String year) {
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

