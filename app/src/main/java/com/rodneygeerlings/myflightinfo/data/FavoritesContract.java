package com.rodneygeerlings.myflightinfo.data;

import android.provider.BaseColumns;


/**
 * Favorites contract.
 * This class is used to define how the favorites table in the SQL local storage will look like.
 */
public class FavoritesContract {

    /**
     * The properties of the favorites table defined.
     */
    public static final class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_FLIGHT_ID = "flightId";
        public static final String COLUMN_FLIGHT_NAME = "flightName";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}

