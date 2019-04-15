package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.adapter.DriversAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CarInfo;
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

public class MyDriverActivity extends AppCompatActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.rcv_driver)
    RecyclerView rcvDriver;

    NiceDialog niceDialog;

    List<CarInfo> carInfoList;
    DriversAdapter driversAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_driver);
        ButterKnife.bind(this);
        tvTitle.setText("My Driver");
        tvBtn.setText("Add Driver");
        tvBtn.setVisibility(View.VISIBLE);
        niceDialog = NiceDialog.init();

        carInfoList = new ArrayList<>();
        driversAdapter = new DriversAdapter(this,carInfoList);
        rcvDriver.setLayoutManager(new LinearLayoutManager(this));
        rcvDriver.setAdapter(driversAdapter);
        if (null != MainActivity.userBean && !TextUtils.isEmpty(MainActivity.userBean.getUser_id())) {
            loadDriver();
        } else {

        }

    }

    private void loadDriver() {
        String url = Constant.GET_Driver_LIST;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
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
                                carInfoList.addAll(JSONArray.parseArray(jsonObject.getString("data"),CarInfo.class));
                                driversAdapter.notifyDataSetChanged();
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
                        ComUtils.showMsg(MyDriverActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(MyDriverActivity.this, "error");
                    }
                }, url, map);


    }

    private void addDriver(String id){
        String url = Constant.GET_ADD_Driver;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("car_num",id);
        new PostRequest("getUserStatus", this, true)
                .go(this, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            ComUtils.showMsg(MyDriverActivity.this,msg);
                            if(status == 1){
                                loadDriver();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(MyDriverActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(MyDriverActivity.this, "error");
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
                niceDialog.setLayoutId(R.layout.add_driver_dialog).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        final EditText editText = holder.getView(R.id.et_driver);
                        TextView tv_commit = holder.getView(R.id.tv_commit);
                        tv_commit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(TextUtils.isEmpty(editText.getText().toString())){
                                    ComUtils.showMsg(MyDriverActivity.this,"Please input driver's ID");
                                    return;
                                }else{
                                    addDriver(editText.getText().toString());
                                    niceDialog.dismiss();
                                }

                            }
                        });
                    }
                }).setDimAmount(0.3f).setShowBottom(true).show(getSupportFragmentManager());
                break;
        }
    }
}