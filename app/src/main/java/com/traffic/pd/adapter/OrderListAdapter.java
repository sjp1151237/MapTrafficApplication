package com.traffic.pd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.OrderDetailActivity;
import com.traffic.pd.activity.OrderDriversActivity;
import com.traffic.pd.activity.OrderWebActivity;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.utils.ComUtils;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.HallViewHolder> {

    List<OrderBean> orderBeans;
    Context mContext;
    String fromWhere;

    public OrderListAdapter(Context mContext, List<OrderBean> orderBeans, String fromWhere) {
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
        final OrderBean orderBean = orderBeans.get(i);

        hallViewHolder.tv_begin.setText("开始地址："+orderBean.getCountry() + "  "+orderBean.getProvince()+ "  "+ orderBean.getCity());
        hallViewHolder.tv_over.setText("目的地址："+orderBean.getRecive_country() + "  "+orderBean.getRecive_province()+ "  "+ orderBean.getRecive_city());

        hallViewHolder.tv_state.setText(stateString(orderBean.getStatus()));
        hallViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.userBean.getIdentity().equals("1")) {
                    if (orderBean.getStatus().equals(4)) {

                        Intent intent = new Intent(mContext, OrderWebActivity.class);
                        intent.putExtra("id", orderBean.getId());
                        intent.putExtra("status", orderBean.getStatus());
                        mContext.startActivity(intent);

                    } else {
                        OrderBean orderBean = orderBeans.get(i);
                        Intent intent = new Intent(mContext, OrderDetailActivity.class);
                        intent.putExtra("from", fromWhere);
                        intent.putExtra("info", orderBean);
                        mContext.startActivity(intent);
                    }
                }
                if (MainActivity.userBean.getIdentity().equals("2")) {
                    if (null != MainActivity.carInfo && MainActivity.carInfo.getStatus().equals("2")) {
                        if (orderBean.getStatus().equals(4)) {

                            Intent intent = new Intent(mContext, OrderWebActivity.class);
                            intent.putExtra("id", orderBean.getId());
                            intent.putExtra("status", orderBean.getStatus());
                            mContext.startActivity(intent);

                        } else {
                            OrderBean orderBean = orderBeans.get(i);
                            Intent intent = new Intent(mContext, OrderDetailActivity.class);
                            intent.putExtra("from", fromWhere);
                            intent.putExtra("info", orderBean);
                            mContext.startActivity(intent);
                        }
                    } else {
                        ComUtils.showMsg(mContext, "审核通过才能接单");
                    }
                }
                if (MainActivity.userBean.getIdentity().equals("3")) {
                    if (null != MainActivity.companyInfo && MainActivity.companyInfo.getStatus().equals("2")) {
                        if (orderBean.getStatus().equals(4)) {

                            Intent intent = new Intent(mContext, OrderWebActivity.class);
                            intent.putExtra("id", orderBean.getId());
                            intent.putExtra("status", orderBean.getStatus());
                            mContext.startActivity(intent);

                        }else{
                            OrderBean orderBean = orderBeans.get(i);
                            Intent intent = new Intent(mContext, OrderDetailActivity.class);
                            intent.putExtra("from", fromWhere);
                            intent.putExtra("info", orderBean);
                            mContext.startActivity(intent);
                        }

                    } else {
                        ComUtils.showMsg(mContext, "审核通过才能接单");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderBeans.size();
    }


    class HallViewHolder extends RecyclerView.ViewHolder {
        TextView tv_begin,tv_state;
        TextView tv_over;
        public HallViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_begin = itemView.findViewById(R.id.tv_begin);
            tv_over = itemView.findViewById(R.id.tv_over);
            tv_state = itemView.findViewById(R.id.tv_state);

        }
    }

    private String stateString(String state){
        if(state.equals("1")){
            return mContext.getString(R.string.state_verify);
        }
        if(state.equals("2")){
            return mContext.getString(R.string.state_publishing);
        }
        if(state.equals("3")){
            return mContext.getString(R.string.state_cancel);
        }
        if(state.equals("4")){
            return mContext.getString(R.string.state_ing);
        }
        if(state.equals("5")){
            return mContext.getString(R.string.state_over);
        }
        return "";
    }
}
