package com.traffic.pd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.activity.CarTypeSelectActivity;
import com.traffic.pd.activity.ChoosePhoneCodeActivity;

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
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.fragment_public, container, false);
        }
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.ll_select_car, R.id.ll_phone_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_car:
                startActivity(new Intent(getContext(), CarTypeSelectActivity.class));
                break;
            case R.id.ll_phone_code:
                startActivity(new Intent(getContext(), ChoosePhoneCodeActivity.class));
                break;
        }
    }
}
