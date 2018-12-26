package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class QueryUtils {

    /** Sample JSON response for a USGS query */
    private static final String SAMPLE_JSON_RESPONSE = "{\"type\":\"FeatureCollection\",\"metadata\":{\"generated\":1542148385000,\"url\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2018-10-13&endtime=2018-11-13&minmag=2&limit=10\",\"title\":\"USGS Earthquakes\",\"status\":200,\"api\":\"1.5.8\",\"limit\":10,\"offset\":1,\"count\":10},\"features\":[{\"type\":\"Feature\",\"properties\":{\"mag\":2.04,\"place\":\"4km SE of Maricao, Puerto Rico\",\"time\":1542067156590,\"updated\":1542081780653,\"tz\":-240,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/pr2018316006\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=pr2018316006&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":null,\"alert\":null,\"status\":\"reviewed\",\"tsunami\":0,\"sig\":64,\"net\":\"pr\",\"code\":\"2018316006\",\"ids\":\",pr2018316006,\",\"sources\":\",pr,\",\"types\":\",geoserve,origin,phase-data,\",\"nst\":11,\"dmin\":0.1357,\"rms\":0.15,\"gap\":69,\"magType\":\"md\",\"type\":\"earthquake\",\"title\":\"M 2.0 - 4km SE of Maricao, Puerto Rico\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-66.952,18.1533,22]},\"id\":\"pr2018316006\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":3.3,\"place\":\"9km E of Soda Springs, Idaho\",\"time\":1542066743320,\"updated\":1542147589730,\"tz\":-420,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us1000hr34\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us1000hr34&format=geojson\",\"felt\":2,\"cdi\":3.4,\"mmi\":null,\"alert\":null,\"status\":\"reviewed\",\"tsunami\":0,\"sig\":168,\"net\":\"us\",\"code\":\"1000hr34\",\"ids\":\",uu60304227,us1000hr34,\",\"sources\":\",uu,us,\",\"types\":\",dyfi,geoserve,origin,phase-data,\",\"nst\":null,\"dmin\":0.196,\"rms\":0.3,\"gap\":140,\"magType\":\"ml\",\"type\":\"earthquake\",\"title\":\"M 3.3 - 9km E of Soda Springs, Idaho\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-111.4862,42.653,5]},\"id\":\"us1000hr34\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":3.17,\"place\":\"72km NE of Santa Barbara de Samana, Dominican Republic\",\"time\":1542066253630,\"updated\":1542083728040,\"tz\":-300,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/pr2018316005\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=pr2018316005&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":null,\"alert\":null,\"status\":\"reviewed\",\"tsunami\":0,\"sig\":155,\"net\":\"pr\",\"code\":\"2018316005\",\"ids\":\",pr2018316005,us1000hr6a,\",\"sources\":\",pr,us,\",\"types\":\",geoserve,origin,phase-data,\",\"nst\":4,\"dmin\":1.1626,\"rms\":0.1,\"gap\":348,\"magType\":\"md\",\"type\":\"earthquake\",\"title\":\"M 3.2 - 72km NE of Santa Barbara de Samana, Dominican Republic\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-68.5596,19.6628,100]},\"id\":\"pr2018316005\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":4.3,\"place\":\"101km NE of San Pedro de Atacama, Chile\",\"time\":1542065454610,\"updated\":1542066431040,\"tz\":-240,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us1000hr2s\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us1000hr2s&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":null,\"alert\":null,\"status\":\"reviewed\",\"tsunami\":0,\"sig\":284,\"net\":\"us\",\"code\":\"1000hr2s\",\"ids\":\",us1000hr2s,\",\"sources\":\",us,\",\"types\":\",geoserve,origin,phase-data,\",\"nst\":null,\"dmin\":0.918,\"rms\":0.8,\"gap\":60,\"magType\":\"mb\",\"type\":\"earthquake\",\"title\":\"M 4.3 - 101km NE of San Pedro de Atacama, Chile\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-67.5534,-22.2352,203.32]},\"id\":\"us1000hr2s\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":2.4,\"place\":\"71km SSW of Kaktovik, Alaska\",\"time\":1542064786430,\"updated\":1542084187040,\"tz\":-540,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/ak20356839\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=ak20356839&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":null,\"alert\":null,\"status\":\"reviewed\",\"tsunami\":0,\"sig\":89,\"net\":\"ak\",\"code\":\"20356839\",\"ids\":\",ak20356839,us1000hr2q,\",\"sources\":\",ak,us,\",\"types\":\",geoserve,origin,phase-data,\",\"nst\":null,\"dmin\":null,\"rms\":1.02,\"gap\":null,\"magType\":\"ml\",\"type\":\"earthquake\",\"title\":\"M 2.4 - 71km SSW of Kaktovik, Alaska\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-144.5682,69.5752,7.5]},\"id\":\"ak20356839\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":2.29,\"place\":\"21km WSW of Afton, Wyoming\",\"time\":1542062335870,\"updated\":1542138655490,\"tz\":-420,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/uu60304212\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=uu60304212&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":null,\"alert\":null,\"status\":\"reviewed\",\"tsunami\":0,\"sig\":81,\"net\":\"uu\",\"code\":\"60304212\",\"ids\":\",uu60304212,\",\"sources\":\",uu,\",\"types\":\",geoserve,origin,phase-data,\",\"nst\":12,\"dmin\":0.1511,\"rms\":0.19,\"gap\":137,\"magType\":\"ml\",\"type\":\"earthquake\",\"title\":\"M 2.3 - 21km WSW of Afton, Wyoming\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-111.1495,42.6186667,-3.41]},\"id\":\"uu60304212\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":5.6,\"place\":\"Central East Pacific Rise\",\"time\":1542062247420,\"updated\":1542144641040,\"tz\":-420,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us1000hr1c\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us1000hr1c&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":0,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":482,\"net\":\"us\",\"code\":\"1000hr1c\",\"ids\":\",us1000hr1c,\",\"sources\":\",us,\",\"types\":\",geoserve,losspager,moment-tensor,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":21.09,\"rms\":1.07,\"gap\":110,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 5.6 - Central East Pacific Rise\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-103.4255,-3.1056,10]},\"id\":\"us1000hr1c\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":2.11,\"place\":\"14km S of Tres Pinos, CA\",\"time\":1542061761430,\"updated\":1542064562227,\"tz\":-480,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/nc73110061\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=nc73110061&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":null,\"alert\":null,\"status\":\"automatic\",\"tsunami\":0,\"sig\":68,\"net\":\"nc\",\"code\":\"73110061\",\"ids\":\",nc73110061,\",\"sources\":\",nc,\",\"types\":\",focal-mechanism,geoserve,nearby-cities,origin,phase-data,scitech-link,\",\"nst\":33,\"dmin\":0.06087,\"rms\":0.06,\"gap\":126,\"magType\":\"md\",\"type\":\"earthquake\",\"title\":\"M 2.1 - 14km S of Tres Pinos, CA\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-121.2938309,36.6694984,3.85]},\"id\":\"nc73110061\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":2.14,\"place\":\"30km S of Solvang, CA\",\"time\":1542061756780,\"updated\":1542070842044,\"tz\":-480,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/ci38360624\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=ci38360624&format=geojson\",\"felt\":2,\"cdi\":2,\"mmi\":null,\"alert\":null,\"status\":\"reviewed\",\"tsunami\":0,\"sig\":71,\"net\":\"ci\",\"code\":\"38360624\",\"ids\":\",ci38360624,\",\"sources\":\",ci,\",\"types\":\",dyfi,geoserve,nearby-cities,origin,phase-data,scitech-link,\",\"nst\":21,\"dmin\":0.1484,\"rms\":0.29,\"gap\":122,\"magType\":\"ml\",\"type\":\"earthquake\",\"title\":\"M 2.1 - 30km S of Solvang, CA\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-120.1853333,34.3298333,5.85]},\"id\":\"ci38360624\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":2.4,\"place\":\"18km S of Chignik Lake, Alaska\",\"time\":1542058487097,\"updated\":1542059154215,\"tz\":-540,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/ak20356697\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=ak20356697&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":null,\"alert\":null,\"status\":\"automatic\",\"tsunami\":0,\"sig\":89,\"net\":\"ak\",\"code\":\"20356697\",\"ids\":\",ak20356697,\",\"sources\":\",ak,\",\"types\":\",geoserve,origin,\",\"nst\":null,\"dmin\":null,\"rms\":0.44,\"gap\":null,\"magType\":\"ml\",\"type\":\"earthquake\",\"title\":\"M 2.4 - 18km S of Chignik Lake, Alaska\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-158.7463,56.0925,73.9]},\"id\":\"ak20356697\"}],\"bbox\":[-158.7463,-22.2352,-3.41,-66.952,69.5752,203.32]} ";



    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() throws JSONException {
    }




    /**
     * Return a list of {@link Report} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Report> extractEarthquakes() {





        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Report> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            //Create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject baseResponse = new JSONObject(SAMPLE_JSON_RESPONSE);

            //Extract the JSONArray associated with the key called "features",
            //which represents a list of features (or earthquakes);
            JSONArray earthquakeArray = baseResponse.getJSONArray("features");

            for(int i = 0 ; i < earthquakeArray.length(); i++){

                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                JSONObject properties = currentEarthquake.getJSONObject("properties");

                //Extract the value for the key called "time"
                long time = properties.getLong("time");

                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                String timeInMilliseconds = properties.getString("time");




                // Extract the value for the key called "url"
                String url = properties.getString("url");
                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                Report earthquake = new Report(magnitude, location, time, url);


                earthquakes.add(earthquake);

            }



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}