package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.adapter.OrderPagerAdapter;
import com.traffic.pd.fragments.HomeFragment;
import com.traffic.pd.fragments.MyOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrderActivity extends AppCompatActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.tl_main)
    TabLayout tlMain;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;

    List<Fragment> fragments;
    MyOrderFragment myOrderFragmentAll,myOrderFragmentPub,myOrderFragmentIng,myOrderFragmentOver;
    OrderPagerAdapter orderPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_activity);
        ButterKnife.bind(this);
        fragments = new ArrayList<>();
        myOrderFragmentAll  = MyOrderFragment.newInstance("0","");
        myOrderFragmentPub = MyOrderFragment.newInstance("2","");
        myOrderFragmentIng = MyOrderFragment.newInstance("4","");
        myOrderFragmentOver = MyOrderFragment.newInstance("5","");
        fragments.add(myOrderFragmentAll);
        fragments.add(myOrderFragmentPub);
        fragments.add(myOrderFragmentIng);
        fragments.add(myOrderFragmentOver);
        orderPagerAdapter = new OrderPagerAdapter(getSupportFragmentManager(),fragments);
        vpOrder.setAdapter(orderPagerAdapter);
        tlMain.setupWithViewPager(vpOrder);
    }

    @OnClick({R.id.ll_back, R.id.tv_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_btn:
                break;
        }
    }
}
