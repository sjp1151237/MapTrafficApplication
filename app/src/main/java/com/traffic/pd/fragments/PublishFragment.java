package com.traffic.pd.fragments;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.CarSelectActivity;
import com.traffic.pd.activity.ChoosePhoneCodeActivity;
import com.traffic.pd.activity.MyOrderActivity;
import com.traffic.pd.adapter.CargoTypeSelectAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.PhoneCodeBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.maps.MyLocationDemoActivity;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PublishFragment extends Fragment implements CargoTypeSelectAdapter.CargoLoadSelect, CargoTypeSelectAdapter.CargoTypeSelect, CargoTypeSelectAdapter.CargoDangerSelect {

    @BindView(R.id.put_choice)
    TextView putChoice;
    @BindView(R.id.ll_select_car)
    LinearLayout llSelectCar;
    @BindView(R.id.put_shipping_address)
    TextView putShippingAddress;
    @BindView(R.id.ll_shipping_address)
    LinearLayout llShippingAddress;
    @BindView(R.id.put_receipt_address)
    TextView putReceiptAddress;
    @BindView(R.id.ll_receipt_address)
    LinearLayout llReceiptAddress;
    @BindView(R.id.put_name)
    EditText putName;
    @BindView(R.id.put_phone_num)
    EditText putPhoneNum;
    @BindView(R.id.tv_phone_code)
    TextView tvPhoneCode;
    @BindView(R.id.ll_phone_code)
    LinearLayout llPhoneCode;
    @BindView(R.id.put_time)
    TextView putTime;
    @BindView(R.id.ll_put_time)
    LinearLayout llPutTime;
    @BindView(R.id.tv_cargo_type)
    TextView tvCargoType;
    @BindView(R.id.ll_cargo_type)
    LinearLayout llCargoType;
    @BindView(R.id.et_requirements)
    EditText etRequirements;
    @BindView(R.id.cargo_name)
    EditText cargoName;
    @BindView(R.id.tv_way_of_loading)
    TextView tvWayOfLoading;
    @BindView(R.id.ll_way_of_loading)
    LinearLayout llWayOfLoading;
    @BindView(R.id.cargo_weight)
    EditText cargoWeight;
    @BindView(R.id.cargo_volume)
    EditText cargoVolume;
    @BindView(R.id.cargo_d)
    EditText cargoD;
    @BindView(R.id.cargo_w)
    EditText cargoW;
    @BindView(R.id.cargo_h)
    EditText cargoH;
    @BindView(R.id.cargo_wrappage)
    EditText cargoWrappage;
    @BindView(R.id.tv_is_danger)
    TextView tvIsDanger;
    @BindView(R.id.ll_is_danger)
    LinearLayout llIsDanger;
    Unbinder unbinder;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    private View mView;
    private List<CarType> carSelect;

    NiceDialog cargoTypeDialog, cargoLoadDialog, dangerDialog,orderSuccessDialog;

    private static int Location_phone = 1001;
    private static int Location_map_s = 1002;
    private static int Location_map_g = 1003;
    private static int cargo_type = 1004;

    PhoneCodeBean phoneCodeBean;
    Address addressS, addressG;

    List<String> cargoType;
    List<String> loadType;
    List<String> dangerType;

    String addressDetail, recAddressDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.test, container, false);
            unbinder = ButterKnife.bind(this, mView);
            carSelect = new ArrayList<>();
            EventBus.getDefault().register(this);

            tvTitle.setText("发布订单");
            tvBtn.setText("发布");
            tvBtn.setVisibility(View.VISIBLE);

            cargoLoadDialog = NiceDialog.init();
            cargoTypeDialog = NiceDialog.init();
            dangerDialog = NiceDialog.init();
            orderSuccessDialog = NiceDialog.init();

            initData();
        }
        return mView;
    }

    private void initData() {
        loadType = new ArrayList<>();
        loadType.add("散货");
        loadType.add("集装箱");
        cargoType = new ArrayList<>();
        cargoType.add("食品");
        cargoType.add("矿物");
        cargoType.add("建材");
        cargoType.add("车辆");
        cargoType.add("药品");
        cargoType.add("服装");
        cargoType.add("家具");
        cargoType.add("工程物资");
        cargoType.add("其他");
        dangerType = new ArrayList<>();
        dangerType.add("是");
        dangerType.add("否");

    }

    StringBuilder carSelects;
    int carNum;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMessage(EventMessage eventMessage) {
        switch (eventMessage.getType()) {
            case EventMessage.SELECT_CAR:
                try {
                    carSelect.clear();
                    carSelect.addAll((List<CarType>) eventMessage.getObject());
                    carNum = 0;
                    if (null != carSelect && carSelect.size() > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        carSelects = new StringBuilder();
                        for (int i = 0; i < carSelect.size(); i++) {
                            stringBuilder.append("型号：" + carSelect.get(i).getId() + "  车数：" + carSelect.get(i).getNum() + "、");
                            carSelects.append(carSelect.get(i).getId() + ":" + carSelect.get(i).getNum() + ",");
                            carNum = carNum + carSelect.get(i).getNum();
                        }
                        putChoice.setText(stringBuilder.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        if (requestCode == Location_phone && resultCode == 2) {
            phoneCodeBean = (PhoneCodeBean) data.getSerializableExtra("res");
            tvPhoneCode.setText(phoneCodeBean.getA());
        }
        if (requestCode == Location_map_s) {
            addressS = (Address) data.getParcelableExtra("address");
            if (!TextUtils.isEmpty(data.getStringExtra("detail"))) {
                addressDetail = data.getStringExtra("detail");
            } else {
                addressDetail = "";
            }

            if (null != addressS) {
                String addresss = ComUtils.formatString(addressS.getSubAdminArea()) + "   " + ComUtils.formatString(addressS.getLocality() + "   " + ComUtils.formatString(addressS.getThoroughfare()));
                putShippingAddress.setText(addresss);
            }
        }

        if (requestCode == Location_map_g) {
            addressG = (Address) data.getParcelableExtra("address");
            if (!TextUtils.isEmpty(data.getStringExtra("detail"))) {
                recAddressDetail = data.getStringExtra("detail");
            } else {
                recAddressDetail = "";
            }
            if (null != addressG) {
                String addresss =  ComUtils.formatString(addressG.getSubAdminArea()) + "   " + ComUtils.formatString(addressG.getLocality() + "   " + ComUtils.formatString(addressG.getThoroughfare()));
                putReceiptAddress.setText(addresss);
            }
        }
    }

    final Calendar calendar = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象

    @OnClick({R.id.tv_btn,R.id.ll_select_car, R.id.ll_put_time, R.id.ll_shipping_address, R.id.ll_is_danger, R.id.ll_receipt_address, R.id.ll_phone_code, R.id.ll_cargo_type, R.id.ll_way_of_loading,R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_is_danger:
                dangerDialog.setLayoutId(R.layout.activity_cargo_type).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        RecyclerView recyclerView = holder.getView(R.id.rcv_car_go_type);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        CargoTypeSelectAdapter cargoTypeSelectAdapter = new CargoTypeSelectAdapter(dangerType, getContext(), PublishFragment.this, PublishFragment.this, PublishFragment.this);
                        recyclerView.setAdapter(cargoTypeSelectAdapter);

                    }
                }).setDimAmount(0.3f).setShowBottom(true).show(getChildFragmentManager());
                break;
            case R.id.ll_cargo_type:
                cargoTypeDialog.setLayoutId(R.layout.activity_cargo_type).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        RecyclerView recyclerView = holder.getView(R.id.rcv_car_go_type);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        CargoTypeSelectAdapter cargoTypeSelectAdapter = new CargoTypeSelectAdapter(cargoType, getContext(), PublishFragment.this, PublishFragment.this, PublishFragment.this);
                        recyclerView.setAdapter(cargoTypeSelectAdapter);

                    }
                }).setDimAmount(0.3f).setShowBottom(true).show(getChildFragmentManager());
                break;
            case R.id.ll_way_of_loading:
                cargoLoadDialog.setLayoutId(R.layout.activity_cargo_type).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        RecyclerView recyclerView = holder.getView(R.id.rcv_car_go_type);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        CargoTypeSelectAdapter cargoTypeSelectAdapter = new CargoTypeSelectAdapter(loadType, getContext(), PublishFragment.this, PublishFragment.this, PublishFragment.this);
                        recyclerView.setAdapter(cargoTypeSelectAdapter);

                    }
                }).setDimAmount(0.3f).setShowBottom(true).show(getChildFragmentManager());
                break;
            case R.id.ll_select_car:
                startActivity(new Intent(getContext(), CarSelectActivity.class));
                break;
            case R.id.ll_shipping_address:
                startActivityForResult(new Intent(getContext(), MyLocationDemoActivity.class), Location_map_s);
                break;
            case R.id.ll_receipt_address:
                startActivityForResult(new Intent(getContext(), MyLocationDemoActivity.class), Location_map_g);
                break;
            case R.id.ll_phone_code:
                startActivityForResult(new Intent(getContext(), ChoosePhoneCodeActivity.class), Location_phone);
                break;
            case R.id.ll_put_time:
                ComUtils.showDatePickerDialog(getActivity(), putTime);
                break;
            case  R.id.ll_back:
                getActivity().finish();
                break;
            case R.id.tv_btn:
                if (carSelect.size() == 0) {
                    ComUtils.showMsg(getContext(), "Please select car");
                    return;
                }
                if (null == addressS) {
                    ComUtils.showMsg(getContext(), "Please input shipping address.");
                    return;
                }
                if (null == addressG) {
                    ComUtils.showMsg(getContext(), "Please input the receipt address.");
                    return;
                }
                if (TextUtils.isEmpty(putName.getText().toString())) {
                    ComUtils.showMsg(getContext(), "Please enter the consignee's name.");
                    return;
                }
                if (null == phoneCodeBean) {
                    ComUtils.showMsg(getContext(), "Please select the phone's country code");
                    return;
                }
                if (TextUtils.isEmpty(putPhoneNum.getText().toString())) {
                    ComUtils.showMsg(getContext(), "Please enter the consignee's phone num.");
                    return;
                }
                if (putTime.getText().toString().contains("Please")) {
                    ComUtils.showMsg(getContext(), "Please enter the appointment Date.");
                    return;
                }
