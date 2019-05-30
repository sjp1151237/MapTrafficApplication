package com.traffic.pd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.UserBean;
import com.traffic.pd.ui.CircleCountDownView;
import com.traffic.pd.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {


    Context mContext;

    private int progress;
    private final int END_TIME = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toMain();
                }
            },3000);
    }

    private void toMain() {
        if(TextUtils.isEmpty(PreferencesUtils.getSharePreStr(this,Constant.USER_INFO))){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else{
            UserBean userBean = com.alibaba.fastjson.JSONObject.parseObject(PreferencesUtils.getSharePreStr(this,Constant.USER_INFO),UserBean.class);
//            if(userBean.getIdentity().equals("1")){
//                Intent intent = new Intent(this,ConsigerHomeActivity.class);
//                startActivity(intent);
//                finish();
//            }else{
//                Intent intent = new Intent(this,MainActivity.class);
//                intent.putExtra("user",userBean);
//                startActivity(intent);
//                finish();
//            }
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("user",userBean);
            startActivity(intent);
            finish();
        }
    }

}
