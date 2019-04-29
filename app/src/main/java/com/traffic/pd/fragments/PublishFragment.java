package com.traffic.pd.fragments;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.MainActivity;
import com.traffic.pd.maps.MyLocationDemoActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.CarSelectActivity;
import com.traffic.pd.activity.ChoosePhoneCodeActivity;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.PhoneCodeBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PublishFragment extends Fragment {

    @BindView(R.id.put_choice)
    TextView putChoice;
    @BindView(R.id.ll_select_car)
    LinearLayout llSelectCar;
    @BindView(R.id.put_shipping_address)
    TextView putShippingAddress;
    @BindView(R.id.put_receipt_address)
    TextView putReceiptAddress;
    @BindView(R.id.put_name)
    EditText putName;
    @BindView(R.id.put_phone_num)
    EditText putPhoneNum;
    @BindView(R.id.put_time)
    TextView putTime;
    @BindView(R.id.btn_publish)
    Button btnPublish;
    Unbinder unbinder;
    @BindView(R.id.tv_cn)
    TextView tvCn;
    @BindView(R.id.ll_phone_code)
    LinearLayout llPhoneCode;
    @BindView(R.id.tv_phone_code)
    TextView tvPhoneCode;
    @BindView(R.id.put_time_hour)
    TextView putTimeHour;
    private View mView;
    private List<CarType> carSelect;

    private static int Location_phone = 1001;
    private static int Location_map_s = 1002;
    private static int Location_map_g = 1003;

    PhoneCodeBean phoneCodeBean;
    Address addressS, addressG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.fragment_public, container, false);
            unbinder = ButterKnife.bind(this, mView);
            carSelect = new ArrayList<>();
            EventBus.getDefault().register(this);
        }
        return mView;
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
                        carSelects= new StringBuilder();
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
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null == data){
            return;
        }
        if (requestCode == Location_phone && resultCode == 2) {
            phoneCodeBean = (PhoneCodeBean) data.getSerializableExtra("res");
            tvCn.setText(phoneCodeBean.getA());
            tvPhoneCode.setText("+" + phoneCodeBean.getD());
        }
        if (requestCode == Location_map_s) {
            addressS = (Address) data.getParcelableExtra("address");
            if (null != addressS) {
                String addresss = ComUtils.formatString(addressS.getCountryName()) + "  " + ComUtils.formatString(addressS.getAdminArea()) + "   " + ComUtils.formatString(addressS.getLocality()) + "   " + ComUtils.formatString(addressS.getSubLocality());
                putShippingAddress.setText(addresss);
            }
        }

        if (requestCode == Location_map_g) {
            addressG = (Address) data.getParcelableExtra("address");
            if (null != addressG) {
                String addresss = ComUtils.formatString(addressG.getCountryName()) + "  " + ComUtils.formatString(addressG.getAdminArea()) + "   " + ComUtils.formatString(addressG.getLocality()) + "   " + ComUtils.formatString(addressG.getSubLocality());
                putReceiptAddress.setText(addresss);
            }
        }
    }

    final Calendar calendar = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象

    @OnClick({R.id.ll_select_car, R.id.put_shipping_address, R.id.put_receipt_address, R.id.ll_phone_code, R.id.put_time, R.id.put_time_hour,R.id.btn_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_car:
                startActivity(new Intent(getContext(), CarSelectActivity.class));
                break;
            case R.id.put_shipping_address:
                startActivityForResult(new Intent(getContext(), MyLocationDemoActivity.class), Location_map_s);
                break;
            case R.id.put_receipt_address:
                startActivityForResult(new Intent(getContext(), MyLocationDemoActivity.class), Location_map_g);
                break;
            case R.id.ll_phone_code:
                startActivityForResult(new Intent(getContext(), ChoosePhoneCodeActivity.class), Location_phone);
                break;
            case R.id.put_time:
                ComUtils.showDatePickerDialog(getActivity(), putTime);
                break;
            case R.id.put_time_hour:
                ComUtils.showTimePickerDialog(getActivity(), putTimeHour);
                break;
            case R.id.btn_publish:
                if(carSelect.size() == 0){
                    ComUtils.showMsg(getContext(),"Please select car");
                    return;
                }

                if(null == addressS){
                    ComUtils.showMsg(getContext(),"Please input shipping address.");
                    return;
                }
                if(null == addressG){
                    ComUtils.showMsg(getContext(),"Please input the receipt address.");
                    return;
                }
                if(TextUtils.isEmpty(putName.getText().toString())){
                    ComUtils.showMsg(getContext(),"Please enter the consignee's name.");
                    return;
                }
                if(null == phoneCodeBean){
                    ComUtils.showMsg(getContext(),"Please select the phone's country code");
                    return;
                }
                if(TextUtils.isEmpty(putPhoneNum.getText().toString())){
                    ComUtils.showMsg(getContext(),"Please enter the consignee's phone num.");
                    return;
                }
                if(putTime.getText().toString().contains("Please")){
                    ComUtils.showMsg(getContext(),"Please enter the appointment Date.");
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
        map.put("long", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getLongitude()))));
        map.put("country", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getCountryName()))));
        map.put("province", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getAdminArea()))));
        map.put("city", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getLocality()))));
        map.put("district", String.valueOf(ComUtils.formatString(String.valueOf(addressS.getSubLocality()))));

        map.put("recive_lat", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getLatitude()))));
        map.put("recive_long", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getLongitude()))));
        map.put("recive_country", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getCountryName()))));
        map.put("recive_province", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getAdminArea()))));
        map.put("recive_city", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getLocality()))));
        map.put("recive_district", String.valueOf(ComUtils.formatString(String.valueOf(addressG.getSubLocality()))));

        map.put("recive_mobile", phoneCodeBean.getD() + putPhoneNum.getText().toString());
        map.put("recive_name", putName.getText().toString());

        map.put("start_time", putTime.getTag().toString());

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
                                ComUtils.showMsg(getContext(),"订单发布成功");

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



}
