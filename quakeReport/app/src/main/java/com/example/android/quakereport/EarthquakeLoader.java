package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>
{
    private static final String LOG_TAG = EarthquakeLoader.class.getSimpleName();
    private String mUrl;

    /**
     * Constructs a new {@link EarthquakeLoader}.
     * @param context is the activity context
     * @param url is the url where it loads the data
     */
    public EarthquakeLoader(Context context, String url)
    {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl == null)
            return null;

        //Makes a network request, then decodefy the answer, and extracts an Earthquake list
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
