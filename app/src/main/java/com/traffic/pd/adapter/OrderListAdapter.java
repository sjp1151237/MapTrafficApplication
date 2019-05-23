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
import com.traffic.pd.activity.OrderDetailActivity;
import com.traffic.pd.data.OrderBean;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.HallViewHolder> {

    List<OrderBean> orderBeans;
    Context mContext;
    String fromWhere;
    String orderType;
    ClickOrder clickOrder;

    public OrderListAdapter(Context mContext, List<OrderBean> orderBeans, String fromWhere) {
        this.mContext = mContext;
        this.orderBeans = orderBeans;
        this.fromWhere = fromWhere;
    }

    public OrderListAdapter(Context mContext, List<OrderBean> orderBeans, String fromWhere,String orderType) {
        this.mContext = mContext;
        this.orderBeans = orderBeans;
        this.fromWhere = fromWhere;
        this.orderType = orderType;
    }

    public OrderListAdapter(Context mContext, List<OrderBean> orderBeans, String fromWhere,String orderType,ClickOrder clickOrder) {
        this.mContext = mContext;
        this.orderBeans = orderBeans;
        this.fromWhere = fromWhere;
        this.orderType = orderType;
        this.clickOrder = clickOrder;
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

        hallViewHolder.tv_time.setText(orderBean.getStart_time());
        hallViewHolder.tv_state.setText(stateString(orderBean.getStatus()));
        hallViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromWhere.equals("CurrentCarsFragment")){
                    if(null != clickOrder){
                        clickOrder.clickOrder(i);
                    }
                }else{
                    OrderBean orderBean = orderBeans.get(i);
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("from", fromWhere);
                    intent.putExtra("info", orderBean);
                    mContext.startActivity(intent);
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
        TextView tv_over,tv_time;
        public HallViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_begin = itemView.findViewById(R.id.tv_begin);
            tv_over = itemView.findViewById(R.id.tv_over);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_time = itemView.findViewById(R.id.tv_time);

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

    public interface ClickOrder{
        void clickOrder(int pos);
    }
}
