package com.example.bama.tracking.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.bama.tracking.MainActivity;
import com.example.bama.tracking.R;
import com.example.bama.tracking.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class serviceNotify extends Service {

    private static DecimalFormat df2 = new DecimalFormat(".##");
    String tag_json_obj = "json_obj_req";
    public static String Klat_satu,Klong_satu ;
    public static String Klat_dua,Klong_dua ;
    public static String notif = "nyala" ;


    Handler mHandler ;

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        Toast.makeText(this, "Memulai Aplikasi . .", Toast.LENGTH_LONG).show();



        mHandler = new Handler();

        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable,7000);


        return START_STICKY;
    }


    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {

            koordinat_1();

            mHandler.postDelayed(m_Runnable, 7000);

        }

    };//runnable




    private void showNotif() {

        NotificationManager notificationManager;


        Intent mIntent = new Intent(this,MainActivity.class);
        Bundle bundle = new Bundle();


        bundle.putString("MOTOR ANDA BERADA !!!!!", "NOTIFIKASI BARU");
        mIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setColor(getResources().getColor(R.color.colorAccent));
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_user)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_user))
                .setTicker("Go-Track")
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("MOTOR DILUAR BATAS RADIUS")
                .setContentText("SEGERA CEK KENDARAAN");

        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(115, builder.build());
    }




    public void getJarak(double lat1, double lon1, double lat2, double lon2 , double speed) {

        final int R = 6371; // Radious of the earth

        Double latDistance = toRad(lat2 - lat1);

        Double lonDistance = toRad(lon2 - lon1);

        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +

                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *

                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        Double distance = R * c;

        double n =  distance * 1000 ;

        System.out.println("Jarak :" +df2.format(n));

        String f = "nyala" ;

        if (n >= 100 && notif == f){
            showNotif();

            Toast.makeText(this,"Kendaraan anda berada diluar batas toleransi segera cek kendaraan anda !!"+"speed"+" "+speed,Toast.LENGTH_LONG).show();

        }




    }


    private static Double toRad(Double value) {

        return value * Math.PI / 180;

    }




    public void koordinat_1()
    {

        final String URL_SATU = "http://bambangm.com/koordinat_satu.php";

        JsonArrayRequest reqData = new JsonArrayRequest(URL_SATU,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("volley","response : " + response.toString());
                        for(int i = 0 ; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);

                                String a = data.getString("latitude");
                                String b = data.getString("longitude");

                                Klat_satu = a ;
                                Klong_satu = b ;

                                koordinat_2();





                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("volley", "error : " + error.getMessage());
                    }
                }){

            @Override
            protected Map<String, String> getParams(){


                final String nama_krj = "bambang" ;

                Map<String,String> params = new HashMap<String, String>();
                //mengirim value melalui parameter ke database
                params.put("nama_krj", nama_krj);
                return params;
            }


        };
        AppController.getInstance().addToRequestQueue(reqData,tag_json_obj);
    }




    public void koordinat_2()
    {

        final String URL_SATU = "http://bambangm.com/koordinat_kedua.php";

        JsonArrayRequest reqData = new JsonArrayRequest(URL_SATU,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("volley","response : " + response.toString());
                        for(int i = 0 ; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);

                                String a = data.getString("latitude");
                                String b = data.getString("longitude");
                                String s = data.getString("speed");
                                double speed = Double.parseDouble(s) ;



                                Klat_dua= a ;
                                Klong_dua = b ;








                                double lat1 = Double.parseDouble(Klat_satu) ;
                                double long1 = Double.parseDouble(Klong_satu);

                                double lat2 = Double.parseDouble(Klat_dua) ;
                                double long2 = Double.parseDouble(Klong_dua);


                                getJarak(lat1,long1,lat2,long2,speed);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("volley", "error : " + error.getMessage());
                    }
                }){

            @Override
            protected Map<String, String> getParams(){


                final String nama_krj = "bambang" ;

                Map<String,String> params = new HashMap<String, String>();
                //mengirim value melalui parameter ke database
                params.put("nama_krj", nama_krj);
                return params;
            }


        };
        AppController.getInstance().addToRequestQueue(reqData,tag_json_obj);
    }

















}