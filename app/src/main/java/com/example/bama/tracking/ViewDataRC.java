package com.example.bama.tracking;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;

import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ViewDataRC extends AppCompatActivity {


    TextView tanggal,waktu,lat,longi,speed,feet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_rc);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbar_view_rc);
        setSupportActionBar(ToolBarAtas2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tanggal = (TextView) findViewById(R.id.tvTanggal);
        waktu = (TextView) findViewById(R.id.tvWaktu);
        lat = (TextView) findViewById(R.id.tvLat);

        longi = (TextView) findViewById(R.id.tvLong);
        speed = (TextView) findViewById(R.id.tvSp);
        feet = (TextView) findViewById(R.id.tvFt);

        /*get data from intent*/
        Intent data = getIntent();
        String intentWaktu = data.getStringExtra("waktu") ;
        String intentTanggal = data.getStringExtra("tanggal") ;
        String intentLat    = data.getStringExtra("latitude") ;
        String intentLong = data.getStringExtra("longitude") ;
        String intentSpeed = data.getStringExtra("speed") ;
        String intentFeet   = data.getStringExtra("feet") ;


        tanggal.setText(intentTanggal);
        waktu.setText(intentWaktu);
        lat.setText(intentLat);
        longi.setText(intentLong);
        speed.setText(intentSpeed);
        feet.setText(intentFeet);

        Button btnlokasi = (Button) findViewById(R.id.btnLok);

    }

    public void btnLokasiKlik (View v) {

        Intent intent = new Intent(ViewDataRC.this, MapCekLokasi.class);
        intent.putExtra("latitude", lat.getText()) ;
        intent.putExtra("longitude", longi.getText());
        startActivity(intent);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






}
