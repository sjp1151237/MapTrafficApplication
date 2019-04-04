package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.traffic.pd.R;
import com.traffic.pd.adapter.PhoneCodeAdapter;
import com.traffic.pd.data.PhoneCodeBean;
import com.traffic.pd.utils.ComUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoosePhoneCodeActivity extends AppCompatActivity implements PhoneCodeAdapter.SelectCode {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rcv_code)
    RecyclerView rcvCode;
    PhoneCodeAdapter phoneCodeAdapter;

    List<PhoneCodeBean> beanList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_phone_code);
        ButterKnife.bind(this);
        String data = ComUtils.getJson("sds.json", this);
        if(!TextUtils.isEmpty(data)){
            beanList = JSONArray.parseArray(data,PhoneCodeBean.class);
            phoneCodeAdapter = new PhoneCodeAdapter(beanList,this,this);

            rcvCode.setLayoutManager(new LinearLayoutManager(this));
            rcvCode.setAdapter(phoneCodeAdapter);
        }else{
            finish();
        }

    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void selectCode(int i) {
        Toast.makeText(this,beanList.get(i).getA(),Toast.LENGTH_SHORT).show();
        finish();
    }
}
