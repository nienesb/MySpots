package com.teamdating.myspots;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class SpotsDBSchema {

    public static final class SpotsTable {
        public static final String NAME = "spots";

        public static final class Colums {
            public static final String _id = "_id";
            public static final String TITLE = "name";
            public static final String CITY = "city";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
        }
    }
}

