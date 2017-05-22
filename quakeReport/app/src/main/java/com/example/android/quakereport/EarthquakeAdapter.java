package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class EarthquakeAdapter extends ArrayAdapter<Earthquake>
{
    private String primaryLocation, locationOffset, magnitude;
    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Activity context, List<Earthquake> earthquake)
    {
        super(context, 0, earthquake);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);

        Earthquake currentItem = getItem(position);

        /**
         * Find the TextView in {@layout earthquake_list_item.xml} with the respective ID.
         * Set the text to the view.
         */
        // Format the text to always showing 1 decimal place
        DecimalFormat formatter = new DecimalFormat("0.0");
        magnitude = formatter.format(currentItem.getMagnitude());
        TextView magTextView = (TextView) listItemView.findViewById(R.id.mag_text_view);
        magTextView.setText(magnitude);
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentItem.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        /**
         * Get the full string for the location from {@Earthquake currentItem} object and check if there is or not a location offset.
         * If it has, then will split and store in different variables to use at {@TextView location_offset_text_view} and {@TextView primary_location_text_view}
         */
        String originalLocation = currentItem.getPlace();
        if(originalLocation.contains(LOCATION_SEPARATOR))
        {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }
        else
        {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.location_offset_text_view);
        locationOffsetTextView.setText(locationOffset);
        TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.primary_location_text_view);
        primaryLocationTextView.setText(primaryLocation);

        /**
         * Find the Date and the Time TextViews and create an object from them.
         * Then create a {@Date dateObject} object to get the time in milliseconds and convert it to a date and time format,
         * calling {@method formatDate} and {@method formatTime}
         */
        Date dateObject = new Date(currentItem.getTimeInMilliseconds());
        String formattedDate = formatDate(dateObject);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(formattedDate);

        String formattedTime = formatTime(dateObject);
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);
        timeTextView.setText(formattedTime);

        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Get the right color to the magnitude circle accordingly to the magnitude value
     * @param magnitude is the magnitude received from the query
     * @return the int value for the color
     */
    private int getMagnitudeColor(double magnitude)
    {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
