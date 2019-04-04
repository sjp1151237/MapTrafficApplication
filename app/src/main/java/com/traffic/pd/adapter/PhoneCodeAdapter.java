package com.traffic.pd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.data.PhoneCodeBean;

import java.util.List;

public class PhoneCodeAdapter extends RecyclerView.Adapter<PhoneCodeAdapter.PhoneCodeViewHolder> {

    List<PhoneCodeBean> beanList;
    Context mContext;
    SelectCode selectCode;

    public PhoneCodeAdapter(List<PhoneCodeBean> beanList, Context mContext, SelectCode selectCode) {

        this.beanList = beanList;
        this.mContext = mContext;
        this.selectCode = selectCode;
    }

    @NonNull
    @Override
    public PhoneCodeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.phone_code_item, viewGroup, false);
        return new PhoneCodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneCodeViewHolder phoneCodeViewHolder, final int i) {
        PhoneCodeBean phoneCodeBean = beanList.get(i);
        if (null != phoneCodeBean.getC()) {
            phoneCodeViewHolder.tv_abbreviation.setText(phoneCodeBean.getC());
        }
        if (null != phoneCodeBean.getD()) {
            phoneCodeViewHolder.tv_code.setText(phoneCodeBean.getD());
        }
        if (null != phoneCodeBean.getA()) {
            phoneCodeViewHolder.tv_name.setText(phoneCodeBean.getA());
        }

        phoneCodeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCode.selectCode(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    class PhoneCodeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_abbreviation, tv_code;

        public PhoneCodeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_abbreviation = itemView.findViewById(R.id.tv_abbreviation);
            tv_code = itemView.findViewById(R.id.tv_code);
        }
    }

    public interface SelectCode {
        void selectCode(int i);
    }
}
