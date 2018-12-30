/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Report>> {

    private TextView mEmptyStateTextView;

    private static final String LOG_TAG = EarthquakeActivity.class.getName();

    private ListAdapter mAdapter;

//    private static final String USGS_REQUEST_URL =
//            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2018-12-01&endtime=2018-12-25&minfelt=50&minmagnitude=4";
       // private static final String USGS_REQUEST_URL =
       // "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=16";

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
        private static final int EARTHQUAKE_LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i (LOG_TAG, "Test: Earthquake Activity onCreate() called");
        //call when routed the phone.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
//        ArrayList<Report> earthquakes = QueryUtils.extractEarthquakes();
//
//        // Create a new adapter that takes the list of earthquakes as input
//        final ListAdapter adapter = new ListAdapter(this, earthquakes);

        // Create a fake list of earthquake locations.
//        ArrayList<Report> report = new ArrayList<>();
////        report.add(new Report("7.2", "San Francisco", "Feb 2, 2016"));


        // Find a reference to the {@link ListView} in the layout


        // Create a new {@link ArrayAdapter} of earthquakes
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, earthquakes);

        //ListAdapter quackreport = new ListAdapter(this, earthquakes);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mEmptyStateTextView  = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new ListAdapter(this, new ArrayList<Report>());

        // earthquakeListView.setAdapter(quackreport);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Report currentEarthquake = mAdapter.getItem(position);


                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);


        // Get a reference to the LoaderManager, in order to interact with loaders.
//        LoaderManager loaderManager = getLoaderManager();
//
//        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
//        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
//        // because this activity implements the LoaderCallbacks interface).
//        Log.i(LOG_TAG, "Test: calling initLoaer()");
//        //called when routed the phone
//        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
         ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get a reference to the ConnectivityManager to check state of network connectivity

        // Get details on the currently active default data network
          NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();

          if(networkInfo != null && networkInfo.isConnected()){
              LoaderManager loaderManager = getLoaderManager();

              loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
          } else {
              View loadingIndicator = findViewById(R.id.loading_indicator);
              loadingIndicator.setVisibility(View.GONE);

              mEmptyStateTextView.setText(R.string.no_internet_connection);
          }


    }

//    private void updateUi(Event earthquake){
//
//    }


//    @Override
//    public Loader<List<Report>> onCreateLoader(int i, Bundle bundle) {
//        // Create a new loader for the given URL
//        Log.i(LOG_TAG, "Test: Calling onCreateLoader()");
//        return new EarthquakeLoader(this, USGS_REQUEST_URL);
//    }

    @Override
    public Loader<List<Report>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        Log.i(LOG_TAG, "Test: Calling onCreateLoader()");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
                Uri baseUri = Uri.parse(USGS_REQUEST_URL);
                Uri.Builder uriBuilder = baseUri.buildUpon();

                uriBuilder.appendQueryParameter("format", "geojson");
                uriBuilder.appendQueryParameter("limit","20");
                uriBuilder.appendQueryParameter("minmag", minMagnitude);
                uriBuilder.appendQueryParameter("orderby", "time");

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Report>> loader, List<Report> reports) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (reports != null && !reports.isEmpty()) {
            mAdapter.addAll(reports);
        }
        Log.i(LOG_TAG, "Test: Calling onLoadFinished()");
        //Called when return to app
        //Called when routed the phone.
    }

    @Override
    public void onLoaderReset(Loader<List<Report>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        Log.i(LOG_TAG, "Test: Calling onLoaderReset()");
        //Called when hit back button.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Report>>{
//
//
//
//        protected List<Report> doInBackground(String... urls) {
//            // Don't perform the request if there are no URLs, or the first URL is null.
//            if (urls.length < 1 || urls[0] == null){
//                return null;
//            }
//
//            List<Report> result = QueryUtils.fetchEarthquakeData(urls[0]);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<Report> result){
//
//            mAdapter.clear();
//
//            //If there is no result, do nothing.
//            if(result != null && !result.isEmpty()){
//                mAdapter.addAll(result);
//            }
//
//            //updateUi(result);
//        }
//    }

}
