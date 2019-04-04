package com.traffic.pd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.traffic.pd.R;
import com.traffic.pd.activity.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.take_picture)
    SimpleDraweeView takePicture;
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

    private String mParam1;
    private String mParam2;

    private View mView;

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
        }
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.take_picture, R.id.ll_order_center, R.id.ll_driver, R.id.ll_Customer_Service, R.id.ll_Forgot_password, R.id.ll_about_us, R.id.ll_Charge_standard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_picture:
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                intent.putExtra("tag",mParam1);
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
}
