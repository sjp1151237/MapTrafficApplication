package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.fragments.ConsigerRegistFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);

        //获取管理者
        supportFragmentManager = getSupportFragmentManager();
        // 事物
        FragmentTransaction ft = supportFragmentManager.beginTransaction();
        mType = getIntent().getStringExtra("tag");
        if(mType.equals(Constant.Val_Consigner)){
            ft.add(R.id.fl_regist, new ConsigerRegistFragment()).commit();
        }
        if(mType.equals(Constant.Val_Drivers)){
            ft.add(R.id.fl_regist, new ConsigerRegistFragment()).commit();
        }
        if(mType.equals(Constant.Val_Company)){
            ft.add(R.id.fl_regist, new ConsigerRegistFragment()).commit();
        }


    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }
}
