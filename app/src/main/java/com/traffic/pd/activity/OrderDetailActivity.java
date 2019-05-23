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

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.data.UserBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;
import com.traffic.pd.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends AppCompatActivity {

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

    String fromWhere;

    NiceDialog niceDialog;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_address_receive)
    TextView tvAddressReceive;
    @BindView(R.id.tv_cargo_name)
    TextView tvCargoName;
    @BindView(R.id.tv_cargo_type)
    TextView tvCargoType;
    @BindView(R.id.tv_way_of_loading)
    TextView tvWayOfLoading;
    @BindView(R.id.tv_cargo_weight)
    TextView tvCargoWeight;
    @BindView(R.id.tv_cargo_volume)
    TextView tvCargoVolume;
    @BindView(R.id.tv_cargo_whd)
    TextView tvCargoWhd;
    @BindView(R.id.tv_requirements)
    TextView tvRequirements;
    @BindView(R.id.tv_cargo_wrappage)
    TextView tvCargoWrappage;
    @BindView(R.id.tv_is_danger)
    TextView tvIsDanger;
    @BindView(R.id.tv_address_start)
    TextView tvAddressStart;
    @BindView(R.id.tv_receive_address_start)
    TextView tvReceiveAddressStart;
    @BindView(R.id.ll_start)
    LinearLayout llStart;
    @BindView(R.id.llover)
    LinearLayout llover;
    @BindView(R.id.tv_time)
    TextView tvTime;
    UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.order_detail);
        niceDialog = NiceDialog.init();

        orderBean = (OrderBean) getIntent().getSerializableExtra("info");
        userBean = com.alibaba.fastjson.JSONObject.parseObject(PreferencesUtils.getSharePreStr(this, Constant.USER_INFO), UserBean.class);
        if(null == userBean){
            finish();
        }
        initOrderView();
        fromWhere = getIntent().getStringExtra("from");
        tvBtn.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(orderBean.getUrl())) {
            tvShare.setText("share url is null");
        } else {
            tvShare.setText("分享链接：" + orderBean.getUrl());
        }
        if (fromWhere.equals("home")) {
            if (userBean.getIdentity().equals("2") || userBean.getIdentity().equals("3")) {
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
            if (null != userBean && userBean.getIdentity().equals("2")) {
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
            } else if (userBean.getIdentity().equals("3")) {
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
            tvGetPhone.setText("联系收货人：" + orderBean.getRecive_mobile());
            tvSendPhone.setText("联系发货人：" + orderBean.getB_country() + orderBean.getMobile());
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

    private void initOrderView() {
        if (null != orderBean) {
            tvTime.setText("开始时间 ："+ ComUtils.formatString(orderBean.getStart_time()));
            tvReceiveAddressStart.setText("终点 : " + ComUtils.formatString(orderBean.getRecive_country()) + " " + ComUtils.formatString(orderBean.getRecive_province()) + " " + ComUtils.formatString(orderBean.getRecive_city()) + " " + ComUtils.formatString(orderBean.getRecive_district()) + "\n" + ComUtils.formatString(orderBean.getRecive_address()));
            tvAddressStart.setText("起点 : " + ComUtils.formatString(orderBean.getCountry()) + " " + ComUtils.formatString(orderBean.getProvince()) + " " + ComUtils.formatString(orderBean.getCity()) + " " + ComUtils.formatString(orderBean.getDistrict()) + "\n" + ComUtils.formatString(orderBean.getAddress()));
//            tvAddressReceive.setText(ComUtils.formatString(orderBean.getRecive_address()));
//            tvAddress.setText(ComUtils.formatString(orderBean.getAddress()));
            if (TextUtils.isEmpty(orderBean.getGoods_name())) {
                tvCargoName.setText("货物名称 : " + "未知");
            } else {
                tvCargoName.setText("货物名称 : " + ComUtils.formatString(orderBean.getGoods_name()));
            }
            if (TextUtils.isEmpty(orderBean.getGoods_kind())) {
                tvCargoType.setText("货物种类 : " + "未知");
            } else {
                tvCargoType.setText("货物种类 : " + ComUtils.formatString(orderBean.getGoods_kind()));
            }
            if (TextUtils.isEmpty(orderBean.getGoods_way())) {
                tvWayOfLoading.setText("装货方式 : " + "未知");
            } else {
                tvWayOfLoading.setText("装货方式 : " + ComUtils.formatString(orderBean.getGoods_way()));
            }
            if (TextUtils.isEmpty(orderBean.getGoods_volume())) {
                tvCargoVolume.setText("货物体积 : " + "未知");
            } else {
                tvCargoVolume.setText("货物体积 : " + ComUtils.formatString(orderBean.getGoods_volume()));
            }
            if (TextUtils.isEmpty(orderBean.getGoods_weight())) {
                tvCargoWeight.setText("货物毛重 : " + "未知");
            } else {
                tvCargoWeight.setText("货物毛重 : " + ComUtils.formatString(orderBean.getGoods_weight()));
            }
            if (TextUtils.isEmpty(orderBean.getGoods_size())) {
                tvCargoWhd.setText("货物长宽高 : " + "未知");
            } else {
                tvCargoWhd.setText("货物长宽高 : " + ComUtils.formatString(orderBean.getGoods_size()));
            }
            if (TextUtils.isEmpty(orderBean.getGoods_pack())) {
                tvCargoWrappage.setText("外包装材质 : " + "未知");
            } else {
                tvCargoWrappage.setText("外包装材质 : " + ComUtils.formatString(orderBean.getGoods_pack()));
            }
            if (TextUtils.isEmpty(orderBean.getGoods_require())) {
                tvRequirements.setText("装货要求 : " + "无");
            } else {
                tvRequirements.setText("装货要求 : " + ComUtils.formatString(orderBean.getGoods_require()));
            }
            if (TextUtils.isEmpty(orderBean.getGoods_danger())) {
                tvIsDanger.setVisibility(View.GONE);
            } else {
                String isDan = orderBean.getGoods_require().equals("1") ? "是" : "否";
                tvIsDanger.setText("是否是危化品 : " + isDan);
            }
        }
    }

    @OnClick({R.id.ll_back, R.id.ll_car_detail, R.id.tv_btn, R.id.ll_call_send, R.id.ll_call_get, R.id.ll_cars, R.id.ll_start, R.id.llover})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_start:
            case R.id.llover:
                Intent intents = new Intent(OrderDetailActivity.this, OrderLocActivity.class);
                intents.putExtra("begin_lat", ComUtils.formatString(orderBean.getLat()));
                intents.putExtra("begin_long", ComUtils.formatString(orderBean.getLongi()));

                intents.putExtra("over_lat", ComUtils.formatString(orderBean.getRecive_lat()));
                intents.putExtra("over_long", ComUtils.formatString(orderBean.getRecive_longi()));
                startActivity(intents);
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_car_detail:

                break;
            case R.id.tv_btn:
                if (fromWhere.equals("home")) {
                    if (userBean.getIdentity().equals("2") || userBean.getIdentity().equals("3")) {
                        if (null != orderBean.getCan_grab()) {
                            if (orderBean.getCan_grab().equals("0")) {
                                driverCancelOrder();
                            }
                            if (orderBean.getCan_grab().equals("1")) {
                                if (userBean.getIdentity().equals("2")) {
                                    toAddOrder(null);
                                } else {
                                    toCars();
                                }
                            }
                        }
                    }
                }
                if (fromWhere.equals("user")) {
                    if (userBean.getIdentity().equals("2") || userBean.getIdentity().equals("3")) {
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
                if (userBean.getIdentity().equals(2)) {
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
        map.put("user_sign", userBean.getUser_id());
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
        map.put("user_sign", userBean.getUser_id());
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
        map.put("user_sign", userBean.getUser_id());
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
                        // 刷新数据
                        EventBus.getDefault().post(new EventMessage(EventMessage.REFRESH_ORDER_HALL_DATA, ""));
                        ComUtils.showMsg(OrderDetailActivity.this, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                    }
                }, url, map);
    }

    @OnClick(R.id.ll_share)
    public void onViewClicked() {
        if (TextUtils.isEmpty(orderBean.getUrl())) {
            ComUtils.showMsg(OrderDetailActivity.this, "share url is null");
        } else {
// 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) this
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(orderBean.getUrl());
            ComUtils.showMsg(this, "已复制到剪切板");
        }
    }

}
