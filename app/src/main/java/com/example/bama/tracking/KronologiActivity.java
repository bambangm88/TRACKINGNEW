package com.example.bama.tracking;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.app.ProgressDialog;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bama.tracking.Adapter.Adapter_Data;

import com.example.bama.tracking.app.AppController;
import com.example.bama.tracking.Adapter.ModelData;
import com.example.bama.tracking.Adapter.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KronologiActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelData> mItems;
    FloatingActionButton btnInsert;
    ProgressDialog pd;
    private static final String TAG = KronologiActivity.class.getSimpleName();


    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kronologi);

        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        // Mengeset properti warna yang berputar pada SwipeRefreshLayout

        // Mengeset listener yang akan dijalankan saat layar di refresh/swipe

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                setTextName();
            }
        });

        mRecyclerview = (RecyclerView) findViewById(R.id.rvTracking);

        pd = new ProgressDialog(KronologiActivity.this);
        mItems = new ArrayList<>();


        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtas2 = (Toolbar) findViewById(R.id.toolbar_kronologi);
        setSupportActionBar(ToolBarAtas2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadJson();

        mManager = new LinearLayoutManager(KronologiActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new Adapter_Data(KronologiActivity.this, mItems);
        mRecyclerview.setAdapter(mAdapter);


    }

    @Override
    public void onRefresh() {
        setTextName();
    }

    private void setTextName(){
        mItems.clear();
        mAdapter.notifyDataSetChanged();
        loadJson();
        mSwipeRefreshLayout.setRefreshing(false);

    }


    private void loadJson() {

       pd.setMessage("Mengambil Data Lokasi Kendaraan");
        pd.setCancelable(false);
        pd.show();

        // String URL_DATA = "https://mybam.000webhostapp.com/get_data.php";
        String URL_DATA = "http://bambangm.com/get_data.php";

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST,URL_DATA, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.cancel();
                        Log.d("volley", "response : " + response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData md = new ModelData();
                                md.setTanggal(data.getString("tanggal"));
                                md.setWaktu(data.getString("waktu"));
                                md.setLatitude(data.getString("latitude"));
                                md.setLongitude(data.getString("longitude"));
                                md.setSpeed(data.getString("speed"));
                                md.setFeet(data.getString("feet"));
                                mItems.add(md);
                            } catch (JSONException e) {
                                pd.cancel();
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(reqData);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kronologi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.delete) {
            showAlertDialog() ;
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    //perintah delete ------------
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Bersihkan data?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete();
                        mItems.clear();
                        mAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  "Data Berhasil Dihapus . .", Snackbar.LENGTH_SHORT);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(ContextCompat.getColor(KronologiActivity.this, R.color.hijau));
                        snackbar.show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//----------------


    // fungsi untuk menghapus
    private void delete(){
        pd.setMessage("Menghapus Data");
        pd.setCancelable(false);
        pd.show();

        String URL_DATA_HAPUS = "http://bambangm.com/delete_data.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_DATA_HAPUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pd.cancel();
                Log.d(TAG,"Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());


                        mAdapter.notifyDataSetChanged();

                    } else {

                    }
                } catch (JSONException e) {
                    // JSON error
                     e.printStackTrace();
                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                  Log.d("volley", "error : " + error.getMessage());
               // Toast.makeText(KronologiActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();


            }
        });

        AppController.getInstance().addToRequestQueue(strReq,"message");
    }





}



































































































































































































































