package dev.tudorflorea.numberfacts.utilities;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tudor on 2/14/2018.
 */

class UrlUtils {

    private static final String BASE_URL = "http://numbersapi.com";

    private static final String DELIMITER = "/";

    private static final String TRIVIA_TYPE = "trivia";

    private static final String DATE_TYPE = "date";

    private static final String MATH_TYPE = "math";

    private static final String YEAR_TYPE = "year";

    private static final String RANDOM_NUMBER = "random";

    private static final String JSON_QUERY_PARAMETER = "?json";

    private static URL buildUrlFromString (String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildTriviaURL(int number) {
        String triviaUrlString = BASE_URL + DELIMITER
                + String.valueOf(number) + DELIMITER
                + TRIVIA_TYPE
                + JSON_QUERY_PARAMETER;

        return buildUrlFromString(triviaUrlString);
    }

    public static URL buildRandomTriviaURL() {
        String randomTriviaUrlString = BASE_URL + DELIMITER
                + RANDOM_NUMBER + DELIMITER
                + TRIVIA_TYPE
                + JSON_QUERY_PARAMETER;
        return buildUrlFromString(randomTriviaUrlString);
    }

    public static URL buildDateURL(int month, int day) {
        String dateUrlString = BASE_URL + DELIMITER
                + String.valueOf(month) + DELIMITER
                + String.valueOf(day) + DELIMITER
                + DATE_TYPE
                + JSON_QUERY_PARAMETER;

        return buildUrlFromString(dateUrlString);
    }

    public static URL buildRandomDateURL() {
        String randomDateUrlString = BASE_URL + DELIMITER
                + RANDOM_NUMBER + DELIMITER
                + DATE_TYPE
                + JSON_QUERY_PARAMETER;

        return buildUrlFromString(randomDateUrlString);
    }

    public static URL buildYearURL(int year) {
        String yearUrlString = BASE_URL + DELIMITER
                + String.valueOf(year) + DELIMITER
                + YEAR_TYPE
                + JSON_QUERY_PARAMETER;

        return buildUrlFromString(yearUrlString);
    }

    public static URL buildRandomYearURL() {
        String randomYearUrlString = BASE_URL + DELIMITER
                + RANDOM_NUMBER + DELIMITER
                + YEAR_TYPE
                + JSON_QUERY_PARAMETER;

        return buildUrlFromString(randomYearUrlString);
    }

    public static URL buildMathURL(int number) {
        String mathUrlString = BASE_URL + DELIMITER
                + String.valueOf(number) + DELIMITER
                + MATH_TYPE
                + JSON_QUERY_PARAMETER;

        return buildUrlFromString(mathUrlString);
    }

    public static URL buildRandomMathURL() {
        String randomMathUrlString = BASE_URL + DELIMITER
                + RANDOM_NUMBER + DELIMITER
                + MATH_TYPE
                + JSON_QUERY_PARAMETER;

        return buildUrlFromString(randomMathUrlString);
    }

}
