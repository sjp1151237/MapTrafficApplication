package com.traffic.pd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.adapter.OrderDriverAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.data.CarInfo;
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

public class OrderDriversActivity extends AppCompatActivity implements OrderDriverAdapter.SelectCar {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.rcv_cars)
    RecyclerView rcvCars;

    String orderId,orderStatus;

    List<CarInfo> carInfoList;
    OrderDriverAdapter driversAdapter;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_drivers);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.order_rec_cars);
        orderId = getIntent().getStringExtra("id");
        orderStatus = getIntent().getStringExtra("status");
        carInfoList = new ArrayList<>();

        if (!TextUtils.isEmpty(orderId)) {
            loadDrivers();
        } else {
            finish();
        }
    }

    private void loadDrivers() {
        String url = Constant.ORDER_DTIVERS;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("order_id", orderId);
        new PostRequest("loadDriver", this, true)
                .go(this, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                carInfoList.clear();
                                carInfoList.addAll(JSONArray.parseArray(jsonObject.getString("data"), CarInfo.class));
                                for (int i = 0; i < carInfoList.size(); i++) {
                                    carInfoList.get(i).setSelect(true);
                                }
                                driversAdapter = new OrderDriverAdapter(OrderDriversActivity.this, carInfoList, orderStatus, OrderDriversActivity.this);
                                rcvCars.setLayoutManager(new LinearLayoutManager(OrderDriversActivity.this));
                                rcvCars.setAdapter(driversAdapter);

                                if(orderStatus.equals("2") && MainActivity.userBean.getIdentity().equals("1")){
                                    tvBtn.setVisibility(View.VISIBLE);
                                    tvBtn.setText(R.string.begin);
                                    tvTip.setVisibility(View.VISIBLE);
                                    tvTip.setText("You have select " + carInfoList.size() + " cars ,press 'Begin' to start the Order");
                                }else{
                                    tvBtn.setVisibility(View.GONE);
                                    tvTip.setVisibility(View.GONE);
                                }

                            }
                            if (status == 0) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(OrderDriversActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(OrderDriversActivity.this, "error");
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
                toConfirm();
                break;
        }
    }

    private void toConfirm() {
        tvBtn.setEnabled(false);
        String url = Constant.ORDER_CONFIRM;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("order_id", orderId);

        StringBuilder carss = new StringBuilder();
        for (int i = 0; i < carInfoList.size(); i++) {
            if(carInfoList.get(i).isSelect()){
                if(i == (carInfoList.size() - 1)){
                    carss.append(carInfoList.get(i).getId());
                }else{
                    carss.append(carInfoList.get(i).getId() + ",");
                }
            }
        }
        map.put("list", carss.toString());
        new PostRequest("toConfirm", this, true)
                .go(this, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                ComUtils.showMsg(OrderDriversActivity.this, "success");
                                toEditOrder();
                            }else{
                                tvBtn.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            tvBtn.setEnabled(true);
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        tvBtn.setEnabled(true);
                        ComUtils.showMsg(OrderDriversActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        tvBtn.setEnabled(true);
                        ComUtils.showMsg(OrderDriversActivity.this, "error");
                    }
                }, url, map);

    }

    private void toEditOrder() {
        String url = Constant.ORDER_EDIT;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("order_id", orderId);
        map.put("status", "4");
        new PostRequest("toEditOrder", this, true)
                .go(this, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                // 刷新数据
                                EventBus.getDefault().post(new EventMessage(EventMessage.REFRESH_ORDER_HALL_DATA, ""));
                                ComUtils.showMsg(OrderDriversActivity.this, "success");
                                tvBtn.setVisibility(View.GONE);
                                Intent intent = new Intent();
                                setResult(0, intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(OrderDriversActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                    }
                }, url, map);
    }


    @Override
    public void selectCar() {
        int select = 0;
        for (int i = 0; i < carInfoList.size(); i++) {
            if(carInfoList.get(i).isSelect()){
                select++;
            }
        }
        tvTip.setText("You have select " + select + " cars ,press 'Begin' to start the Order");
    }
}
