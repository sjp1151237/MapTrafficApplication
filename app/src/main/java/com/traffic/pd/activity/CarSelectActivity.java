package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.traffic.pd.R;
import com.traffic.pd.adapter.CarSelectAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarSelectActivity extends AppCompatActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.rcv_car_select)
    RecyclerView rcvCarSelect;

    CarSelectAdapter carSelectAdapter;
    List<CarType> carTypeList;
    List<CarType> carSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);
        ButterKnife.bind(this);
        carTypeList = new ArrayList<>();
        carSelect = new ArrayList<>();
        carSelectAdapter = new CarSelectAdapter(carTypeList, this);
        rcvCarSelect.setLayoutManager(new LinearLayoutManager(this));
        rcvCarSelect.setAdapter(carSelectAdapter);
        tvBtn.setVisibility(View.VISIBLE);
        tvBtn.setText("Sure");
        tvTitle.setText("Select Cars");
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
                            if (status == 1) {
                                carTypeList.clear();
                                List<CarType> carTypes = JSONArray.parseArray(jsonObject.getString("result"), CarType.class);
                                for (int i = 0; i < carTypes.size(); i++) {
                                    carTypes.get(i).setNum(0);
                                }
                                carTypeList.addAll(carTypes);
                                carSelectAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(CarSelectActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(CarSelectActivity.this, "error");
                    }
                }, url, map);
    }

    @OnClick({R.id.ll_back, R.id.tv_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_btn:
                if(carTypeList.size() > 0){
                    carSelect.clear();
                    for (int i = 0; i < carTypeList.size(); i++) {
                        if(carTypeList.get(i).getNum() > 0){
                            carSelect.add(carTypeList.get(i));
                        }
                    }
                    EventBus.getDefault().post(new EventMessage(EventMessage.SELECT_CAR, carSelect));
                    finish();
                }
                break;
        }
    }
}
