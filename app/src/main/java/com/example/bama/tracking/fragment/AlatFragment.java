package com.example.bama.tracking.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bama.tracking.Adapter.ModelData;
import com.example.bama.tracking.R;
import com.example.bama.tracking.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AlatFragment extends Fragment  {

    Button btn_on, btn_off;

    ProgressDialog pd;

    int success;

    TextView status;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";
    private static final String TAG = AlatFragment.class.getSimpleName();

    public AlatFragment() {
        // Required empty public constructor
    }

       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           View view = inflater.inflate(R.layout.activity_alat,container, false);


           btn_on = view.findViewById(R.id.tmbl_on);
           btn_off = view.findViewById(R.id.tmbl_off);

            status = view.findViewById(R.id.status);

           Toolbar ToolBarAtas2 = view.findViewById(R.id.toolbar_satu);
           ((AppCompatActivity)getActivity()).setSupportActionBar(ToolBarAtas2);

           pd = new ProgressDialog(getActivity());

           btn_on.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   showAlertDialog_On() ;

               }
           });

           btn_off.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   showAlertDialog_Off() ;
               }
           });

           loadJson();

           return view;

       }


    //perintah delete ------------
private void showAlertDialog_On() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Nyalakan Listrik ?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        String id = "2" ;
                        String Color = "blue" ;
                        String State = "1" ;
                        String loading = "Menyalakan Listrik" ;

                        simpan_update(id,Color,State,loading);


                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//----------------



    //perintah delete ------------
    private void showAlertDialog_Off() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Matikan Listrik ?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        String id = "2" ;
                        String Color = "blue" ;
                        String State = "00" ;
                        String loading = "Mematikan Listrik" ;

                        simpan_update(id,Color,State,loading);



                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//----------------






    // fungsi untuk menyimpan atau update
    public void simpan_update(final String id, final String Color, final String State , final String loading) {

        pd.setMessage(loading);
        pd.setCancelable(false);
        pd.show();

        String URL_DATA = "http://bambangm.com/update_relay.php";

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pd.cancel();
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),  jObj.getString(TAG_MESSAGE),
                                Snackbar.LENGTH_SHORT);

                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.hijau));

                        snackbar.show();
                        loadJson();

                    } else {
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),  jObj.getString(TAG_MESSAGE), Snackbar.LENGTH_SHORT);

                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gagal));
                        snackbar.show();
                  }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.e(TAG, "Error: " + error.getMessage());

                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),  error.getMessage(), Snackbar.LENGTH_SHORT);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gagal));
                snackbar.show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update

                    params.put("ID", id);
                    params.put("Color", Color);
                    params.put("State", State);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }










    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.petunjuk) {
            return true;
        }

        if (id == R.id.keluar) {
            showAlertDialog(); //keluar
        }


        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



    //perintah keluar ------------
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
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
                        getActivity().finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//----------------








    private void loadJson() {

        pd.setMessage("Cek Kelistrikan . .");
        pd.setCancelable(false);
        pd.show();

        // String URL_DATA = "https://mybam.000webhostapp.com/get_data.php";
        String URL_DATA = "http://bambangm.com/get_on_off.php";

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST,URL_DATA, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.cancel();
                        Log.d("volley", "response : " + response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);

                                String stat = data.getString("State");


                                if (stat.equals("1")){
                                    btn_off.setVisibility(View.VISIBLE);
                                    btn_on.setVisibility(View.GONE);
                                    status.setText("Terhubung");
                                } else {
                                    btn_on.setVisibility(View.VISIBLE);
                                    btn_off.setVisibility(View.GONE);
                                    status.setText("Tidak Terhubung");
                                }

                            } catch (JSONException e) {
                                pd.cancel();
                                e.printStackTrace();
                            }
                        }

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













}
