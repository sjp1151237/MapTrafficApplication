package com.traffic.pd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.activity.CarDetailActivity;
import com.traffic.pd.data.CarInfo;

import java.util.List;

public class DriversAdapter extends RecyclerView.Adapter<DriversAdapter.DriversHolder> {

    List<CarInfo> carInfoList;
    Context mContext;
    public DriversAdapter(Context mContext,List<CarInfo> carInfoList){

        this.carInfoList = carInfoList;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public DriversHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.company_item, viewGroup, false);
        return new DriversHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriversHolder driversHolder, final int i) {
        CarInfo carInfo = carInfoList.get(i);
        driversHolder.tv_company_name.setText("mobile: "+carInfo.getMobile());
        driversHolder.tv_license.setText("License: "+carInfo.getCar_num());
        driversHolder.tv_location.setText("location: "+carInfo.getCountry()  + "   "+ carInfo.getProvince()+ "   " + carInfo.getCity()+ "   " + carInfo.getDistrict()+ "   " + carInfo.getAddress());

        if(carInfo.getStatus().equals("2")){
            driversHolder.tv_status.setText("已同意");
        }else{
            driversHolder.tv_status.setText("未同意");
        }

        driversHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CarDetailActivity.class);
                intent.putExtra("info",carInfoList.get(i));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return carInfoList.size();
    }

    class DriversHolder extends RecyclerView.ViewHolder{
        TextView tv_company_name,tv_license,tv_location,tv_status;
        public DriversHolder(@NonNull View itemView) {
            super(itemView);
            tv_company_name = itemView.findViewById(R.id.tv_company_name);
            tv_license = itemView.findViewById(R.id.tv_license);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
