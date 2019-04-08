package com.traffic.pd.fragments;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.MyLocationDemoActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.ChoosePhoneCodeActivity;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.LocationBean;
import com.traffic.pd.data.PhoneCodeBean;
import com.traffic.pd.utils.ComUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DriversRegisterFragment extends Fragment {

    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.et_psw_conf)
    EditText etPswConf;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_district)
    TextView tvDistrict;
    @BindView(R.id.ll_select_car_location)
    LinearLayout llSelectCarLocation;
    @BindView(R.id.tv_address_detail)
    EditText tvAddressDetail;
    @BindView(R.id.et_car_license_num)
    EditText etCarLicenseNum;
    @BindView(R.id.iv_car_license_num)
    ImageView ivCarLicenseNum;
    @BindView(R.id.iv_car_pics)
    ImageView ivCarPics;
    @BindView(R.id.ll_cartype)
    LinearLayout llCartype;
    @BindView(R.id.ll_introduce)
    LinearLayout llIntroduce;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    Unbinder unbinder;
    @BindView(R.id.ll_location_phone)
    LinearLayout llLocationPhone;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    private View mView;

    private static int Location_phone = 1001;
    private static int Location_map = 1002;

    PhoneCodeBean phoneCodeBean;
    LocationBean address;

    String carLicenseImg;
    String[] carImgs;
    CarType carType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.activity_register_driver, container, false);
            unbinder = ButterKnife.bind(this, mView);
        }
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_location_phone, R.id.ll_select_car_location, R.id.ll_cartype, R.id.ll_introduce, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_location_phone:
                startActivityForResult(new Intent(getContext(), ChoosePhoneCodeActivity.class), Location_phone);
                break;
            case R.id.ll_select_car_location:
                startActivity(new Intent(getContext(), MyLocationDemoActivity.class));
                break;
            case R.id.ll_cartype:
                break;
            case R.id.ll_introduce:
                break;
            case R.id.tv_commit:
                if (null == phoneCodeBean) {
                    ComUtils.showMsg(getContext(),"Please select your phone country");
                    return;
                }
                if(TextUtils.isEmpty(etPhoneNum.getText().toString())){
                    ComUtils.showMsg(getContext(),"Please enter phonenum");
                    return;
                }
                if(TextUtils.isEmpty(etPsw.getText().toString())){
                    ComUtils.showMsg(getContext(),"Please enter psw");
                    return;
                }
                if(TextUtils.isEmpty(etPswConf.getText().toString())){
                    ComUtils.showMsg(getContext(),"Please enter psw again");
                    return;
                }
                if(!etPsw.getText().toString().equals(etPswConf.getText().toString())){
                    ComUtils.showMsg(getContext(),"The passwords are different");
                    return;
                }
                if(null == address){
                    ComUtils.showMsg(getContext(),"Please select car location");
                    return;
                }
                if(TextUtils.isEmpty(etCarLicenseNum.getText().toString())){
                    ComUtils.showMsg(getContext(),"Please input car license num");
                    return;
                }
                if(TextUtils.isEmpty(carLicenseImg)){
                    ComUtils.showMsg(getContext(),"Please up car license picture");
                    return;
                }
                if(null == carImgs || carImgs.length == 0){
                    ComUtils.showMsg(getContext(),"Please up car pictures");
                }
                if(null == carType){
                    ComUtils.showMsg(getContext(),"Please select car type");
                }


                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Location_phone && resultCode == 2) {

            phoneCodeBean = (PhoneCodeBean) data.getSerializableExtra("res");
            tvLocation.setText(phoneCodeBean.getA() + "   " + phoneCodeBean.getD());

        }
    }
}
