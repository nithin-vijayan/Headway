package com.headway.nk.headway;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.Manifest;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    static final String KEY_CHECKPOINT_NAME = "checkpoint_name";
    static final String KEY_ASSET_COUNT = "asset_count";
    private static final float LOCATION_REFRESH_DISTANCE = 12;
    private static final boolean DEVELOPER_MODE = true;
    private static final int REQUEST_CAMERA =0 ;
    private static final int REQUEST_GALLERY =1 ;
    private static final int ADD_NOTE =3 ;
    private static int THUMBNAIL_SIZE=42;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location mLastLocation;
    private String mLatitudeText;
    private String mLongitudeText;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private ListItemAdapter adapter;
    private ArrayList<HashMap<String, String>> check_list;
    private String currentLocationString;
    private ImageView imageview;
    private int REQUEST_CODE=1231;
    private boolean mReturnWithResult=false;
    private int requestCodeResumed;
    private int resultCodeResumed;
    private Intent imageReturnedIntentResumed;
    private Bitmap bitmap;
    private LinearLayout vwParentRow;
    private HeadwayDBHelper headwayDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.actionmenu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_done_trip)
        {
            Toast.makeText(this,"Done Adding Trip",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        headwayDBHelper=new HeadwayDBHelper(this);
        sqLiteDatabase=headwayDBHelper.getWritableDatabase();
        Uri bitmapUri;
        Intent data=getIntent();

        if(data!=null && data.getExtras().getString("TripName")!=null) {
            String text = data.getExtras().getString("TripName");
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }

        if(mReturnWithResult && imageReturnedIntentResumed!=null)
        {
            System.out.print(imageReturnedIntentResumed.getExtras()!=null);
            //System.out.print(imageReturnedIntentResumed.getExtras().get("data")!=null);
            imageview=new ImageView(this);
            switch(requestCodeResumed) {
                case 0:
                    if(resultCodeResumed == RESULT_OK && imageReturnedIntentResumed.getParcelableExtra("data") != null){
                        bitmap=  imageReturnedIntentResumed.getParcelableExtra("data");

                    }

                    break;
                case 1:
                    if(resultCodeResumed == RESULT_OK && imageReturnedIntentResumed.getData() != null){
                        bitmapUri= imageReturnedIntentResumed.getData();
                        try {
                            //bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageReturnedIntent.getData());
                        bitmap=getThumbnail(bitmapUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }
                    break;
                case 3:
                    if(resultCodeResumed==RESULT_OK )
                    {
                        String text=imageReturnedIntentResumed.getExtras().getString("noteincluded");
                        Toast.makeText(this,"Note Added",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(this,imageReturnedIntentResumed.getExtras().getString("noteincluded"),Toast.LENGTH_SHORT).show();
                        //System.out.println(""+imageReturnedIntentResumed.getExtras().getString("noteincluded").toString());
                    }
                    break;
            }

            //BitmapFactory.Options opts = new BitmapFactory.Options();
            //opts.inSampleSize = 4;
            //Bitmap newBitmap = BitmapFactory.decodeFile(bitmapUri.getPath(), opts);
            LinearLayout thumbnailLayoutView = (LinearLayout) vwParentRow.getParent().getParent();
            ImageView thumbview=(ImageView)thumbnailLayoutView.getChildAt(2);
            thumbview.setImageBitmap(bitmap);

        }
        mReturnWithResult=false;

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private synchronized void buildGoogleAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        buildGoogleAPIClient();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
        LatLng MUMBAI = new LatLng(19.0767311, 72.875607);
        LatLng PUNE = new LatLng(18.5128501, 73.861448);


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
        } else {


        }
        mMap.setMyLocationEnabled(true);
        //Location myLocation = googleMap.;  //Nullpointer exception.........
        //LatLng myLatLng = new LatLng(myLocation.getLatitude(),
        //        myLocation.getLongitude());
        mMap.setOnInfoWindowClickListener(this);

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

        check_list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map1 = new HashMap<String, String>();
        HashMap<String, String> map2 = new HashMap<String, String>();
        HashMap<String, String> map3 = new HashMap<String, String>();

        map1.put(MapsActivity.KEY_CHECKPOINT_NAME, "Mumbai");
        map1.put(MapsActivity.KEY_ASSET_COUNT, "10 photos 12 notes");
        check_list.add(map1);
        map2.put(MapsActivity.KEY_CHECKPOINT_NAME, "Chennai");
        map2.put(MapsActivity.KEY_ASSET_COUNT, "110 photos 112 notes");
        check_list.add(map2);
        map3.put(MapsActivity.KEY_CHECKPOINT_NAME, "Kolkata");
        map3.put(MapsActivity.KEY_ASSET_COUNT, "1110 photos 132 notes");
        check_list.add(map3);


        adapter = new ListItemAdapter(this, check_list);
        listView.setAdapter(adapter);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void reverseGeocode(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            System.out.println(addresses.get(0).getLocality());
            currentLocationString = addresses.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.snippet("Tap to add checkpoint");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        reverseGeocode(location);

    }

    @Override
    public void onPause() {
        super.onPause();

        if(sqLiteDatabase!=null)
            sqLiteDatabase.close();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        HashMap<String, String> map4 = new HashMap<String, String>();

        map4.put(MapsActivity.KEY_CHECKPOINT_NAME, currentLocationString);
        map4.put(MapsActivity.KEY_ASSET_COUNT, "10 photos 12 notes");
        check_list.add(0, map4);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "CheckPoint Added",
                Toast.LENGTH_SHORT).show();
    }

    public void photoButtonClicked(View view) {

        vwParentRow = (LinearLayout) view.getParent();
        Button btnChild = (Button) vwParentRow.getChildAt(0);
        selectImage();

    }

    public void noteButtonClicked(View view) {

        vwParentRow = (LinearLayout) view.getParent();

        Button btnChild = (Button) vwParentRow.getChildAt(1);
        addnote();
    }

    private void addnote() {
        Intent intent=new Intent(this,AddNoteActivity.class);
        startActivityForResult(intent,ADD_NOTE);
    }

    private void selectImage() {
             final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                boolean result=true;
                result=Utility.checkPermission(MapsActivity.this);
                String userChoosenTask;
                if (items[item].equals(items[0])) {
                    userChoosenTask="Take Photo";
                    if(result) {
                        cameraIntent();
                        System.out.println(userChoosenTask);
                    }
                } else if (items[item].equals(items[1])) {
                    userChoosenTask="Choose from Library";
                    if(result) {
                        galleryIntent();
                        System.out.println(userChoosenTask);

                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
        pickPhoto.setType("image/*");
        if (pickPhoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPhoto, REQUEST_GALLERY);
        }

    }

    private void cameraIntent()
    {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
       super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        mReturnWithResult=true;
        resultCodeResumed=resultCode;
        requestCodeResumed=requestCode;
        imageReturnedIntentResumed=imageReturnedIntent;
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException{
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither=true;//optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    public long InsertTripData(String tripname)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(HeadwayDBContract.TripTable.COLUMN_NAME_TRIP,tripname);
        long id=sqLiteDatabase.insert(HeadwayDBContract.TripTable.HEADWAY_TRIP_TABLE,null,contentValues);
        return id;

    }

    public void InsertCheckpointData(long tripid,String checkpointname)
    {

        ContentValues contentValues=new ContentValues();
        contentValues.put(HeadwayDBContract.CheckpointTable.COLUMN_NAME_TRIP_ID,tripid);
        contentValues.put(HeadwayDBContract.CheckpointTable.COLUMN_NAME_NOTE_COUNT,0);
        contentValues.put(HeadwayDBContract.CheckpointTable.COLUMN_NAME_PHOTO_COUNT,0);
        contentValues.put(HeadwayDBContract.CheckpointTable.COLUMN_NAME_CHECKPOINT,checkpointname);
        sqLiteDatabase.insert(HeadwayDBContract.CheckpointTable.HEADWAY_CHECKPOINT_TABLE, null,contentValues);

    }




}