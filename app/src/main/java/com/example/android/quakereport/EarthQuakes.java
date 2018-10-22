package com.example.android.quakereport;

public class EarthQuakes {

    private String magnitude;
    private String location;
    private long timeInMilliseconds;
    private long mTimeInMilliseconds;

    public EarthQuakes(String magnitude, String location, long timeInMilliseconds) {
        this.magnitude = magnitude;
        this.location = location;
        this.timeInMilliseconds = timeInMilliseconds;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }
}
