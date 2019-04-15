package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.adapter.ImagesAdapter;
import com.traffic.pd.data.CarInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarDetailActivity extends AppCompatActivity {


    CarInfo carInfo;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.tv_driver)
    TextView tvDriver;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.rcv_pic)
    RecyclerView rcvPic;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    List<String> imgs;
    ImagesAdapter imagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        ButterKnife.bind(this);
        imgs = new ArrayList<>();
        carInfo = (CarInfo) getIntent().getSerializableExtra("info");

        tvDriver.setText(carInfo.getDriver());
        tvMobile.setText(carInfo.getMobile());
        tvIntro.setText(carInfo.getIntroduce());
        tvLocation.setText("location : "+carInfo.getCountry()  + "   "+ carInfo.getProvince()+ "   " + carInfo.getCity()+ "   " + carInfo.getDistrict()+ "   " + carInfo.getAddress());

        if(null != carInfo.getCar_num_pic()){
            imgs.add(carInfo.getCar_num_pic());
        }
        if(null != carInfo.getCar_pic()){
            imgs.addAll(carInfo.getCar_pic());
        }
        imagesAdapter = new ImagesAdapter(imgs,this);
        rcvPic.setLayoutManager(new LinearLayoutManager(this));
        rcvPic.setAdapter(imagesAdapter);

    }

    @OnClick({R.id.ll_back, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                break;
            case R.id.tv_commit:
                break;
        }
    }
}
