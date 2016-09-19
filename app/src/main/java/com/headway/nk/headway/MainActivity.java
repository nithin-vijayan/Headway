package com.headway.nk.headway;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
