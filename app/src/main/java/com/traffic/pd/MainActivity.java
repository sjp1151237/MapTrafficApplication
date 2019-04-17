package com.traffic.pd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.traffic.pd.activity.CarSelectActivity;
import com.traffic.pd.activity.UpDetailActivity;
import com.traffic.pd.adapter.MainFreagmentAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CarInfo;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.CompanyInfo;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.data.UserBean;
import com.traffic.pd.fragments.OrderHallFragment;
import com.traffic.pd.fragments.PublishFragment;
import com.traffic.pd.fragments.UserFragment;
import com.traffic.pd.services.LocationUpService;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;
import com.traffic.pd.utils.PreferencesUtils;
import com.traffic.pd.weigets.NoScrollViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    String tag;
    @BindView(R.id.vp_main)
    NoScrollViewPager vpMain;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.rl_home)
    RelativeLayout rlHome;
    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.rl_mine)
    RelativeLayout rlMine;

    public static UserBean userBean;
    public static CompanyInfo companyInfo;
    public static CarInfo carInfo;
    public static boolean isDetailUp;

    public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 555;

    List<Fragment> fragments;
    PublishFragment publishFragment = new PublishFragment();
    OrderHallFragment orderHallFragmentD = OrderHallFragment.newInstance("2", "");
    OrderHallFragment orderHallFragmentC = OrderHallFragment.newInstance("3", "");

    UserFragment userFragment;

    public static List<CarType> carTypeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        fragments.add(publishFragment);
        fragments.add(orderHallFragmentD);
        fragments.add(orderHallFragmentC);

        isDetailUp = false;
        carTypeList = new ArrayList<>();
        userBean = (UserBean) getIntent().getSerializableExtra("user");
        if (null != userBean) {
            tag = userBean.getIdentity();
            if (TextUtils.isEmpty(tag)) {
                if (!TextUtils.isEmpty(PreferencesUtils.getSharePreStr(this, Constant.Identity_Name))) {
                    tag = PreferencesUtils.getSharePreStr(this, Constant.Identity_Name);
                }
            }
            if (null != tag) {
                userFragment = UserFragment.newInstance(tag,"");
                fragments.add(userFragment);
                vpMain.setAdapter(new MainFreagmentAdapter(getSupportFragmentManager(), tag, fragments));
            }
            vpMain.setScroll(false);
        }
//        else {
//            tag = "2";
//            if (null != tag) {
//                vpMain.setAdapter(new MainFreagmentAdapter(getSupportFragmentManager(), tag, fragments));
//            }
//            vpMain.setScroll(false);
//        }
        ComUtils.getLocationPermission(this);

        if (null != userBean && !userBean.getIdentity().equals("1")) {
            getUserStatus();
        }

        loadCar();

    }

    private void loadCar() {
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                    }
                }, url, map);
    }

    private void getUserStatus() {
        String url = "";
        if (userBean.getIdentity().equals("2")) {
            url = Constant.DIVER_STATUS;
        }
        if (userBean.getIdentity().equals("3")) {
            url = Constant.COMPANY_STATUS;
        }
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", userBean.getUser_id());
        new PostRequest("getUserStatus", this, false)
                .go(this, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                isDetailUp = true;
                                if (userBean.getIdentity().equals("2")) {
                                    Intent intent = new Intent(MainActivity.this, LocationUpService.class);
                                    startService(intent);
                                    carInfo = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("data"),CarInfo.class);
                                }
                                if (userBean.getIdentity().equals("3")) {
                                    companyInfo = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("data"),CompanyInfo.class);
                                }
                                if(null != userFragment){
                                    userFragment.resetData();
                                }
                            }
                            if (status == 0) {
                                isDetailUp = false;
                                Intent intent = new Intent(MainActivity.this, UpDetailActivity.class);
                                intent.putExtra("tag", userBean.getIdentity());
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(MainActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(MainActivity.this, "error");
                    }
                }, url, map);

    }

    @OnClick({R.id.rl_home, R.id.rl_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                vpMain.setCurrentItem(0);
                ivHome.setImageDrawable(getResources().getDrawable(R.mipmap.home_select));
                ivMine.setImageDrawable(getResources().getDrawable(R.mipmap.mine_no));
                break;
            case R.id.rl_mine:
                vpMain.setCurrentItem(1);
                ivHome.setImageDrawable(getResources().getDrawable(R.mipmap.home_no));
                ivMine.setImageDrawable(getResources().getDrawable(R.mipmap.main_select));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        publishFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (orderHallFragmentC != null) {
            orderHallFragmentC.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (orderHallFragmentD != null) {
            orderHallFragmentD.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}