package com.traffic.pd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
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

    }

    @OnClick({R.id.ll_Consigner, R.id.ll_Drivers, R.id.ll_Company})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_Consigner:
                PreferencesUtils.putSharePre(mContext, Constant.Identity_Name,Constant.Val_Consigner);
                Intent intent = new Intent(mContext,MainActivity.class);
                intent.putExtra("tag",Constant.Val_Consigner);
                startActivity(intent);
                break;
            case R.id.ll_Drivers:
                PreferencesUtils.putSharePre(mContext, Constant.Identity_Name,Constant.Val_Drivers);
                Intent intentd = new Intent(mContext,MainActivity.class);
                intentd.putExtra("tag",Constant.Val_Drivers);
                startActivity(intentd);
                break;
            case R.id.ll_Company:
                PreferencesUtils.putSharePre(mContext, Constant.Identity_Name,Constant.Val_Company);
                Intent intentf = new Intent(mContext,MainActivity.class);
                intentf.putExtra("tag",Constant.Val_Company);
                startActivity(intentf);
                break;
        }
    }
}