//                if(putTimeHour.getText().toString().contains("Please")){
//                    ComUtils.showMsg(getContext(),"Please enter the appointment time.");
//                    return;
//                }

                upOrder();

                break;
        }
    }

    private void upOrder() {
        String url = Constant.UP_ORDER;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("car_type", carSelects.toString());
        map.put("num", String.valueOf(carNum));

        map.put("lat", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getLatitude()))));
        map.put("longi", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getLongitude()))));
        map.put("country", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getCountryName()))));
        map.put("province", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getAdminArea()))));
        map.put("city", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getSubAdminArea()))));
        map.put("district", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getLocality()))));
        map.put("address", ComUtils.formatString(String.valueOf(addressS.getThoroughfare())) + "   " + addressDetail);

        map.put("recive_lat", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getLatitude()))));
        map.put("recive_longi", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getLongitude()))));
        map.put("recive_country", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getCountryName()))));
        map.put("recive_province", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getAdminArea()))));
        map.put("recive_city", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getSubAdminArea()))));
        map.put("recive_district", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getLocality()))));
        map.put("recive_address", ComUtils.formatString(String.valueOf(addressG.getThoroughfare())) + "   " + recAddressDetail);

        map.put("recive_mobile", phoneCodeBean.getD() + putPhoneNum.getText().toString());
        map.put("recive_name", putName.getText().toString());

        if (null == putTime.getTag()) {
            Toast.makeText(getContext(), "日期选择错误", Toast.LENGTH_SHORT).show();
            return;
        }
        map.put("start_time", putTime.getTag().toString());

        map.put("goods_kind", tvCargoType.getText().toString());
        map.put("goods_name", cargoName.getText().toString());
        map.put("goods_way", tvWayOfLoading.getText().toString());
        map.put("goods_weight", cargoWeight.getText().toString());
        map.put("goods_volume", cargoVolume.getText().toString());
        if (!TextUtils.isEmpty(cargoD.getText()) && !TextUtils.isEmpty(cargoW.getText()) && !TextUtils.isEmpty(cargoH.getText())) {
            map.put("goods_size", cargoD.getText().toString() + "*" + cargoW.getText().toString() + "*" + cargoH.getText().toString());
        }
        map.put("goods_pack", cargoWrappage.getText().toString());
        map.put("goods_require", etRequirements.getText().toString());
        if (!TextUtils.isEmpty(putTime.getTag().toString())) {
            map.put("goods_danger", putTime.getTag().toString().equals("是") ? "1" : "0");
        }

        new PostRequest("upInfo", getContext(), true)
                .go(getContext(), new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                orderSuccessDialog.setLayoutId(R.layout.to_order_detail).setConvertListener(new ViewConvertListener() {
                                    @Override
                                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                                        TextView tv_cancel = holder.getView(R.id.tv_cancel);
                                        TextView tv_sure = holder.getView(R.id.tv_sure);
                                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                orderSuccessDialog.cancelDialog();
                                            }
                                        });
                                        tv_sure.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent1 = new Intent(getActivity(), MyOrderActivity.class);
                                                startActivity(intent1);
                                                orderSuccessDialog.cancelDialog();
                                            }
                                        });

                                    }
                                })
                                        .setDimAmount(0.3f)
                                        .setMargin(30)
                                        .setShowBottom(false)
                                        .show(getChildFragmentManager());

                                carSelect.clear();
                                putChoice.setText("");
                                addressS = null;
                                addressG = null;
                                putShippingAddress.setText("");
                                putReceiptAddress.setText("");
                                putName.setText("");
                                putPhoneNum.setText("");
                                putTime.setText("");
                                putTime.setTag("");
                                tvCargoType.setText("");
                                etRequirements.setText("");
                                cargoName.setText("");
                                cargoWrappage.setText("");
                                cargoW.setText("");
                                cargoVolume.setText("");
                                cargoWeight.setText("");
                                cargoD.setText("");
                                cargoH.setText("");
                                tvCargoType.setText("");
                                tvIsDanger.setText("");
                                tvWayOfLoading.setText("");
                                EventBus.getDefault().post(new EventMessage(EventMessage.REFRESH_ORDER_HALL_DATA, ""));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(getContext(), "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(getContext(), "error");
                    }
                }, url, map);

    }

    @Override
    public void cargoTypeSelect(String name) {
        tvCargoType.setText(name);
        cargoTypeDialog.cancelDialog();
    }

    @Override
    public void cargoLoadSelect(String name) {
        tvWayOfLoading.setText(name);
        cargoLoadDialog.cancelDialog();
    }

    @Override
    public void cargoDangerSelect(String name) {
        tvIsDanger.setText(name);
        dangerDialog.cancelDialog();
    }

}
