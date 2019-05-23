package com.traffic.pd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.data.OrderBean;

import java.util.List;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.MyHolder> {
    List<OrderBean> orderBeans;
    Context mContext;
    ClickOrder clickOrder;

    public MyOrderListAdapter(Context mContext, List<OrderBean> orderBeans, ClickOrder clickOrder){
        this.mContext = mContext;
        this.orderBeans = orderBeans;
        this.clickOrder = clickOrder;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_order_item, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        final OrderBean orderBean = orderBeans.get(i);
        myHolder.tv_des.setText("订单号：" + orderBean.getId() + "   "+"目的地：" + orderBean.getRecive_city());
        myHolder.tv_pos.setText(String.valueOf(i+1));

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOrder.clickOrder(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderBeans.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView tv_pos,tv_des;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_pos = itemView.findViewById(R.id.tv_pos);
            tv_des = itemView.findViewById(R.id.tv_des);

        }
    }

    public interface ClickOrder{
        void clickOrder(int pos);
    }
}
