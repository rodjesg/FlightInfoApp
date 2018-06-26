package com.rodneygeerlings.myflightinfo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.rodneygeerlings.myflightinfo.R;
import com.rodneygeerlings.myflightinfo.data.NetworkUtils;
import com.rodneygeerlings.myflightinfo.models.Flight;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class FlightDetailActivity extends AppCompatActivity {

    private int flightId;
    private Flight flight;

    private TextView tvFlightName;
    private TextView tvFlightNumber;
    private TextView tvFlightDirection;
    /* todo: aanvullen met overige json data uit request */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        Intent intent = getIntent();
        flightId = intent.getExtras().getInt("flightId");
        makeFlightDetailQuery(flightId);

        tvFlightName = findViewById(R.id.tv_flight_name);
        tvFlightNumber = findViewById(R.id.tv_flight_number);
        tvFlightDirection = findViewById(R.id.tv_flight_direction);
    }

    private void makeFlightDetailQuery(int flightId) {
        URL flightDetailUrl = NetworkUtils.buildFlightDetailUrl(flightId);
        new FlightDetailActivity.FlightDetailQueryTask().execute(flightDetailUrl);
    }

    private void parseFlightData(String flightJsonString) throws JSONException {

        // get json data
        JSONObject flightJsonObject = new JSONObject(flightJsonString);
        flight = new Flight(
                flightJsonObject.getString("flightDirection"),
                flightJsonObject.getInt("flightId"),
                flightJsonObject.getString("flightName"),
                flightJsonObject.getString("flightNumber"),
                flightJsonObject.getString("sheduleDate"),
                flightJsonObject.getString("sheduleTime"),
                flightJsonObject.getString("route"),
                flightJsonObject.getString("flightState"),
                flightJsonObject.getString("terminal"),
                flightJsonObject.getString("gate"),
                flightJsonObject.getString("aircraftType"),
                flightJsonObject.getString("landingTime")
        );
    }

    private void bindFlightData() {
        tvFlightName.setText(flight.getFlightName());
        tvFlightNumber.setText(flight.getFlightNumber());
        tvFlightDirection.setText(flight.getFlightDirection());
    }

    /**
     * Inner class that takes care of the query task.
     */
    public class FlightDetailQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL flightDetailUrl = urls[0];
            Log.e("url", flightDetailUrl.toString());
            String flightDetailResults = null;
            try {
                flightDetailResults = NetworkUtils.getResponseFromHttpUrl(flightDetailUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return flightDetailResults;
        }

        /**
         * Executes when the API call is finished.
         */
        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                try {
                    parseFlightData(s);
                    bindFlightData();

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
