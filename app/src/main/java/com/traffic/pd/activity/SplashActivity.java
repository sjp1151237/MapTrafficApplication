package com.traffic.pd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.UserBean;
import com.traffic.pd.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {


    @BindView(R.id.ll_Consigner)
    LinearLayout llConsigner;
    @BindView(R.id.ll_Drivers)
    LinearLayout llDrivers;
    @BindView(R.id.ll_Company)
    LinearLayout llCompany;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mContext = this;

        if(!TextUtils.isEmpty(PreferencesUtils.getSharePreStr(this,Constant.USER_INFO))){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    UserBean userBean = JSONObject.parseObject(PreferencesUtils.getSharePreStr(SplashActivity.this,Constant.USER_INFO),UserBean.class);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("user",userBean);
                    startActivity(intent);
                }
            },3000);


        }
    }

    @OnClick({R.id.ll_Consigner, R.id.ll_Drivers, R.id.ll_Company})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_Consigner:
                PreferencesUtils.putSharePre(mContext, Constant.Identity_Name,Constant.Val_Consigner);
                Intent intent = new Intent(mContext,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
