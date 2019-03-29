package com.traffic.pd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.traffic.pd.adapter.MainFreagmentAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PreferencesUtils;
import com.traffic.pd.weigets.NoScrollViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    String tag;
    @BindView(R.id.vp_main)
    NoScrollViewPager vpMain;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.rl_home)
    RelativeLayout rlHome;
    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.rl_mine)
    RelativeLayout rlMine;


    public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 555;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        ButterKnife.bind(this);
        tag = getIntent().getStringExtra("tag");
        if (TextUtils.isEmpty(tag)) {
            if (!TextUtils.isEmpty(PreferencesUtils.getSharePreStr(this, Constant.Identity_Name))) {
                tag = PreferencesUtils.getSharePreStr(this, Constant.Identity_Name);
            }
        }
        vpMain.setAdapter(new MainFreagmentAdapter(getSupportFragmentManager(),tag));
        vpMain.setScroll(false);


        ComUtils.getLocationPermission(this);

    }

    @OnClick({R.id.rl_home, R.id.rl_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                vpMain.setCurrentItem(0);
                ivHome.setImageDrawable(getResources().getDrawable(R.mipmap.home_select));
                ivMine.setImageDrawable(getResources().getDrawable(R.mipmap.mine_no));
                break;
            case R.id.rl_mine:
                vpMain.setCurrentItem(1);
                ivHome.setImageDrawable(getResources().getDrawable(R.mipmap.home_no));
                ivMine.setImageDrawable(getResources().getDrawable(R.mipmap.main_select));
                break;
        }
    }
}
