package com.rodneygeerlings.myflightinfo.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.example.rodneygeerlings.myflightinfo.R;
import com.rodneygeerlings.myflightinfo.adapters.FlightRecyclerViewAdapter;
import com.rodneygeerlings.myflightinfo.data.FavoritesContract;
import com.rodneygeerlings.myflightinfo.data.FavoritesDbHelper;
import com.rodneygeerlings.myflightinfo.models.Flight;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private RecyclerView rvFavorites;
    private ArrayList<Flight> flights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        rvFavorites = findViewById(R.id.rv_favorites);
        rvFavorites.setNestedScrollingEnabled(false);

        // Create a database helper
        FavoritesDbHelper dbHelper = new FavoritesDbHelper(this);
        database = dbHelper.getWritableDatabase();

        // Get favorites and initialize flights
        Cursor cursorFavorites = getFavorites();
        initFlights(cursorFavorites);
        populateRecyclerView();
    }

    /**
     * Called when the user switches back from flight detail activity to favorites activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Get favorites and initialize flights
        Cursor cursorFavorites = getFavorites();
        initFlights(cursorFavorites);
        populateRecyclerView();
    }

    /**
     * Get the favorites flights from de local database
     *
     * @return
     */
    private Cursor getFavorites() {
        return database.query(
                FavoritesContract.FavoritesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoritesContract.FavoritesEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    /**
     * Populates the recyclerview with the flights defined in the flights array.
     */
    private void populateRecyclerView() {
        FlightRecyclerViewAdapter rvAdapter = new FlightRecyclerViewAdapter(getApplicationContext(), flights);

        // Decide the number of columns based on the screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        rvFavorites.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvFavorites.setAdapter(rvAdapter);
    }

    /**
     * Reads the query result and initialize favorite flights into an array.
     *
     * @param cursorFlights
     */
    private void initFlights(Cursor cursorFlights) {
        try {
            flights = new ArrayList<>();
            while (cursorFlights.moveToNext()) {
                Long flightId = cursorFlights.getLong(cursorFlights.getColumnIndex("flightId"));
                String flightName = cursorFlights.getString(cursorFlights.getColumnIndex("flightName"));
                flights.add(new Flight(flightId, flightName));
            }
        } finally {
            cursorFlights.close();
        }
    }
}
