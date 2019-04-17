package com.traffic.pd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.activity.CarDetailActivity;
import com.traffic.pd.data.CarInfo;
import com.traffic.pd.utils.ComUtils;

import java.util.List;

public class DriversAdapter extends RecyclerView.Adapter<DriversAdapter.DriversHolder> {

    List<CarInfo> carInfoList;
    Context mContext;
    String type;
    public DriversAdapter(Context mContext,List<CarInfo> carInfoList,String type){

        this.carInfoList = carInfoList;
        this.mContext = mContext;
        this.type = type;
    }
    @NonNull
    @Override
    public DriversHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.company_item, viewGroup, false);
        return new DriversHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DriversHolder driversHolder, final int i) {
        final CarInfo carInfo = carInfoList.get(i);
        driversHolder.tv_company_name.setText("mobile: "+carInfo.getMobile());
        driversHolder.tv_license.setText("License: "+carInfo.getCar_num());
        driversHolder.tv_location.setText("location: "+carInfo.getCountry()  + "   "+ carInfo.getProvince()+ "   " + carInfo.getCity()+ "   " + carInfo.getDistrict()+ "   " + carInfo.getAddress());

        if(carInfo.getStatus().equals("2")){
            driversHolder.tv_status.setText("已同意");
        }else{
            driversHolder.tv_status.setText("未同意");
        }

        if(TextUtils.isEmpty(type)){
            driversHolder.iv_next.setVisibility(View.VISIBLE);
            driversHolder.iv_select.setVisibility(View.GONE);
        }else{
            driversHolder.iv_next.setVisibility(View.GONE);
            driversHolder.iv_select.setVisibility(View.VISIBLE);
            driversHolder.iv_select.setSelected(carInfo.isSelect());
        }

        driversHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(type)){
                    Intent intent = new Intent(mContext, CarDetailActivity.class);
                    intent.putExtra("info",carInfoList.get(i));
                    mContext.startActivity(intent);
                }else{
                    if(carInfo.getStatus().equals("2")){
                        if(carInfo.isSelect()){
                            carInfo.setSelect(false);
                            driversHolder.iv_select.setSelected(false);
                        }else{
                            carInfo.setSelect(true);
                            driversHolder.iv_select.setSelected(true);
                        }
                    }else{
                        ComUtils.showMsg(mContext,"司机还未同意不能选择");
                    }

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return carInfoList.size();
    }

    class DriversHolder extends RecyclerView.ViewHolder{
        TextView tv_company_name,tv_license,tv_location,tv_status;
        ImageView iv_next,iv_select;
        public DriversHolder(@NonNull View itemView) {
            super(itemView);
            tv_company_name = itemView.findViewById(R.id.tv_company_name);
            tv_license = itemView.findViewById(R.id.tv_license);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_status = itemView.findViewById(R.id.tv_status);
            iv_next =  itemView.findViewById(R.id.iv_next);
            iv_select = itemView.findViewById(R.id.iv_select);
        }
    }
}
