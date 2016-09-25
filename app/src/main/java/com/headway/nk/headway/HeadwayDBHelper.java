package com.headway.nk.headway;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*

 */
public class HeadwayDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String  DATABASE_NAME="headwaydatabase.db";
    private static final String CREATE_TABLE_TRIP="CREATE TABLE " +
            HeadwayDBContract.TripTable.HEADWAY_TRIP_TABLE +
            " ( " +
            HeadwayDBContract.TripTable.COLUMN_NAME_TRIP + " VARCHAR(30)" +
            " );";

    private static final String CREATE_TABLE_CHECKPOINT="CREATE TABLE " +
            HeadwayDBContract.CheckpointTable.HEADWAY_CHECKPOINT_TABLE+
            "( " +
            HeadwayDBContract.CheckpointTable.COLUMN_NAME_TRIP_ID+" INTEGER , " +
            HeadwayDBContract.CheckpointTable.COLUMN_NAME_CHECKPOINT+" VARCHAR(30) , " +
            HeadwayDBContract.CheckpointTable.COLUMN_NAME_NOTE_COUNT+ " INTEGER , " +
            HeadwayDBContract.CheckpointTable.COLUMN_NAME_PHOTO_COUNT+" INTEGER , " +
            " FOREIGN KEY("+HeadwayDBContract.CheckpointTable.COLUMN_NAME_TRIP_ID+") REFERENCES "+
            HeadwayDBContract.TripTable.HEADWAY_TRIP_TABLE+"(_id+)"+
            " );";

    private static final String SQL_DELETE_TRIP =
            "DROP TABLE IF EXISTS " + HeadwayDBContract.TripTable.HEADWAY_TRIP_TABLE;
    private static final String SQL_DELETE_CHECKPOINT =
            "DROP TABLE IF EXISTS " + HeadwayDBContract.CheckpointTable.HEADWAY_CHECKPOINT_TABLE;

    public HeadwayDBHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_TRIP);
        db.execSQL(CREATE_TABLE_CHECKPOINT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
