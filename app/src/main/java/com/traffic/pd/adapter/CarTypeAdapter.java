package com.traffic.pd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.traffic.pd.R;
import com.traffic.pd.data.CarType;

import java.util.List;

public class CarTypeAdapter extends RecyclerView.Adapter<CarTypeAdapter.CarViewHolder> {

    Context mContext;
    List<CarType> carTypeList;
    CarTypeSelect carTypeSelect;

    public CarTypeAdapter(Context mContext, List<CarType> carTypeList,CarTypeSelect carTypeSelect) {
        this.mContext = mContext;
        this.carTypeList = carTypeList;
        this.carTypeSelect = carTypeSelect;

    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.car_type_item, viewGroup, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder carViewHolder, int i) {

        final CarType carType = carTypeList.get(i);
        carViewHolder.tv_capacity.setText(carType.getCapacity());
        carViewHolder.tv_volume.setText(carType.getVolume());
        carViewHolder.tv_weight.setText(carType.getWeight());

        carViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carTypeSelect.carTypeSelect(carType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carTypeList.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView type_picture;
        TextView tv_weight,tv_volume,tv_capacity;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            type_picture = itemView.findViewById(R.id.type_picture);
            tv_weight = itemView.findViewById(R.id.tv_weight);
            tv_volume = itemView.findViewById(R.id.tv_volume);
            tv_capacity = itemView.findViewById(R.id.tv_capacity);
        }
    }

    public interface CarTypeSelect{
        void carTypeSelect(CarType id);
    }
}
