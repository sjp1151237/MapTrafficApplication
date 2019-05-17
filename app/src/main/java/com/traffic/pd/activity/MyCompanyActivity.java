package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.adapter.MyCompanyAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CompanyInfo;
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

public class MyCompanyActivity extends AppCompatActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.rcv_companys)
    RecyclerView rcvCompanys;
    MyCompanyAdapter myCompanyAdapter;
    List<CompanyInfo> companyInfoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company);
        ButterKnife.bind(this);
        tvTitle.setText("My Company");
        companyInfoList = new ArrayList<>();
        myCompanyAdapter  =  new MyCompanyAdapter(this,companyInfoList);
        rcvCompanys.setLayoutManager(new LinearLayoutManager(this));
        rcvCompanys.setAdapter(myCompanyAdapter);

        loadData();
    }

    private void loadData() {
        String url = Constant.GET_COMPANY_LIST;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        new PostRequest("loadData", this, true)
                .go(this, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                companyInfoList.clear();
                                companyInfoList.addAll(JSONArray.parseArray(jsonObject.getString("data"),CompanyInfo.class));

                                myCompanyAdapter.notifyDataSetChanged();
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
                        ComUtils.showMsg(MyCompanyActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(MyCompanyActivity.this, "error");
                    }
                }, url, map);
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }
}
