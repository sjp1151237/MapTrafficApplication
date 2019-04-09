package com.traffic.pd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.traffic.pd.R;
import com.traffic.pd.adapter.CarTypeAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarTypeSelectActivity extends AppCompatActivity implements CarTypeAdapter.CarTypeSelect {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rcv_type)
    RecyclerView rcvType;

    CarTypeAdapter carTypeAdapter;

    List<CarType> carTypeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_select);
        ButterKnife.bind(this);
        carTypeList = new ArrayList<>();
        carTypeAdapter = new CarTypeAdapter(this,carTypeList,this);
        tvTitle.setText("select car type");
        rcvType.setLayoutManager(new LinearLayoutManager(this));
        rcvType.setAdapter(carTypeAdapter);

        loadData();
    }

    private void loadData() {
        String url = Constant.GET_CAR_TYPE;
        Map<String, String> map = new HashMap<>();
        new PostRequest("loadCar", this, true)
                .go(this, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            ComUtils.showMsg(CarTypeSelectActivity.this, msg);
                            if (status == 1) {
                                carTypeList.clear();
                                carTypeList.addAll(JSONArray.parseArray(jsonObject.getString("result"),CarType.class));
                                carTypeAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(CarTypeSelectActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(CarTypeSelectActivity.this, "error");
                    }
                }, url, map);
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void carTypeSelect(CarType id) {
        Intent intent = new Intent();
        intent.putExtra("car",id);
        setResult(3, intent);
        finish();
    }
}
