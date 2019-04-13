package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CompanyInfo;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyDetailActivity extends AppCompatActivity {

    CompanyInfo companyInfo;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.tv_contacts)
    TextView tvContacts;
    @BindView(R.id.tv_contacts_num)
    TextView tvContactsNum;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_license)
    TextView tvLicense;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.rcv_pic)
    RecyclerView rcvPic;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
        ButterKnife.bind(this);
        companyInfo = (CompanyInfo) getIntent().getSerializableExtra("info");
        if (null != companyInfo) {
            if(companyInfo.getStatus().equals("2")){
                tvBtn.setText("已同意");
                tvBtn.setEnabled(false);
            }else if(companyInfo.getStatus().equals("1")){
                tvBtn.setText("同意");
                tvBtn.setEnabled(true);
            }else{
                tvBtn.setVisibility(View.GONE);
            }

            tvCompanyName.setText("Company Name : " +companyInfo.getName());
            tvContacts.setText("Contacts : " + companyInfo.getOwner());
            tvContactsNum.setText("Mobile : "+companyInfo.getMobile());
            tvIntro.setText("Introduce : "+companyInfo.getIntroduce());
            tvLicense.setText("license num :"+companyInfo.getLicense_num());
            tvLocation.setText("location : "+companyInfo.getCountry()  + "   "+ companyInfo.getProvince()+ "   " + companyInfo.getCity()+ "   " + companyInfo.getDistrict()+ "   " + companyInfo.getAddress());
        }

    }

    @OnClick({R.id.ll_back, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_commit:

                if(null != companyInfo){
                    bindCompany(companyInfo.getId());
                }

                break;
        }
    }

    private void bindCompany(String companyId) {
        String url = Constant.BIND_COMPANY;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("company_id", companyId);
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
                        ComUtils.showMsg(CompanyDetailActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(CompanyDetailActivity.this, "error");
                    }
                }, url, map);
    }
}
