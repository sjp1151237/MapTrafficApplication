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
import com.traffic.pd.fragments.DriversRegisterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpDetailActivity extends AppCompatActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.fl_regist)
    FrameLayout flRegist;
    DriversRegisterFragment driversRegisterFragment;
    String tag;
    private FragmentManager supportFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_detail);
        ButterKnife.bind(this);
        tag = getIntent().getStringExtra("tag");

        driversRegisterFragment = DriversRegisterFragment.newInstance(tag,"");
        //获取管理者
        supportFragmentManager = getSupportFragmentManager();
        // 事物
        FragmentTransaction ft = supportFragmentManager.beginTransaction();
        ft.add(R.id.fl_regist, driversRegisterFragment).commit();

    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        driversRegisterFragment.onActivityResult(requestCode,resultCode,data);
    }
}
