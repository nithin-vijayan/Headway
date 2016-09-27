package com.headway.nk.headway;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private ListItemAdapter adapter;
    private ArrayList<HashMap<String, String>> check_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ListView listView = (ListView) findViewById(R.id.location_list_view_test);
        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("Mumbai");
        your_array_list.add("Pune");
        your_array_list.add("Bangalore");
        your_array_list.add("Chennai");
        your_array_list.add("Delhi");

        check_list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map1 = new HashMap<String, String>();
        HashMap<String, String> map2 = new HashMap<String, String>();
        HashMap<String, String> map3 = new HashMap<String, String>();

        map1.put(MapsActivity.KEY_CHECKPOINT_NAME,"Mumbai");
        map1.put(MapsActivity.KEY_ASSET_COUNT,"10 photos 12 notes");
        check_list.add(map1);
        map2.put(MapsActivity.KEY_CHECKPOINT_NAME,"Chennai");
        map2.put(MapsActivity.KEY_ASSET_COUNT,"110 photos 112 notes");
        check_list.add(map2);
        map3.put(MapsActivity.KEY_CHECKPOINT_NAME,"Kolkata");
        map3.put(MapsActivity.KEY_ASSET_COUNT,"1110 photos 132 notes");
        check_list.add(map3);

        adapter=new ListItemAdapter(this,check_list);
        listView.setAdapter(adapter);


    }

    public void photoButtonClicked(View view) {

        LinearLayout vwParentRow = (LinearLayout)view.getParent();
        Button btnChild = (Button)vwParentRow.getChildAt(0);
        selectImage();

    }

    public void noteButtonClicked(View view) {

        LinearLayout vwParentRow = (LinearLayout)view.getParent();

        Button btnChild = (Button)vwParentRow.getChildAt(1);
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=true;
                //result=Utility.checkPermission(MainActivity.this);
                String userChoosenTask;
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        //cameraIntent();
                    System.out.println(userChoosenTask);
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        //galleryIntent();
                        System.out.println(userChoosenTask);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}

