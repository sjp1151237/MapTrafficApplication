package com.traffic.pd.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.traffic.pd.MainActivity;
import com.traffic.pd.OnMapAndViewReadyListener;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;
import com.traffic.pd.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.tv_send_phone)
    TextView tvSendPhone;
    @BindView(R.id.tv_get_phone)
    TextView tvGetPhone;
    @BindView(R.id.ll_car_detail)
    LinearLayout llCarDetail;
    OrderBean orderBean;

    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    @BindView(R.id.tv_add_cars)
    TextView tvAddCars;
    @BindView(R.id.ll_call_send)
    LinearLayout llCallSend;
    @BindView(R.id.ll_call_get)
    LinearLayout llCallGet;

    private GoogleMap mMap = null;

    /**
     * Keeps track of the selected marker.
     */
    private Marker mSelectedMarker;

    String fromWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);
        orderBean = (OrderBean) getIntent().getSerializableExtra("info");
        fromWhere = getIntent().getStringExtra("from");
        tvBtn.setVisibility(View.VISIBLE);
        if (fromWhere.equals("home")) {
            if (MainActivity.userBean.getIdentity().equals("2") || MainActivity.userBean.getIdentity().equals("3")) {
                if (null != orderBean.getCan_grab()) {
                    if (orderBean.getCan_grab().equals("0")) {
                        tvBtn.setText("Cancel");
                    }
                    if (orderBean.getCan_grab().equals("1")) {
                        tvBtn.setText("ADD");
                    }
                }
            }
        }
        if (fromWhere.equals("user")) {
            if (MainActivity.userBean.getIdentity().equals("2") || MainActivity.userBean.getIdentity().equals("3")) {
                // 审核中、发布中 可以取消订单
                if (orderBean.getStatus().equals("1") || orderBean.getStatus().equals("2")) {
                    tvBtn.setText("Cancel");
                }
                // 以拒绝、已完成 可以删除订单
                if (orderBean.getStatus().equals("3") || orderBean.getStatus().equals("5")) {
                    tvBtn.setText("Delete");
                }
                // 进行中只有发布者可以操作
                if (orderBean.getStatus().equals("4")) {
                    tvBtn.setVisibility(View.GONE);
                }
            } else {
                // 待审核/发布中
                if (orderBean.getStatus().equals("1") || orderBean.getStatus().equals("2")) {
                    tvBtn.setText("Cancel");
                }
                // 已拒绝
                if (orderBean.getStatus().equals("3") || orderBean.getStatus().equals("5")) {
                    tvBtn.setText("Delete");
                }
                // 进行中只有发布者可以操作
                if (orderBean.getStatus().equals("4")) {
                    tvBtn.setVisibility(View.GONE);
                }
            }
        }

        tvAddCars.setText("已有" + orderBean.getGrab_num() + "辆车接单");
        if (null != orderBean) {
            tvGetPhone.setText(orderBean.getRecive_mobile());
            tvSendPhone.setText(orderBean.getB_country() + orderBean.getMobile());
            try {
                if (null != MainActivity.carTypeList) {
                    String[] cars = orderBean.getCar_type().split(",");

                    for (int i = 0; i < cars.length; i++) {
                        String[] carT = cars[i].split(":");
                        String carTypeId = carT[0];
                        String carTypeNum = carT[1];
                        for (int j = 0; j < MainActivity.carTypeList.size(); j++) {
                            if (MainActivity.carTypeList.get(j).getId().equals(carTypeId)) {
                                Log.e("OrderDetailActivity", "====================" + carTypeId);
                                CarType carType = MainActivity.carTypeList.get(i);
                                carType.setNum(Integer.parseInt(carTypeNum));
                                View view = LayoutInflater.from(this).inflate(R.layout.order_car_select_item, null);
                                TextView tv_weight, tv_volume, tv_capacity, tv_car_num;
                                tv_weight = view.findViewById(R.id.tv_weight);
                                tv_volume = view.findViewById(R.id.tv_volume);
                                tv_capacity = view.findViewById(R.id.tv_capacity);
                                tv_car_num = view.findViewById(R.id.tv_car_num);
                                tv_weight.setText(carType.getWeight());
                                tv_volume.setText(carType.getVolume());
                                tv_capacity.setText(carType.getCapacity());
                                tv_car_num.setText(carType.getNum() + "辆");
                                llCarDetail.addView(view);
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
//            BRISBANE = new LatLng(-27.47093, 153.0235);
//            MELBOURNE = new LatLng(-37.81319, 144.96298);
        }
    }

    @OnClick({R.id.ll_back, R.id.ll_car_detail, R.id.tv_btn,R.id.ll_call_send, R.id.ll_call_get})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_car_detail:

                break;
            case R.id.tv_btn:
                if (fromWhere.equals("home")) {
                    if (MainActivity.userBean.getIdentity().equals("2") || MainActivity.userBean.getIdentity().equals("3")) {
                        if (null != orderBean.getCan_grab()) {
                            if (orderBean.getCan_grab().equals("0")) {
                                driverCancelOrder();
                            }
                            if (orderBean.getCan_grab().equals("1")) {
                                if (MainActivity.userBean.getIdentity().equals("2")) {
                                    toAddOrder();
                                } else {
                                    toCars();
                                }
                            }
                        }
                    }
                }
                if (fromWhere.equals("user")) {
                    if (MainActivity.userBean.getIdentity().equals("2") || MainActivity.userBean.getIdentity().equals("3")) {
                        // 审核中、发布中 可以取消订单
                        if (orderBean.getStatus().equals("1") || orderBean.getStatus().equals("2")) {
                            driverCancelOrder();
                        }
                        // 以拒绝、已完成 可以删除订单
                        if (orderBean.getStatus().equals("3") || orderBean.getStatus().equals("5")) {
                            driverDeleteOrder();
                        }
                        // 进行中只有发布者可以操作
                        if (orderBean.getStatus().equals("4")) {

                        }
                    } else {
                        // 待审核/发布中
                        if (orderBean.getStatus().equals("1") || orderBean.getStatus().equals("2")) {
                            userCancelOrder();
                        }
                        // 已拒绝
                        if (orderBean.getStatus().equals("3") || orderBean.getStatus().equals("5")) {
                            userDeleteOrder();
                        }
                        // 进行中只有发布者可以操作
                        if (orderBean.getStatus().equals("4")) {
                            tvBtn.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case R.id.ll_call_send:
                try {
                    ComUtils.showCallDialog(OrderDetailActivity.this,orderBean.getB_country() + orderBean.getMobile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_call_get:
                try {
                    ComUtils.showCallDialog(OrderDetailActivity.this,orderBean.getRecive_mobile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // 用户删除订单
    private void userDeleteOrder() {

    }

    // 用户取消订单
    private void userCancelOrder() {

    }

    // 司机删除订单
    private void driverDeleteOrder() {

    }

    // 司机取消订单
    private void driverCancelOrder() {

    }

    // 公司跳转到自己的车辆
    private void toCars() {

    }

    private void toAddOrder() {
        String url = Constant.GRAB_ORDER;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("order_id", orderBean.getId());
        new PostRequest("toAddOrder", this, true)
                .go(this, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            ComUtils.showMsg(OrderDetailActivity.this, jsonObject.getString("msg"));
                            if (status == 1) {

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

    @Override
    public void onMapClick(LatLng latLng) {
        mSelectedMarker = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(mSelectedMarker)) {
            mSelectedMarker = null;
            return true;
        }

        mSelectedMarker = marker;
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Hide the zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();

        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        mMap.setOnMapClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localized.
        googleMap.setContentDescription("Demo showing how to close the info window when the currently"
                + " selected marker is re-tapped.");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(BRISBANE)
                .include(MELBOURNE)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
    }

    private void addMarkersToMap() {
        mMap.addMarker(new MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane")
                .snippet("Population: 2,074,200"));

        mMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("Melbourne")
                .snippet("Population: 4,137,400"));

    }



}
