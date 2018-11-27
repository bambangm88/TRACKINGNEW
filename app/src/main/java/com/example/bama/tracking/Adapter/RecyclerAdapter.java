package com.example.bama.tracking.Adapter;

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

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.HolderData> {
    private List<ModelData> mItems;
    private Context context;

    public RecyclerAdapter (Context context, List<ModelData> items) {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_data_rc, parent, false);
        HolderData holderData = new HolderData(layout);

        return holderData;
    }

    @Override
    public void onBindViewHolder(final HolderData holder, int position) {

        final ModelData md = mItems.get(position);
        holder.tvTanggal.setText(md.getTanggal());
        holder.tvId.setText(md.getId());
        holder.md = md;
        }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvId ;
        ModelData md;

        public HolderData(View view) {
            super(view);

            tvTanggal = (TextView) view.findViewById(R.id.tvTanggal);






        }




    }
}