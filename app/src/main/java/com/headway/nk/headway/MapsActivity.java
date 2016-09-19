package com.headway.nk.headway;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    static final String KEY_CHECKPOINT_NAME = "checkpoint_name";
    static final String KEY_ASSET_COUNT = "asset_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ListView listView = (ListView) findViewById(R.id.location_list_view);
        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("Mumbai");
        your_array_list.add("Pune");
        your_array_list.add("Bangalore");
        your_array_list.add("Chennai");
        your_array_list.add("Delhi");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

       // listView.setAdapter(arrayAdapter);

        ArrayList<HashMap<String, String>> check_list = new ArrayList<HashMap<String, String>>();
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


        ListItemAdapter adapter=new ListItemAdapter(this,check_list);
        listView.setAdapter(adapter);





    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng MUMBAI = new LatLng(19.0767311,72.875607);
        LatLng PUNE = new LatLng(18.5128501,73.861448);


        // Add a marker in Sydney and move the camera
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(MUMBAI);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);



            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        else{


        }
        mMap.setMyLocationEnabled(true);
        //Location myLocation = googleMap.;  //Nullpointer exception.........
        //LatLng myLatLng = new LatLng(myLocation.getLatitude(),
        //        myLocation.getLongitude());

        Marker mumbai = mMap.addMarker(new MarkerOptions()
                .position(MUMBAI)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        Marker pune = mMap.addMarker(new MarkerOptions()
                .position(PUNE)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        //Marker myLocationMarker = mMap.addMarker(new MarkerOptions()
         //       .position(myLatLng)
         //       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);


    }
}
