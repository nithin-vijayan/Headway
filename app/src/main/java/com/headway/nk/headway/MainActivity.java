package com.headway.nk.headway;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.MapView;

public class MainActivity extends Activity {

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

    public void newTrip(View view) {
        Intent intent=new Intent(this,MapsActivity.class);
        startActivity(intent);
    }

    public void planItineraries(View view) {

        Intent intent=new Intent(this,TestActivity.class);
        startActivity(intent);

    }
}
