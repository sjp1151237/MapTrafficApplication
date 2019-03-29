package com.traffic.pd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.traffic.pd.R;

public class HallListAdapter extends RecyclerView.Adapter<HallListAdapter.HallViewHolder> {

    Context mContext;
    public HallListAdapter(Context mContext){
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HallViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hall_item, viewGroup, false);
        return new HallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HallViewHolder hallViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class HallViewHolder extends RecyclerView.ViewHolder{

        public HallViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
