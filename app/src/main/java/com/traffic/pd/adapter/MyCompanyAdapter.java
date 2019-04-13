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
import com.traffic.pd.activity.CompanyDetailActivity;
import com.traffic.pd.data.CompanyInfo;

import java.util.List;

public class MyCompanyAdapter extends RecyclerView.Adapter<MyCompanyAdapter.CompanyHolder> {

    List<CompanyInfo> companyInfoList;
    Context context;

    public MyCompanyAdapter(Context context,List<CompanyInfo> companyInfoList){
        this.companyInfoList = companyInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public CompanyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.company_item, viewGroup, false);
        return new CompanyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyHolder companyHolder, int i) {
        final CompanyInfo companyInfo = companyInfoList.get(i);
        companyHolder.tv_company_name.setText("name: "+companyInfo.getName());
        companyHolder.tv_license.setText("License: "+companyInfo.getLicense_num());
        companyHolder.tv_location.setText("location: "+companyInfo.getCountry()  + "   "+ companyInfo.getProvince()+ "   " + companyInfo.getCity()+ "   " + companyInfo.getDistrict()+ "   " + companyInfo.getAddress());

        if(companyInfo.getStatus().equals("2")){
            companyHolder.tv_status.setText("已同意");
        }else{
            companyHolder.tv_status.setText("未同意");
        }

        companyHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CompanyDetailActivity.class);
                intent.putExtra("info",companyInfo);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return companyInfoList.size();
    }

    class CompanyHolder extends RecyclerView.ViewHolder{
        TextView tv_company_name,tv_license,tv_location,tv_status;
        public CompanyHolder(@NonNull View itemView) {
            super(itemView);
            tv_company_name = itemView.findViewById(R.id.tv_company_name);
            tv_license = itemView.findViewById(R.id.tv_license);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
