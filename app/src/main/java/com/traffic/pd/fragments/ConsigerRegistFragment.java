package com.traffic.pd.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ConsigerRegistFragment extends Fragment {

    private static String TAG = ConsigerRegistFragment.class.getSimpleName();
    View mView;
    @BindView(R.id.et_phonenum)
    EditText etPhonenum;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_two)
    EditText etPasswordTwo;
    Unbinder unbinder;
    @BindView(R.id.btn_regist)
    TextView btnRegist;
    Unbinder unbinder1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.activity_register_consigner, container, false);
            unbinder = ButterKnife.bind(this, mView);

        }

        unbinder1 = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_regist)
    public void onViewClicked() {
        if(TextUtils.isEmpty(etPhonenum.getText().toString())){
            ComUtils.showMsg(getContext(),"please enter phonenum");
            return;
        }

        if(TextUtils.isEmpty(etPassword.getText().toString())){
            ComUtils.showMsg(getContext(),"please enter password");
            return;
        }
        if(TextUtils.isEmpty(etPasswordTwo.getText().toString())){
            ComUtils.showMsg(getContext(),"please config password");
            return;
        }
        if(!etPasswordTwo.getText().toString().equals(etPassword.getText().toString())){
            ComUtils.showMsg(getContext(),"not same");
            return;
        }
        String url = Constant.USER_LOGIN;
        Map<String, String> map = new HashMap<>();
        map.put("password",etPassword.getText().toString());
        map.put("password",etPassword.getText().toString());
        new PostRequest(TAG, getContext(), false)
                .go(getContext(), new PostRequest.PostListener() {
            @Override
            public TestBean postSuccessful(String response) {
                Log.e(TAG,response);
                return null;
            }

            @Override
            public void postError(String error) {
                super.postError(error);
                Log.e(TAG,error);
            }

            @Override
            public void postNull() {
                super.postNull();
                Log.e(TAG,"null==========");
            }
        }, url, map);

    }
}
