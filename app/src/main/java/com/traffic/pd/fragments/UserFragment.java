package com.traffic.pd.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.activity.LoginActivity;
import com.traffic.pd.activity.RegisterActivity;
import com.traffic.pd.constant.Constant;
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
import butterknife.Unbinder;

public class UserFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.take_picture)
    ImageView takePicture;
    @BindView(R.id.my_name)
    TextView myName;
    @BindView(R.id.tv_invite)
    TextView tvInvite;
    @BindView(R.id.rl_to_share)
    RelativeLayout rlToShare;
    @BindView(R.id.ll_order_center)
    LinearLayout llOrderCenter;
    @BindView(R.id.ll_driver)
    LinearLayout llDriver;
    @BindView(R.id.v_driver)
    View vDriver;
    @BindView(R.id.ll_Customer_Service)
    LinearLayout llCustomerService;
    @BindView(R.id.ll_Forgot_password)
    LinearLayout llForgotPassword;
    @BindView(R.id.ll_about_us)
    LinearLayout llAboutUs;
    @BindView(R.id.ll_Charge_standard)
    LinearLayout llChargeStandard;
    Unbinder unbinder;
    @BindView(R.id.tv_state)
    TextView tvState;

    private String mParam1;
    private String mParam2;

    private View mView;

    private MyReceiver myReceiver;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     *
     */
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.fragment_user, container, false);
            myReceiver = new MyReceiver();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constant.LOGIN_SUCESS);
            getActivity().registerReceiver(myReceiver, intentFilter);
        }
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(myReceiver);
    }

    @OnClick({R.id.take_picture, R.id.ll_order_center, R.id.ll_driver, R.id.ll_Customer_Service, R.id.ll_Forgot_password, R.id.ll_about_us, R.id.ll_Charge_standard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_picture:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_order_center:
                break;
            case R.id.ll_driver:
                break;
            case R.id.ll_Customer_Service:
                break;
            case R.id.ll_Forgot_password:
                break;
            case R.id.ll_about_us:
                break;
            case R.id.ll_Charge_standard:
                break;
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.REGIST_SUCESS)) {

//                toLogin(intent.getStringExtra("username"), intent.getStringExtra("psw"));

            }
            if (intent.getAction().equals(Constant.LOGIN_SUCESS)) {

                myName.setText(intent.getStringExtra("num"));

            }
        }
    }

}
