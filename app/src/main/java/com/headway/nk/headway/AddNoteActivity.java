package com.headway.nk.headway;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {
    public HeadwayDBHelper headwayDBHelper=new HeadwayDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        final TextView textView=(TextView) findViewById(R.id.checkpoint_note);

        Button button=(Button) findViewById(R.id.buttondonenote);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                String text=textView.getText().toString();
                intent.putExtra("noteincluded",text);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });



    }

}
