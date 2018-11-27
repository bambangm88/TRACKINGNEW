package com.example.bama.tracking.Adapter;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bama.tracking.R;
import com.example.bama.tracking.ViewDataRC;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Data extends RecyclerView.Adapter<Adapter_Data.HolderData> {
    private List<ModelData> mItems;
    private Context context;

    public Adapter_Data(Context context, List<ModelData> items) {
        this.mItems = items;
        this.context = context;
    }





    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row, parent, false);
        HolderData holderData = new HolderData(layout);
          return holderData;
    }

    @Override
    public void onBindViewHolder(final HolderData holder, int position) {

        final ModelData md = mItems.get(position);
        holder.tvLat.setText(md.getTanggal());
        holder.tvLong.setText(md.getWaktu());

        holder.md = md;

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewDataRC.class);
                intent.putExtra("waktu", md.getWaktu()) ;
                intent.putExtra("tanggal", md.getTanggal());
                intent.putExtra("latitude", md.getLatitude());
                intent.putExtra("longitude", md.getLongitude()) ;
                intent.putExtra("speed", md.getSpeed());
                intent.putExtra("feet", md.getFeet());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder {
        TextView tvLat, tvLong ;
           ModelData md;
        CardView cardView ;

        public HolderData(View view) {
            super(view);

            tvLat = (TextView) view.findViewById(R.id.Latitude);
            tvLong= (TextView) view.findViewById(R.id.Longitude);
            cardView= (CardView) view.findViewById(R.id.cvKronologi);

        }




    }
}