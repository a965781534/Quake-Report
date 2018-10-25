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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

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

        EarthQuakeTask earthQuakeTask = new EarthQuakeTask();
        earthQuakeTask.execute(USGS_REQUEST_URL);

    }

    private class EarthQuakeTask extends AsyncTask<String, Void, ArrayList<EarthQuakes>>{

        @Override
        protected ArrayList<EarthQuakes> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null){
                return null;
            }
            // Perform the HTTP request for earthquake data and process the response.
            ArrayList<EarthQuakes> result = (ArrayList<EarthQuakes>) QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<EarthQuakes> data) {
            mAdapter.clear();
            if (data != null && !data.isEmpty()){
                mAdapter.addAll(data);
            }
        }

    }

}
