package com.headway.nk.headway;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TripTitleActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_title);
        final TextView textView=(TextView) findViewById(R.id.tripname);

        Button button=(Button) findViewById(R.id.addtripname);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                String text=textView.getText().toString();
                intent.putExtra("TripName",text);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });


    }
}
