package com.example.android.quakereport;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Report>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    private String mUrl;

    /**
     * Constructs a new {@link EarthquakeLoader}.
     * @param context
     * @param url
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i (LOG_TAG, "Test: Calling onStartLoading()");
        forceLoad();
    }

    /**
     * This is on a background thread.
     * @return
     */
    @Override
    public List<Report> loadInBackground() {
        Log.i (LOG_TAG, "Test: loadInBackground()");
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Report> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }

}
