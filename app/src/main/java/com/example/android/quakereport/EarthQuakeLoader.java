package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuakes>> {

    private static final String LOG_TAG = EarthQuakeLoader.class.getName();

    private String mUrl;

    public EarthQuakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<EarthQuakes> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the HTTP request for earthquake data and process the response.
        List<EarthQuakes> result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }
}
