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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeActivity.class.getName();
    //private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private EarthquakeAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
        //final ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        /**
         * Create an EarthquakeAdapter
         */
        //EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        mLoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        /**
         * Check the internet status
         */
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            //Get a referece to LoaderManager, to interact to other loaders
            LoaderManager loaderManager = getLoaderManager();

            /**
             * Start the loader. Give an ID and a null bandle. Pass this activity to the LoaderCallback parameter (which is valid because this activity implements a LoaderCallback interface).
             */
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else
        {
            mLoadingSpinner.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        /**
         * This handle the click listener for each item on the list view
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                // This takes the current item on the list
                Earthquake earthquake = mAdapter.getItem(position);

                /**
                 * This launches the item clicked to more info on the website
                 */
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(earthquake.getUrl()));
                startActivity(browserIntent);
            }
        });

        //Start the AsyncTask to load the earthquake data
//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args)
    {
        //Create a new loader to the URL received
        //return new EarthquakeLoader(this, USGS_REQUEST_URL);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        Uri uri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data)
    {
        //Set the progress spinner to gone
        mLoadingSpinner.setVisibility(View.GONE);

        //Set the empty text to show a "No earthquake found" message
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        //Clear the old data from the earthquake list
        mAdapter.clear();

        /**
         * If there is a valid list of {@link Earthquake}, then add them to the mAdapter.
         * This will call the ListView to be up-to-date
         */
        if(data != null && !data.isEmpty())
            mAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader)
    {
        mAdapter.clear();
    }

//    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>
//    {
//        @Override
//        protected void onPostExecute(List<Earthquake> data)
//        {
//            //Clear the old data from the earthquake list
//            mAdapter.clear();
//
//            /**
//             * If there is a valid list of {@link Earthquake}, then add them to the mAdapter.
//             * This will call the ListView to be up-to-date
//             */
//            if(data != null && !data.isEmpty())
//                mAdapter.addAll(data);
//        }
//
//        @Override
//        protected List<Earthquake> doInBackground(String... strings)
//        {
//            if(strings.length < 1 || strings[0] == null)
//                return null;
//
//            List<Earthquake> result = QueryUtils.fetchEarthquakeData(strings[0]);
//            return result;
//        }
//    }

    /**
     * Create the menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This handle the items selected at the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}