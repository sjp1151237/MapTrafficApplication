package com.traffic.pd.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.fragments.OrderHallFragment;

public class RegisterActivity extends AppCompatActivity {

    String mType;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getStringExtra("tag");
        if(mType.equals(Constant.Val_Consigner)){
            view = LayoutInflater.from(this).inflate(R.layout.activity_register_consigner,null);
            view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        if(mType.equals(Constant.Val_Drivers)){
            view = LayoutInflater.from(this).inflate(R.layout.activity_register_driver,null);
            view.findViewById(R.id.ll_cartype).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegisterActivity.this,CarTypeSelectActivity.class));
                }
            });
            view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        if(mType.equals(Constant.Val_Company)){
            view = LayoutInflater.from(this).inflate(R.layout.activity_register_company,null);
            view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        setContentView(view);

    }
}
