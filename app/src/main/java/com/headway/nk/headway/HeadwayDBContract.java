package com.headway.nk.headway;

import android.provider.BaseColumns;

/**
 * Created by nk on 25/9/16.
 */
public class HeadwayDBContract {

    private HeadwayDBContract(){}

    public static class TripTable implements BaseColumns{

        public static final String HEADWAY_TRIP_TABLE="Trip_table";
        public static final String COLUMN_NAME_TRIP="Trip_Name";

    }

    public static class CheckpointTable implements BaseColumns{

        public static final String HEADWAY_CHECKPOINT_TABLE="checkpoint_table";
        public static final String COLUMN_NAME_TRIP_ID="Trip_ID";
        public static final String COLUMN_NAME_CHECKPOINT="Check_Point_Name";
        public static final String COLUMN_NAME_PHOTO_COUNT="Photo_Count";
        public static final String COLUMN_NAME_NOTE_COUNT="Note_Count";


    }

}
