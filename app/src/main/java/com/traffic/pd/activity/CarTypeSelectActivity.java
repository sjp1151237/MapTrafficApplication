package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.adapter.CarTypeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarTypeSelectActivity extends AppCompatActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rcv_type)
    RecyclerView rcvType;

    CarTypeAdapter carTypeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_select);
        ButterKnife.bind(this);
        carTypeAdapter = new CarTypeAdapter(this);
        tvTitle.setText("select car type");
        rcvType.setLayoutManager(new LinearLayoutManager(this));
        rcvType.setAdapter(carTypeAdapter);
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }
}
