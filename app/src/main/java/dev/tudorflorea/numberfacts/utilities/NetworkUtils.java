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

public final class NetworkUtils {


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

    public static String getTriviaRawJSON(String number) {
        return getResponseFromHttpUrl(UrlUtils.buildTriviaURL(number));
    }

    public static String getRandomTriviaRawJSON() {
        return getResponseFromHttpUrl(UrlUtils.buildRandomTriviaURL());
    }

    public static String getMathRawJSON(String number) {
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

    public static String getYearRawJSON(String year) {
        return getResponseFromHttpUrl(UrlUtils.buildYearURL(year));
    }

    public static String getRandomYearRawJSON() {
        return getResponseFromHttpUrl(UrlUtils.buildRandomYearURL());
    }

}
