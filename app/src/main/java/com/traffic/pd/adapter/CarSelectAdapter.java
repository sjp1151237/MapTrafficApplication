package com.traffic.pd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.traffic.pd.R;
import com.traffic.pd.data.CarType;

import java.util.List;

public class CarSelectAdapter extends RecyclerView.Adapter<CarSelectAdapter.CarSelectHolder> {

    List<CarType> carTypeList;
    Context mContext;

    public CarSelectAdapter(List<CarType> carTypeList, Context mContext) {

        this.carTypeList = carTypeList;
        this.mContext = mContext;
    }

    @Override
    public CarSelectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.car_select_item, parent, false);
        return new CarSelectHolder(view);
    }

    @Override
    public void onBindViewHolder(CarSelectHolder holder, int position) {
        final CarType carType = carTypeList.get(position);
        holder.tv_weight.setText(carType.getWeight());
        holder.tv_volume.setText(carType.getVolume());
        holder.tv_capacity.setText(carType.getCapacity());
        holder.tv_car_num.setText(String.valueOf(carType.getNum()));

        holder.rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = carType.getNum();
                num++;
                carType.setNum(num);
                notifyDataSetChanged();
            }
        });
        holder.rl_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = carType.getNum();
                num--;
                if(num < 0){
                    num = 0;
                }
                carType.setNum(num);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return carTypeList.size();
    }

    class CarSelectHolder extends RecyclerView.ViewHolder {
        TextView tv_weight;
        TextView tv_volume;
        TextView tv_capacity, tv_car_num;
        RelativeLayout rl_sub, rl_add;
        SimpleDraweeView type_picture;
        public CarSelectHolder(View itemView) {
            super(itemView);
            tv_weight = itemView.findViewById(R.id.tv_weight);
            tv_volume = itemView.findViewById(R.id.tv_volume);
            tv_capacity = itemView.findViewById(R.id.tv_capacity);
            tv_car_num = itemView.findViewById(R.id.tv_car_num);
            rl_sub = itemView.findViewById(R.id.rl_sub);
            rl_add = itemView.findViewById(R.id.rl_add);
            type_picture = itemView.findViewById(R.id.type_picture);
        }
    }
}
