package com.traffic.pd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.traffic.pd.R;
import com.traffic.pd.adapter.CodeSelectTipAdapter;
import com.traffic.pd.adapter.PhoneCodeAdapter;
import com.traffic.pd.data.CodeSelectTip;
import com.traffic.pd.data.PhoneCodeBean;
import com.traffic.pd.utils.ComUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoosePhoneCodeActivity extends AppCompatActivity implements PhoneCodeAdapter.SelectCode, CodeSelectTipAdapter.ClickTip {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rcv_code)
    RecyclerView rcvCode;
    PhoneCodeAdapter phoneCodeAdapter;

    List<PhoneCodeBean> beanList;
    @BindView(R.id.rcv_tip)
    RecyclerView rcvTip;

    LinearLayoutManager llmCode;

    List<CodeSelectTip> tipList;
    String[] zimu = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_phone_code);

        ButterKnife.bind(this);
        tvTitle.setText("号码归属地");
        String data = ComUtils.getJson("sds.json", this);
        if (!TextUtils.isEmpty(data)) {
            beanList = JSONArray.parseArray(data, PhoneCodeBean.class);
            phoneCodeAdapter = new PhoneCodeAdapter(beanList, this, this);

            llmCode = new LinearLayoutManager(this);
            rcvCode.setLayoutManager(llmCode);
            rcvCode.setAdapter(phoneCodeAdapter);

            rcvCode.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int fvp = llmCode.findFirstCompletelyVisibleItemPosition();


                    Log.e("CODE","第一个可见位置==== "+ String.valueOf(fvp));
                }
            });
        } else {
            finish();
        }

        initTip();



    }

    private void initTip() {
        tipList = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            CodeSelectTip codeSelectTip = new CodeSelectTip();
            codeSelectTip.setTip(zimu[i]);
            if(i==0){
                codeSelectTip.setSelect(true);
            }else{
                codeSelectTip.setSelect(false);
            }
            tipList.add(codeSelectTip);
        }
        rcvTip.setLayoutManager(new LinearLayoutManager(this));
        rcvTip.setAdapter(new CodeSelectTipAdapter(tipList,this,this));
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void selectCode(int i) {
        Intent intent = new Intent();
        intent.putExtra("res", beanList.get(i));
        setResult(2, intent);
        finish();
    }

    @Override
    public void clickTip(int pos) {

    }
}
