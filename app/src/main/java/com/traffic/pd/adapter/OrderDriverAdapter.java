package com.traffic.pd.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.CarDetailActivity;
import com.traffic.pd.activity.OrderDetailActivity;
import com.traffic.pd.data.CarInfo;
import com.traffic.pd.utils.ComUtils;

import java.util.List;

public class OrderDriverAdapter extends RecyclerView.Adapter<OrderDriverAdapter.DriversHolder> {

    List<CarInfo> carInfoList;
    Activity mContext;
    String type;
    SelectCar selectCar;
    public OrderDriverAdapter(Activity mContext, List<CarInfo> carInfoList, String type, SelectCar selectCar){

        this.carInfoList = carInfoList;
        this.mContext = mContext;
        this.type = type;
        this.selectCar = selectCar;
    }
    @NonNull
    @Override
    public OrderDriverAdapter.DriversHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_driver_item, viewGroup, false);
        return new OrderDriverAdapter.DriversHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDriverAdapter.DriversHolder driversHolder, final int i) {
        final CarInfo carInfo = carInfoList.get(i);
        driversHolder.tv_license.setText("License: "+carInfo.getCar_num());
        driversHolder.tv_driver_name.setText("Driver: "+carInfo.getDriver());
        driversHolder.iv_select.setSelected(true);
        driversHolder.tv_phone.setText("Call: " + carInfo.getMobile());

        if(type.equals("2") && MainActivity.userBean.getIdentity().equals("1")){
            driversHolder.iv_select.setVisibility(View.VISIBLE);
        }else{
            driversHolder.iv_select.setVisibility(View.GONE);
        }

        driversHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CarDetailActivity.class);
                intent.putExtra("info",carInfoList.get(i));
                mContext.startActivity(intent);
            }
        });

        driversHolder.rl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(carInfo.isSelect()){
                    carInfo.setSelect(false);
                    driversHolder.iv_select.setSelected(false);
                }else{
                    carInfo.setSelect(true);
                    driversHolder.iv_select.setSelected(true);
                }
                selectCar.selectCar();
            }
        });

        driversHolder.tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ComUtils.showCallDialog(mContext,carInfo.getMobile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return carInfoList.size();
    }

    class DriversHolder extends RecyclerView.ViewHolder{
        TextView tv_driver_name,tv_license,tv_phone;
        ImageView iv_next,iv_select;
        RelativeLayout rl_select,rl_detail;
        public DriversHolder(@NonNull View itemView) {
            super(itemView);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_license = itemView.findViewById(R.id.tv_license);
            tv_driver_name = itemView.findViewById(R.id.tv_driver_name);
            iv_next =  itemView.findViewById(R.id.iv_next);
            iv_select = itemView.findViewById(R.id.iv_select);
            rl_select = itemView.findViewById(R.id.rl_select);
            rl_detail = itemView.findViewById(R.id.rl_detail);
        }
    }

    public interface SelectCar{
        void selectCar();
    }
}