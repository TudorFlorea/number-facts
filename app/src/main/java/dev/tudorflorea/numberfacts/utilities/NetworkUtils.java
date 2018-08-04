package dev.tudorflorea.numberfacts.utilities;


import dev.tudorflorea.numberfacts.utilities.UrlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Tudor on 12/2/2017.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "http://numbersapi.com";

    private static final String DELIMITER = "/";

    private static final String TRIVIA_TYPE = "trivia";

    private static final String DATE_TYPE = "math";

    private static final String MATH_TYPE = "date";

    private static final String YEAR_TYPE = "year";

    private static final String RANDOM_NUMBER = "random";

    private static final String JSON_QUERY_PARAMETER = "?json";

    public static final int TEST_NUMBER = 42;


    public static String getResponseFromHttpUrl(URL url) {

        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
        builder.url(url);

        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

    }

    public static String getTriviaRawJSON(int number) {
        return getResponseFromHttpUrl(UrlUtils.buildTriviaURL(number));
    }

    public static String getRandomTriviaRawJSON() {
        return getResponseFromHttpUrl(UrlUtils.buildRandomTriviaURL());
    }

    public static String getMathRawJSON(int number) {
        return getResponseFromHttpUrl(UrlUtils.buildMathURL(number));
    }

    public static String getRandomMathRawJSON() {
        return getResponseFromHttpUrl(UrlUtils.buildRandomMathURL());
    }

    public static String getDateRawJSON(int month, int day) {
        return getResponseFromHttpUrl(UrlUtils.buildDateURL(month, day));
    }

    public static String getRandomDateRawJSON() {
        return getResponseFromHttpUrl(UrlUtils.buildRandomDateURL());
    }

    public static String getYearRawJSON(int year) {
        return getResponseFromHttpUrl(UrlUtils.buildYearURL(year));
    }

    public static String getRandomYearRawJSON() {
        return getResponseFromHttpUrl(UrlUtils.buildRandomYearURL());
    }

}
