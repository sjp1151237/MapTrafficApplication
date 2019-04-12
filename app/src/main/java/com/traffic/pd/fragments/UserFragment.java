package com.traffic.pd.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

import com.othershe.nicedialog.NiceDialog;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.LoginActivity;
import com.traffic.pd.activity.MyDriverActivity;
import com.traffic.pd.activity.UpDetailActivity;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PreferencesUtils;

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
    @BindView(R.id.ll_out_login)
    LinearLayout llOutLogin;
    Unbinder unbinder1;

    private String mParam1;
    private String mParam2;

    private View mView;

    private MyReceiver myReceiver;

    NiceDialog alertDialog;

    String[] userStatus = {"资料未上传"};

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
            unbinder = ButterKnife.bind(this, mView);
            llDriver.setVisibility(View.GONE);
            myReceiver = new MyReceiver();

            alertDialog = NiceDialog.init();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constant.LOGIN_SUCESS);
            getActivity().registerReceiver(myReceiver, intentFilter);

            if (null != MainActivity.userBean) {
                myName.setText(ComUtils.formatString(MainActivity.userBean.getNickname()));
            }

        }

        unbinder1 = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(myReceiver);
    }

    @OnClick({R.id.take_picture, R.id.ll_order_center, R.id.ll_driver, R.id.ll_Customer_Service, R.id.ll_Forgot_password, R.id.ll_about_us, R.id.ll_Charge_standard,R.id.ll_out_login,R.id.rl_to_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_picture:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_order_center:
                break;
            case R.id.ll_driver:
                startActivity(new Intent(getContext(), MyDriverActivity.class));
                break;
            case R.id.ll_Customer_Service:
                break;
            case R.id.ll_Forgot_password:
                break;
            case R.id.ll_about_us:
                break;
            case R.id.ll_Charge_standard:
                break;
            case R.id.ll_out_login:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("提醒");
                builder.setMessage("确定退出？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getActivity().finish();
                        startActivity(new Intent(getContext(),LoginActivity.class));

                        PreferencesUtils.putSharePre(getContext(),Constant.USER_INFO,"");
                        MainActivity.userBean = null;
                        MainActivity.companyInfo = null;


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
                builder.show();
                break;
            case R.id.rl_to_share:
                Intent intent1 = new Intent(getContext(), UpDetailActivity.class);
                intent1.putExtra("tag",mParam1);
                startActivity(intent1);
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

    public void resetData(){
        if(null != MainActivity.carInfo){
            llDriver.setVisibility(View.GONE);
            if(MainActivity.carInfo.getStatus().equals("1")){
                tvState.setText("审核中");
            }
            if(MainActivity.carInfo.getStatus().equals("3")){
                tvState.setText("审核失败:" + MainActivity.carInfo.getReson());
            }
            if(MainActivity.carInfo.getStatus().equals("2")){
                tvState.setText("审核通过");
            }
        }
        if(null != MainActivity.companyInfo){
            llDriver.setVisibility(View.VISIBLE);

            if(MainActivity.companyInfo.getStatus().equals("1")){
                tvState.setText("审核中");
            }
            if(MainActivity.companyInfo.getStatus().equals("3")){
                tvState.setText("审核失败:" + MainActivity.companyInfo.getReson());
            }
            if(MainActivity.companyInfo.getStatus().equals("2")){
                tvState.setText("审核通过");
            }
        }
    }

}
