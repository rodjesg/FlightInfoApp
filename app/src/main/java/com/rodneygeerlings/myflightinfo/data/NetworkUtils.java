package com.rodneygeerlings.myflightinfo.data;




import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    private final static String FLIGHT_API_BASE_URL = "https://api.schiphol.nl/public-flights/flights";
    private final static String PARAM_APP_KEY = "app_key";
    private final static String VALUE_APP_KEY = "98c8d5a5493b3c5578c65eb9c4b4a46f";
    private final static String PARAM_APP_ID = "app_id";
    private final static String VALUE_APP_ID = "cc158223";
    private final static String PARAM_FROM_DATE = "fromdate";
    private final static String PARAM_TO_DATE = "todate";

    /**
     * Builds the URL used to get flight overviews.
     *
     * @return The URL to use to query the Schiphol API.
     */
    public static URL buildFlightOverviewUrl() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDate = LocalDate.now().format(formatter);
        String toDate = LocalDate.now().plusDays(1).format(formatter);

        Uri builtUri = Uri.parse(FLIGHT_API_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_APP_KEY, VALUE_APP_KEY)
                .appendQueryParameter(PARAM_APP_ID, VALUE_APP_ID)
                .appendQueryParameter(PARAM_FROM_DATE, fromDate)
                .appendQueryParameter(PARAM_TO_DATE, toDate)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to get flight details.
     *
     * @return The URL to use to query the Schiphol API.
     */
    public static URL buildFlightDetailUrl(int flightId) {
        Uri builtUri = Uri.parse(FLIGHT_API_BASE_URL + '/' + flightId).buildUpon()
                .appendQueryParameter(PARAM_APP_KEY, VALUE_APP_KEY)
                .appendQueryParameter(PARAM_APP_ID, VALUE_APP_ID)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        // set target API version for Schiphol API
        urlConnection.setRequestProperty("ResourceVersion", "v3");
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
