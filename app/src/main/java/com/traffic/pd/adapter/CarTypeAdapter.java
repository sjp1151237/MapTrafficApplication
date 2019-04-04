package com.traffic.pd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.traffic.pd.R;

public class CarTypeAdapter extends RecyclerView.Adapter<CarTypeAdapter.CarViewHolder> {

    Context mContext;

    public CarTypeAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.car_type_item, viewGroup, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder carViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class CarViewHolder extends RecyclerView.ViewHolder {

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
