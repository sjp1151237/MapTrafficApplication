package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.adapter.ImagesAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CarInfo;
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
    List<String> imgs;
    ImagesAdapter imagesAdapter;
    @BindView(R.id.tv_grade)
    TextView tvGrade;

    NiceDialog startDialog;

    String orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        ButterKnife.bind(this);
        imgs = new ArrayList<>();
        carInfo = (CarInfo) getIntent().getSerializableExtra("info");

        tvTitle.setText("车辆详情");
        orderStatus = getIntent().getStringExtra("status");
        if(!TextUtils.isEmpty(orderStatus)){
            if(orderStatus.equals("5")){
                tvBtn.setVisibility(View.VISIBLE);
            }else{
                tvBtn.setVisibility(View.GONE);
            }
        }else{
            tvBtn.setVisibility(View.GONE);
        }

        tvBtn.setText("打分");
        tvGrade.setText("服务分： "+carInfo.getScore() + "  (满分5分)");
        tvDriver.setText("司机：" + carInfo.getDriver());
        tvMobile.setText("联系方式：" + carInfo.getMobile());
        tvIntro.setText("车辆介绍：" + carInfo.getIntroduce());
        tvLocation.setText("地址 : " + carInfo.getCountry() + "   " + carInfo.getProvince() + "   " + carInfo.getCity() + "   " + carInfo.getDistrict() + "   " + carInfo.getAddress());

        if (null != carInfo.getCar_num_pic()) {
            imgs.add(carInfo.getCar_num_pic());
        }
        if (null != carInfo.getCar_pic()) {
            imgs.addAll(carInfo.getCar_pic());
        }
        imagesAdapter = new ImagesAdapter(imgs, this);
        rcvPic.setLayoutManager(new LinearLayoutManager(this));
        rcvPic.setAdapter(imagesAdapter);

        startDialog = NiceDialog.init();


    }

    @OnClick({R.id.ll_back,R.id.tv_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_btn:
                if(null == MainActivity.userBean){
                    return;
                }
                startDialog.setLayoutId(R.layout.car_to_star).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        RadioGroup rgp = holder.getView(R.id.rg_main);
                        int star = 5;
                        switch (rgp.getCheckedRadioButtonId()) {
                            case R.id.rb_1:
                                star = 1;
                                break;
                            case R.id.rb_2:
                                star = 2;
                                break;
                            case R.id.rb_3:
                                star = 3;
                                break;
                            case R.id.rb_4:
                                star = 4;
                                break;
                            case R.id.rb_5:
                                star = 4;
                                break;
                        }
                        final Button btn_commit = holder.getView(R.id.btn_commit);
                        final int finalStar = star;
                        btn_commit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_commit.setEnabled(false);
                                startDialog.cancelDialog();
                                String url = Constant.CAR_STAR;
                                Map<String, String> map = new HashMap<>();
                                map.put("user_sign",MainActivity.userBean.getUser_id());
                                map.put("driver_id",carInfo.getId());
                                map.put("order_id",carInfo.getOrder_id());
                                map.put("score",String.valueOf(finalStar));
                                new PostRequest("CAR_STAR", CarDetailActivity.this, true)
                                        .go(CarDetailActivity.this, new PostRequest.PostListener() {

                                            @Override
                                            public TestBean postSuccessful(String response) {
                                                JSONObject jsonObject = null;
                                                try {
                                                    jsonObject = new JSONObject(response);
                                                    int status = jsonObject.getInt("status");
                                                    if(status == 1){
                                                        ComUtils.showMsg(CarDetailActivity.this, "success");
                                                    }else{
                                                        ComUtils.showMsg(CarDetailActivity.this, "You have been graded");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                return null;
                                            }

                                            @Override
                                            public void postError(String error) {
                                                super.postError(error);
                                                btn_commit.setEnabled(true);
                                                ComUtils.showMsg(CarDetailActivity.this, "error");
                                            }

                                            @Override
                                            public void postNull() {
                                                super.postNull();
                                                ComUtils.showMsg(CarDetailActivity.this, "error");
                                            }
                                        }, url, map);
                            }
                        });

                    }
                }).setDimAmount(0.3f).setShowBottom(true).show(getSupportFragmentManager());

                break;
        }
    }

}
