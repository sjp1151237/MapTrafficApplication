package com.traffic.pd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.OrderDetailActivity;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.utils.ComUtils;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.HallViewHolder> {

    List<OrderBean> orderBeans;
    Context mContext;
    String fromWhere;
    public OrderListAdapter(Context mContext, List<OrderBean> orderBeans,String fromWhere){
        this.mContext = mContext;
        this.orderBeans = orderBeans;
        this.fromWhere = fromWhere;
    }

    @NonNull
    @Override
    public HallViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hall_item, viewGroup, false);
        return new HallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HallViewHolder hallViewHolder, final int i) {

        hallViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.userBean.getIdentity().equals("2")){
                    if(null != MainActivity.carInfo && MainActivity.carInfo.getStatus().equals("2")){
                        OrderBean orderBean = orderBeans.get(i);
                        Intent intent = new Intent(mContext, OrderDetailActivity.class);
                        intent.putExtra("from",fromWhere);
                        intent.putExtra("info",orderBean);
                        mContext.startActivity(intent);
                    }else{
                        ComUtils.showMsg(mContext,"审核通过才能接单");
                    }
                }
                if(MainActivity.userBean.getIdentity().equals("3")){
                    if(null != MainActivity.companyInfo && MainActivity.companyInfo.getStatus().equals("2")){
                        OrderBean orderBean = orderBeans.get(i);
                        Intent intent = new Intent(mContext, OrderDetailActivity.class);
                        intent.putExtra("from",fromWhere);
                        intent.putExtra("info",orderBean);
                        mContext.startActivity(intent);
                    }else{
                        ComUtils.showMsg(mContext,"审核通过才能接单");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderBeans.size();
    }

    class HallViewHolder extends RecyclerView.ViewHolder{

        public HallViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
