package com.example.android.quakereport;

import org.json.JSONObject;

/**
 * {@link Earthquake} represents the data for each earthquake.
 */

public class Earthquake extends JSONObject
{
    private double mMagnitude;
    private String mPlace, mUrl;
    private long mTimeInMilliseconds;

    /**
     * Contstructs a new {@link Earthquake} object.
     * @param magnitude magnitude is the magnitude (size) of the earthquake
     * @param place location is the location where the earthquake happened
     * @param timeInMilliseconds timeInMilliseconds is the time in milliseconds (from the Epoch) when the earthquake happened
     * @param url is the website URL to find more details about the earthquake
     */
    public Earthquake(double magnitude, String place, long timeInMilliseconds, String url)
    {
        mMagnitude = magnitude;
        mPlace = place;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    public double getMagnitude()
    {
        return mMagnitude;
    }

    public String getPlace()
    {
        return mPlace;
    }

    public long getTimeInMilliseconds()
    {
        return mTimeInMilliseconds;
    }

    public String getUrl()
    {
        return mUrl;
    }
}
