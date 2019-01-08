package com.example.bama.tracking.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bama.tracking.DirectionsParser;
import com.example.bama.tracking.R;
import com.example.bama.tracking.app.AppController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private Location mLastLocation;
    private Context context;

    LatLng mylatLng;

    MarkerOptions markerOptions = new MarkerOptions();
    LatLng latLang;
    String title;

    Handler mHandler;
    public static final String ID = "id";
    public static final String TITLE = "nama";
    public static final String LAT = "lat";
    public static final String LNG = "lng";

    //private String url = "https://mybam.000webhostapp.com/markers.php";
    private String url = "http://bambangm.com/markers.php";

    String tag_json_obj = "json_obj_req";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        context = getActivity();






        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = getRequestUrl(mylatLng, latLang);
                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                taskRequestDirections.execute(url);
            }
        });




        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar ToolBarAtas2 = view.findViewById(R.id.toolbar_map);
        ((AppCompatActivity)getActivity()).setSupportActionBar(ToolBarAtas2);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);


        // Untuk Mendeteksi apakah GPS aktif atau tidak
        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("GPS Anda Belum Aktif ");
            builder.setMessage("Silahkan Aktifkan GPS !!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }


       mHandler = new Handler();

       mHandler.postDelayed(m_Runnable,5000);

    }


    public final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            getMarkers();

            mHandler.postDelayed(m_Runnable, 5000);
        }

    };//runnable






    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Memulai Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        //getMarkers();





    }

    public void addMarker(LatLng latlang) {
        markerOptions.position(latlang);
        markerOptions.title("Kendaraan Pemilik");
        markerOptions.snippet(""+latlang);
        markerOptions.icon(bitmapDescriptorFromVectorMotor(context, R.drawable.ic_motor));
        mMap.addMarker(markerOptions).showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLang.latitude, latLang.longitude)).zoom(16.0f).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(context, marker.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fungsi get JSON marker
    private void getMarkers() {
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString("koordinat");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        title = jsonObject.getString(TITLE);
                        latLang = new LatLng(Double.parseDouble(jsonObject.getString(LAT)), Double.parseDouble(jsonObject.getString(LNG)));

                        // Menambah data marker untuk di tampilkan ke google map
                        addMarker(latLang);






                        if (mylatLng == null || latLang == null) {
                            Toast.makeText(getActivity(),"Harap Tunggu",Toast.LENGTH_LONG).show();
                        } else {



                        }

                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());


               Toast.makeText(context, "Gangguan Koneksi Internet", Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }




    public BitmapDescriptor bitmapDescriptorFromVectorMotor(Context context, @DrawableRes  int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_motor);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(70, 70, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    //Method Pemilik
    public BitmapDescriptor bitmapDescriptorFromVectorUser(Context context, @DrawableRes  int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_user);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(70, 70, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
         mylatLng = new LatLng(location.getLatitude(), location.getLongitude());


        mMap.addMarker(new MarkerOptions()
                .position(mylatLng)
                .title("Pemilik Kendaraan")
                .snippet(""+mylatLng)
                .icon(bitmapDescriptorFromVectorUser(context, R.drawable.ic_user)));

        //menghentikan pembaruan lokasi
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Izin diberikan.
                    if (ContextCompat.checkSelfPermission(context,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Izin ditolak.
                    Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
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




    private String getRequestUrl(LatLng latLngAwal, LatLng latLngTujuan) {
        String str_org = "origin=" + latLngAwal.latitude + "," + latLngAwal.longitude ;

        String str_dest = "destination=" + latLngTujuan.latitude + "," + latLngTujuan.longitude ;

        String sensor = "sensor=false";

        String mode = "mode=driving";

        String api = "AIzaSyAS4JPBZ5jrkSUBU4jsLcDu91SuxP5iUJE";

        String key = "key=" + api;

        String param =  str_org + "&" + str_dest + "&" +sensor+ "&" +mode+ "&" + key;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;

        return url;


    }

    private String requestDirections(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null ;
        HttpURLConnection httpURLConnection = null ;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection() ;
            httpURLConnection.connect() ;

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader =  new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {

                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                inputStream.close();
            }

            httpURLConnection.disconnect();

        }


        return  responseString;
    }





    public class
    TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected  String doInBackground (String... strings){
            String responseString = "" ;

            try {
                responseString = requestDirections(strings[0]);
            } catch (Exception e){
                e.printStackTrace();
            }
            return  responseString ;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }


    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(10);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getActivity(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }

















}
