package com.traffic.pd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.fragments.ConsigerRegistFragment;
import com.traffic.pd.fragments.DriversRegisterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    String mType;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_regist)
    FrameLayout flRegist;

    private FragmentManager supportFragmentManager;

    ConsigerRegistFragment consigerRegistFragment;
    DriversRegisterFragment driversRegisterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);
        tvTitle.setText("Sign up");
        consigerRegistFragment = new ConsigerRegistFragment();

        //获取管理者
        supportFragmentManager = getSupportFragmentManager();
        // 事物
        FragmentTransaction ft = supportFragmentManager.beginTransaction();
        ft.add(R.id.fl_regist, consigerRegistFragment).commit();
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != consigerRegistFragment){
            consigerRegistFragment.onActivityResult(requestCode,resultCode,data);
        }
        if(null != driversRegisterFragment){
            driversRegisterFragment.onActivityResult(requestCode,resultCode,data);
        }

    }
}
