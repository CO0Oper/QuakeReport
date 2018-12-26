package com.example.android.quakereport;

public class Report {

    private Double magnitude;

    private String location;

    private long timeInMilliseconds;

    private String url;

    public Report(Double Magnitude, String Location, long TimeInMilliseconds, String Url){
        magnitude = Magnitude;
        location = Location;
        timeInMilliseconds = TimeInMilliseconds;
        url = Url;
    }

    public double getMagnitude(){
        return magnitude;
    }

    public String getLocation(){
        return location;
    }

    /**
     * Return the time of the earthquake
     * @return
     */
    public long getTimeInMilliseconds(){
        return timeInMilliseconds;
    }

    /**
     * Returns the website URL to find more information about the earthquake.
     */
    public String getUrl() {
        return url;
    }

}
