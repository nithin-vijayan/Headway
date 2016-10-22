package com.headway.nk.headway;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

public class MainActivity extends Activity {

    private static final int ADDTRIP =5 ;
    private Log BaseApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fixing Later Map loading Delay
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();

        setContentView(R.layout.activity_main);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null && data.getExtras().getString("TripName")!=null)
        {
            if(resultCode==RESULT_OK && requestCode==ADDTRIP)
            {
                String text=data.getExtras().getString("TripName");
                //Toast.makeText(this,text,Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(this,MapsActivity.class);
                intent.putExtra("TripName",text);
                startActivity(intent);


            }


        }



    }

    public void newTrip(View view) {

        Intent intent=new Intent(this,TripTitleActivity.class) ;
        startActivityForResult(intent,ADDTRIP);

    }




    public void planItineraries(View view) {

        HeadwayDBHelper headwayDBHelper=new HeadwayDBHelper(this);
        SQLiteDatabase sqLiteDatabase=headwayDBHelper.getWritableDatabase();
        headwayDBHelper.onUpgrade(sqLiteDatabase,1,2);
        sqLiteDatabase.close();
        //Intent intent=new Intent(this,TestActivity.class);
        //startActivity(intent);

    }
}
