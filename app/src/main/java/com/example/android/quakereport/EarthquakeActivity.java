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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<EarthQuakes>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private EarthQuakesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mAdapter = new EarthQuakesAdapter(getBaseContext(), new ArrayList<EarthQuakes>());
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuakes currentQuakes = mAdapter.getItem(position);
                Uri earthQuakeUri = Uri.parse(currentQuakes.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, earthQuakeUri);
                startActivity(intent);
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            View loadingBar = findViewById(R.id.loading);
            loadingBar.setVisibility(View.GONE);
            setEmptyView("No internet connection.");
        }

    }

    @Override
    public Loader<List<EarthQuakes>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uribuilder = baseUri.buildUpon();

        uribuilder.appendQueryParameter("format", "geojson");
        uribuilder.appendQueryParameter("limit", "10");
        uribuilder.appendQueryParameter("minmag", minMagnitude);
        uribuilder.appendQueryParameter("orderby", orderBy);

        return new EarthQuakeLoader(this, uribuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuakes>> loader, List<EarthQuakes> data) {
        View loadingBar = findViewById(R.id.loading);
        loadingBar.setVisibility(View.GONE);
        setEmptyView("No earthquakes found.");
        mAdapter.clear();
        if (data != null && !data.isEmpty()){
            mAdapter.addAll(data);
        }
    }

    public void setEmptyView(String message) {
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        TextView emptyView = (TextView) findViewById(R.id.empty);
        emptyView.setText(message);
        earthquakeListView.setEmptyView(emptyView);
    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuakes>> loader) {
        mAdapter.clear();
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
}
