package com.rodneygeerlings.myflightinfo.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodneygeerlings.myflightinfo.R;
import com.rodneygeerlings.myflightinfo.data.FavoritesContract;
import com.rodneygeerlings.myflightinfo.data.FavoritesDbHelper;
import com.rodneygeerlings.myflightinfo.data.NetworkUtils;
import com.rodneygeerlings.myflightinfo.models.Flight;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class FlightDetailActivity extends AppCompatActivity {

    private Flight flight;

    private TextView tvFlightName;
    private TextView tvFlightNumber;
    private TextView tvFlightDirection;
    private TextView tvFlightScheduleDate;
    private TextView tvFlightScheduleTime;
    private TextView tvFlightRoute;
    private TextView tvFlightPublicFlightState;
    private TextView tvFlightTerminal;
    private TextView tvFlightGate;
    private TextView tvFlightAircraftType;
    private Button btnAddToFavorites;
    private SQLiteDatabase database;
    private Long flightId;

    /* todo: aanvullen met overige json data uit request */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        Intent intent = getIntent();
        flightId = intent.getExtras().getLong("FlightId");
        makeFlightDetailQuery(flightId);

        tvFlightName = findViewById(R.id.tv_flight_name);
        tvFlightNumber = findViewById(R.id.tv_flight_number);
        tvFlightDirection = findViewById(R.id.tv_flight_direction);
        tvFlightScheduleDate = findViewById(R.id.tv_flight_scheduledate);
        tvFlightScheduleTime = findViewById(R.id.tv_flight_scheduletime);;
        tvFlightRoute = findViewById(R.id.tv_flight_route);;
        tvFlightPublicFlightState = findViewById(R.id.tv_flight_publicflightstate);;
        tvFlightTerminal = findViewById(R.id.tv_flight_flightterminal);;
        tvFlightGate = findViewById(R.id.tv_flight_gate);;
        tvFlightAircraftType = findViewById(R.id.tv_flight_aircraftType);;
        btnAddToFavorites = findViewById(R.id.btn_add_to_favorites);

        // Create db helpen
        FavoritesDbHelper dbHelper = new FavoritesDbHelper(this);
        database = dbHelper.getReadableDatabase();

        // Set favorites button text
        String txtBtnFavorites;
        if (isInFavorites()) {
            txtBtnFavorites = "Verwijder van favorieten";
        } else {
            txtBtnFavorites = "Voeg toe aan favorieten";
        }
        btnAddToFavorites.setText(txtBtnFavorites);

        // Set on click listener on favorite button
        btnAddToFavorites.setOnClickListener(new View.OnClickListener() {
            int duration = Toast.LENGTH_LONG;

            public void onClick(View v) {
                if (isInFavorites() && removeFromFavorites()) {
                    Toast toast = Toast.makeText(getApplicationContext(),"Vlucht " + flight.getFlightName() + " succesvol verwijderd." , duration);
                    toast.show();
                    btnAddToFavorites.setText("Voeg toe aan favorieten");
                } else if (addToFavorites()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Vlucht " + flight.getFlightName() + " succesvol toegevoegd.", duration);
                    toast.show();
                    btnAddToFavorites.setText("Verwijder van favorieten");
                }
            }
        });


    }

    private void makeFlightDetailQuery(Long flightId) {
        URL flightDetailUrl = NetworkUtils.buildFlightDetailUrl(flightId);
        new FlightDetailActivity.FlightDetailQueryTask().execute(flightDetailUrl);
    }

    private void parseFlightData(String flightJsonString) throws JSONException {

        // get json data
        JSONObject flightJsonObject = new JSONObject(flightJsonString);

        flight = new Flight(
                flightJsonObject.getString("flightDirection"),
                flightJsonObject.getLong("id"),
                flightJsonObject.getString("flightName"),
                flightJsonObject.getString("flightNumber"),
                flightJsonObject.getString("scheduleDate"),
                flightJsonObject.getString("scheduleTime"),
                flightJsonObject.getString("route"),
                flightJsonObject.getJSONObject("publicFlightState").getJSONArray("flightStates").get(0).toString(),
                flightJsonObject.getString("terminal"),
                flightJsonObject.getString("gate"),
                flightJsonObject.getString("aircraftType")
        );
    }

    private void bindFlightData() {
        tvFlightName.setText(flight.getFlightName());
        tvFlightNumber.setText(flight.getFlightNumber());
        tvFlightDirection.setText(flight.getFlightDirection());
        tvFlightAircraftType.setText(flight.getAircraftType());
        tvFlightPublicFlightState.setText(flight.getFlightState());
        tvFlightGate.setText(flight.getGate());
        tvFlightRoute.setText(flight.getRoute());
        tvFlightScheduleDate.setText(flight.getScheduleDate());
        tvFlightScheduleTime.setText(flight.getScheduleTime());
        tvFlightDirection.setText(flight.getFlightDirection());
        tvFlightTerminal.setText(flight.getTerminal());



        Log.d("1", flight.getFlightName());
        Log.d("2", flight.getFlightNumber());
        Log.d("3", flight.getFlightDirection());
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
            Log.d("url", flightDetailUrl.toString());
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

    /**
     * Checks if the current flight is in the user's favorites.
     *
     * @return Boolean
     * The flight is in the user's favorites.
     */
    private Boolean isInFavorites() {
        Cursor mCursor = database.rawQuery(
                "SELECT * FROM " + FavoritesContract.FavoritesEntry.TABLE_NAME +
                        " WHERE " + FavoritesContract.FavoritesEntry.COLUMN_FLIGHT_ID + "= " + flightId
                , null);

        return mCursor.moveToFirst();
    }

    /**
     * Adds the current flight to the user's favorites using local storage.
     *
     * @return
     * Succeeded to add the flight to the user's favorites.
     */
    private Boolean addToFavorites() {
        ContentValues values = new ContentValues();
        // add values to record keys
        values.put(FavoritesContract.FavoritesEntry.COLUMN_FLIGHT_ID, flightId);
        values.put(FavoritesContract.FavoritesEntry.COLUMN_FLIGHT_NAME, flight.getFlightName());

        return database.insert(FavoritesContract.FavoritesEntry.TABLE_NAME, null, values) > 0;
    }

    /**
     * Removes the flight from the user's favorites.
     *
     * @return
     * Succeeded to remove the flight from the user's favorites.
     */
    private Boolean removeFromFavorites() {
        return database.delete(FavoritesContract.FavoritesEntry.TABLE_NAME, FavoritesContract.FavoritesEntry.COLUMN_FLIGHT_ID + " = " + flightId, null) > 0;
    }
}
