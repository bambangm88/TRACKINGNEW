package com.example.bama.tracking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bama.tracking.Login.Login;
import com.example.bama.tracking.Service.serviceNotify;
import com.example.bama.tracking.fragment.AlatFragment;
import com.example.bama.tracking.fragment.KronologiFragment;
import com.example.bama.tracking.fragment.MapFragment;
import com.example.bama.tracking.fragment.PetunjukFragment;
import com.example.bama.tracking.fragment.TentangFragment;

public class MainActivity extends AppCompatActivity
         {

             SharedPreferences sharedpreferences;
    Fragment fragment ;
    Toolbar toolbar;

             public static final String TAG_ID = "id";
             public static final String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        //---

        startService(new Intent(MainActivity.this, serviceNotify.class));

        loadFragment(new MapFragment());



        //cek koneksi internet
        checkInternet() ;


    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.me:

                    fragment = new MapFragment();
                    loadFragment(fragment) ;
                    return true;

                case R.id.kronologi:

                    fragment = new KronologiFragment();
                    loadFragment(fragment) ;
                    return true;

                case R.id.alat:

                    fragment = new AlatFragment();
                    loadFragment(fragment) ;
                    return true;

                case R.id.petunjuk:
                    fragment = new PetunjukFragment();
                    loadFragment(fragment) ;
                    return true;

                case R.id.tentang:
                    fragment = new TentangFragment();
                    loadFragment(fragment) ;

                    return true;
            }
            return false;
        }
    };













    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.petunjuk) {
            return true;
        }

        if (id == R.id.keluar) {
            showAlertLogOut() ;
        }


        if (id == R.id.hidup) {
            serviceNotify.notif = "nyala" ;
            Toast.makeText(this, "Notif Menyala",Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.mati) {
            serviceNotify.notif = "mati" ;
            Toast.makeText(this, "Notif Mati",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


//perintah keluar ------------
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Tutup Aplikasi ini ?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//----------------

             //perintah keluar ------------
             private void showAlertLogOut() {
                 AlertDialog.Builder builder = new AlertDialog.Builder(this)
                         .setMessage("Keluar Akun Anda ? ?")
                         .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 dialogInterface.dismiss();
                             }
                         })
                         .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 dialogInterface.dismiss();

                                 sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);


                                 // TODO Auto-generated method stub
                                 // update login session ke FALSE dan mengosongkan nilai id dan username
                                 SharedPreferences.Editor editor = sharedpreferences.edit();
                                 editor.putBoolean(Login.session_status, false);
                                 editor.putString(TAG_ID, null);
                                 editor.putString(TAG_USERNAME, null);
                                 editor.commit();

                                 Intent intent = new Intent(MainActivity.this, Login.class);
                                 finish();
                                 startActivity(intent);
                             }
                         });
                 AlertDialog dialog = builder.create();
                 dialog.show();
             }
//----------------




//cek koneksi internet ------------
    public boolean checkInternet(){
        boolean connectStatus = true;
        ConnectivityManager ConnectionManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ) {
            connectStatus = true;
        }
        else {
            connectStatus = false;
        }
        return connectStatus;
    }



}
