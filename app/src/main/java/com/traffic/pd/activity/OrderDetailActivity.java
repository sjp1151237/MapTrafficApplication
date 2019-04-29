package com.traffic.pd.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.traffic.pd.MainActivity;
import com.traffic.pd.OnMapAndViewReadyListener;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.ll_share)
    LinearLayout llShare;

    private GoogleMap mMap = null;

    /**
     * Keeps track of the selected marker.
     */
    private Marker mSelectedMarker;

    String fromWhere;

    NiceDialog niceDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.order_detail);
        niceDialog = NiceDialog.init();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);
        orderBean = (OrderBean) getIntent().getSerializableExtra("info");
        fromWhere = getIntent().getStringExtra("from");
        tvBtn.setVisibility(View.VISIBLE);
        if(TextUtils.isEmpty(orderBean.getUrl())){
            tvShare.setText("share url is null");
        }else{
            tvShare.setText("分享链接："+orderBean.getUrl());
        }
        if (fromWhere.equals("home")) {
            if (MainActivity.userBean.getIdentity().equals("2") || MainActivity.userBean.getIdentity().equals("3")) {
                if (null != orderBean.getCan_grab()) {
                    if (orderBean.getCan_grab().equals("0")) {
                        tvBtn.setVisibility(View.GONE);
                    }
                    if (orderBean.getCan_grab().equals("1")) {
                        tvBtn.setText("ADD");
                    }
                }
            }
        }
        if (fromWhere.equals("user")) {
            if (MainActivity.userBean.getIdentity().equals("2")) {
                // 审核中、发布中 可以取消订单
                if (orderBean.getStatus().equals("1") || orderBean.getStatus().equals("2")) {
                    tvBtn.setText(R.string.cancel);
                }
                // 以拒绝、已完成 可以删除订单
                if (orderBean.getStatus().equals("3") || orderBean.getStatus().equals("5")) {
//                    tvBtn.setText("Delete");
                    tvBtn.setVisibility(View.GONE);
                }
                // 进行中只有发布者可以操作
                if (orderBean.getStatus().equals("4")) {
                    tvBtn.setVisibility(View.GONE);
                }
            } else if (MainActivity.userBean.getIdentity().equals("3")) {
                tvBtn.setVisibility(View.GONE);
            } else {
                // 待审核/发布中
                if (orderBean.getStatus().equals("1") || orderBean.getStatus().equals("2")) {
                    tvBtn.setText(R.string.oper);
                }
                // 已拒绝
                if (orderBean.getStatus().equals("3") || orderBean.getStatus().equals("5")) {
                    tvBtn.setVisibility(View.GONE);
                }
                // 进行中只有发布者可以操作
                if (orderBean.getStatus().equals("4")) {
                    tvBtn.setVisibility(View.VISIBLE);
                    tvBtn.setText(R.string.over);
                }
            }
        }

        tvAddCars.setText(String.format("已有%s辆车接单", orderBean.getGrab_num()));
        if (null != orderBean) {
            tvGetPhone.setText("联系收货人："+orderBean.getRecive_mobile());
            tvSendPhone.setText("联系发货人："+orderBean.getB_country() + orderBean.getMobile());
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

    @OnClick({R.id.ll_back, R.id.ll_car_detail, R.id.tv_btn, R.id.ll_call_send, R.id.ll_call_get, R.id.ll_cars})
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
                                    toAddOrder(null);
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

                            niceDialog.setLayoutId(R.layout.order_confirm_dialog).setConvertListener(new ViewConvertListener() {
                                @Override
                                protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                                    TextView tv_confirm = holder.getView(R.id.tv_confirm);
                                    TextView tv_over = holder.getView(R.id.tv_over);

                                    tv_confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(OrderDetailActivity.this, OrderDriversActivity.class);
                                            intent.putExtra("id", orderBean.getId());
                                            intent.putExtra("status", orderBean.getStatus());
                                            startActivityForResult(intent, REFRESH_UI);
                                            niceDialog.cancelDialog();
                                        }
                                    });
                                    tv_over.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            userOverOrder();
                                            niceDialog.cancelDialog();
                                        }
                                    });
                                }
                            }).setDimAmount(0.3f).setShowBottom(true).show(getSupportFragmentManager());

                        }
                        // 已拒绝
                        if (orderBean.getStatus().equals("3") || orderBean.getStatus().equals("5")) {

                        }
                        // 进行中只有发布者可以操作
                        if (orderBean.getStatus().equals("4")) {
                            // 订单完成
                            userOverOrder();
                        }
                    }
                }
                break;
            case R.id.ll_call_send:
                try {
                    ComUtils.showCallDialog(OrderDetailActivity.this, orderBean.getB_country() + orderBean.getMobile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_call_get:
                try {
                    ComUtils.showCallDialog(OrderDetailActivity.this, orderBean.getRecive_mobile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_cars:
                if (MainActivity.userBean.getIdentity().equals(2)) {
                    ComUtils.showMsg(OrderDetailActivity.this, "don't have permission");
                    return;
                }
                Intent intent = new Intent(OrderDetailActivity.this, OrderDriversActivity.class);
                intent.putExtra("id", orderBean.getId());
                intent.putExtra("status", orderBean.getStatus());
                startActivityForResult(intent, REFRESH_UI);
                break;
        }
    }

    // 用户自动完成订单
    private void userOverOrder() {
        String url = Constant.ORDER_EDIT;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("order_id", orderBean.getId());
        map.put("status", "5");
        new PostRequest("userOverOrder", this, true)
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
                                ComUtils.showMsg(OrderDetailActivity.this, "success");
                                tvBtn.setText("已完成");
                                tvBtn.setEnabled(false);
                            } else {
                                ComUtils.showMsg(OrderDetailActivity.this, "fali");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(OrderDetailActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                    }
                }, url, map);
    }

    // 司机删除订单 保留
    private void driverDeleteOrder() {

    }

    // 司机取消订单
    private void driverCancelOrder() {
        String url = Constant.GRAB_DEL;
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
                            if (status == 1) {
                                // 刷新数据
                                EventBus.getDefault().post(new EventMessage(EventMessage.REFRESH_ORDER_HALL_DATA, ""));
                                ComUtils.showMsg(OrderDetailActivity.this, "success");
                                tvBtn.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(OrderDetailActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                    }
                }, url, map);
    }

    // 公司跳转到自己的车辆
    private static int TO_CAR = 1002;
    private static int REFRESH_UI = 1003;

    private void toCars() {
        Intent intent = new Intent(this, MyDriverActivity.class);
        intent.putExtra("type", "dsfsdf");
        startActivityForResult(intent, TO_CAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        if (requestCode == TO_CAR) {
            ArrayList<String> cars = data.getStringArrayListExtra("cars");
            if (null != cars && cars.size() > 0) {
                toAddOrder(cars);
            }
        }
        if (requestCode == REFRESH_UI) {

            Log.e("toEditOrder", "订单开始，刷新UI");
            tvBtn.setEnabled(false);
            tvBtn.setText(R.string.order_begin);

        }
    }

    private void toAddOrder(ArrayList<String> cars) {
        String url = Constant.GRAB_ORDER;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("order_id", orderBean.getId());
        if (null != cars) {
            StringBuilder carss = new StringBuilder();
            for (int i = 0; i < cars.size(); i++) {
                if (i == (cars.size() - 1)) {
                    carss.append(cars.get(i));
                } else {
                    carss.append(cars.get(i) + ",");
                }
            }
            map.put("list", carss.toString());
        }
        new PostRequest("toAddOrder", this, true)
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
                                ComUtils.showMsg(OrderDetailActivity.this, "success");
                                tvBtn.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(OrderDetailActivity.this, "error");
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


    @OnClick(R.id.ll_share)
    public void onViewClicked() {
        if(TextUtils.isEmpty(orderBean.getUrl())){
            ComUtils.showMsg(OrderDetailActivity.this,"share url is null");
        }else{
// 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) this
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(orderBean.getUrl());
            ComUtils.showMsg(this,"已复制到剪切板");
        }
    }
}
