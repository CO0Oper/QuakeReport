package com.example.android.quakereport;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /** Sample JSON response for a USGS query */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2018-12-01&endtime=2018-12-25&minfelt=50&minmagnitude=1";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<Report> fetchEarthquakeData(String requestUrl){

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
           // Thread.sleep(2000);
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }//force to sleep 2 second method.

        List<Report> reports = extractFeatureFromJson(jsonResponse);

        return reports;

    }

    @SuppressLint("LongLogTag")
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if ( inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
//
//    /**
//     * Return a list of {@link Report} objects that has been built up from
//     * parsing a JSON response.
//     */
//    public static List<Report> extractFeaturnFromJson(String earthquakeJSON) {
//        if (TextUtils.isEmpty(earthquakeJSON)){
//            return null;
//        }
//
//        // Create an empty ArrayList that we can start adding earthquakes to
//        List<Report> earthquakes = new ArrayList<>();
//
//        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
//        // is formatted, a JSONException exception object will be thrown.
//        // Catch the exception so the app doesn't crash, and print the error message to the logs.
//        try {
//
//            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
//            // build up a list of Earthquake objects with the corresponding data.
//
//            //Create a JSONObject from the SAMPLE_JSON_RESPONSE string
//            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
//
//            //Extract the JSONArray associated with the key called "features",
//            //which represents a list of features (or earthquakes);
//            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
//
//            for(int i = 0 ; i < earthquakeArray.length(); i++){
//
//                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
//
//                JSONObject properties = currentEarthquake.getJSONObject("properties");
//
//                //Extract the value for the key called "time"
//                long time = properties.getLong("time");
//
//                double magnitude = properties.getDouble("mag");
//                String location = properties.getString("place");
//                String timeInMilliseconds = properties.getString("time");
//
//
//                // Extract the value for the key called "url"
//                String url = properties.getString("url");
//                // Create a new {@link Earthquake} object with the magnitude, location, time,
//                // and url from the JSON response.
//                Report earthquake = new Report(magnitude, location, time, url);
//
//
//                earthquakes.add(earthquake);
//
//            }
//
//        } catch (JSONException e) {
//            // If an error is thrown when executing any of the above statements in the "try" block,
//            // catch the exception here, so the app doesn't crash. Print a log message
//            // with the message from the exception.
//            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
//        }
//        // Return the list of earthquakes
//        return earthquakes;
//    }

    /**
     * Return an {@link} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private static List<Report> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        List<Report> reports = new ArrayList<>();



        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            // If there are results in the features array
            for (int i = 0 ; i < earthquakeArray.length() ; i++) {
                // Extract out the first feature (which is an earthquake)
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                double magnitude = properties.getDouble("mag");

                String location = properties.getString("place");

                long time = properties.getLong("time");

                String url = properties.getString("url");

                Report report = new Report(magnitude, location, time, url);

                reports.add(report);

                // Create a new {@link Event} object

            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return reports;
    }

}