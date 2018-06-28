package com.rodneygeerlings.myflightinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.rodneygeerlings.myflightinfo.R;
import com.rodneygeerlings.myflightinfo.activities.ContactActivity;
import com.rodneygeerlings.myflightinfo.activities.FlightDetailActivity;
import com.rodneygeerlings.myflightinfo.activities.WeatherActivity;
import com.rodneygeerlings.myflightinfo.adapters.FlightRecyclerViewAdapter;
import com.rodneygeerlings.myflightinfo.data.NetworkUtils;
import com.rodneygeerlings.myflightinfo.models.Flight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FlightRecyclerViewAdapter rvAdapter;
    private ArrayList<Flight> flights;
    private RecyclerView rvFlightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flights = new ArrayList<>();
        rvAdapter = new FlightRecyclerViewAdapter(getApplicationContext(), flights);
        rvFlightList = findViewById(R.id.rv_flight_list);
        rvFlightList.setAdapter(rvAdapter);
        rvFlightList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        // Starts the query
        makeFlightOverviewQuery();
    }

    /**
     * Makes the query to get a list of flights
     */
    private void makeFlightOverviewQuery() {
        URL flightOverviewUrl = NetworkUtils.buildFlightOverviewUrl();
        new FlightOverviewQueryTask().execute(flightOverviewUrl);
    }

    /**
     * Parses the flights JSON string and stores them into the movie array.
     *
     * @param flightsJSONString
     */
    private void parseFlights(String flightsJSONString) throws JSONException {
        Log.d("test", flightsJSONString);
        JSONObject resultJSONObject = new JSONObject(flightsJSONString);
        JSONArray flightsJSONArray = resultJSONObject.getJSONArray("flights");

        // Loop throught the JSON array results
        for (int i = 0; i < flightsJSONArray.length(); i++) {
            JSONObject flightJSONObject = new JSONObject(flightsJSONArray.get(i).toString());
            Long flightId = flightJSONObject.getLong("id");
            String flightName = flightJSONObject.getString("flightName");

            Log.d("name", flightName);

            // Add new flight object to the flights array
            flights.add(new Flight(flightId, flightName));
        }
    }

    /**
     * Populates the recyclerview with the retrieved movies.
     */
    private void populateRecyclerView() {
        // update the flight in the adapter with the retrieved ones from the API call
        rvAdapter.flights = flights;
        rvAdapter.notifyDataSetChanged();
    }

    /**
     * Inner class that takes care of the query task.
     */
    public class FlightOverviewQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL flightsUrl = urls[0];
            String flightsResults = null;
            try {
                flightsResults = NetworkUtils.getResponseFromHttpUrl(flightsUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return flightsResults;
        }

        /**
         * Executes when the API call is finished.
         */
        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                try {
                    parseFlights(s);
                    populateRecyclerView();

                } catch (JSONException e) {
                    e.printStackTrace();
                    // show error message
                }
            } else {
                // show error message
            }
        }
    }
}