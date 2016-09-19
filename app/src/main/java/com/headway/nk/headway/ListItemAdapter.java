package com.headway.nk.headway;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nk on 19/9/16.
 */
public class ListItemAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater=null;

    public ListItemAdapter(Activity a,ArrayList<HashMap<String,String>> d)
    {
        activity=a;
        data=d;
        inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount(){
    return (data == null) ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi=convertView;

        if(convertView==null)
            vi=inflater.inflate(R.layout.list_row_item,null);
        TextView checkpoint_name=(TextView)vi.findViewById(R.id.checkpoint_name);
        TextView asset_count=(TextView)vi.findViewById(R.id.asset_count);

        HashMap<String,String> checkpoint = new HashMap<String,String>();
        checkpoint = data.get(position);

        checkpoint_name.setText(checkpoint.get(MapsActivity.KEY_CHECKPOINT_NAME));
        asset_count.setText(checkpoint.get(MapsActivity.KEY_ASSET_COUNT));


        return vi;
    }
}
