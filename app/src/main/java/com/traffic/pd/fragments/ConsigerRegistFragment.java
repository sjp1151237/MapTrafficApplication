package com.traffic.pd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.ChoosePhoneCodeActivity;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.PhoneCodeBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.data.UserBean;
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
    @BindView(R.id.tv_location)
    EditText tvLocation;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;

    private static int locCode = 1024;
    @BindView(R.id.rb_consiger)
    RadioButton rbConsiger;
    @BindView(R.id.rb_driver)
    RadioButton rbDriver;
    @BindView(R.id.rb_company)
    RadioButton rbCompany;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    Unbinder unbinder2;
    @BindView(R.id.et_username)
    EditText etUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.activity_register_consigner, container, false);
            unbinder = ButterKnife.bind(this, mView);
        }
        unbinder2 = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_location, R.id.btn_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_location:
                startActivityForResult(new Intent(getContext(), ChoosePhoneCodeActivity.class), locCode);
                break;
            case R.id.btn_regist:
                if(TextUtils.isEmpty(etUsername.getText().toString())){
                    ComUtils.showMsg(getContext(),"please enter user name");
                    return;
                }
                if (null == phoneCodeBean || TextUtils.isEmpty(tvLocation.getText().toString())) {
                    ComUtils.showMsg(getContext(), "Please select your phone country");
                    return;
                }
                if (TextUtils.isEmpty(etPhonenum.getText().toString())) {
                    ComUtils.showMsg(getContext(), "please enter phonenum");
                    return;
                }

                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    ComUtils.showMsg(getContext(), "please enter password");
                    return;
                }
                if (TextUtils.isEmpty(etPasswordTwo.getText().toString())) {
                    ComUtils.showMsg(getContext(), "please config password");
                    return;
                }
                if (!etPasswordTwo.getText().toString().equals(etPassword.getText().toString())) {
                    ComUtils.showMsg(getContext(), "not same");
                    return;
                }
                int type = 0;
                switch (rgMain.getCheckedRadioButtonId()) {
                    case R.id.rb_consiger:
                        type = 1;
                        break;
                    case R.id.rb_driver:
                        type = 2;
                        break;
                    case R.id.rb_company:
                        type = 3;
                        break;
                }
                String url = Constant.USER_REGISTER;
                Map<String, String> map = new HashMap<>();
                map.put("mobile", etPhonenum.getText().toString());
                map.put("password", etPassword.getText().toString());
                map.put("password2", etPasswordTwo.getText().toString());
                map.put("country", phoneCodeBean.getD());
                map.put("username", etUsername.getText().toString());
                map.put("identity", String.valueOf(type));
                new PostRequest(TAG, getContext(), false)
                        .go(getContext(), new PostRequest.PostListener() {
                            @Override
                            public TestBean postSuccessful(String response) {

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    int status = jsonObject.getInt("status");
                                    String msg = jsonObject.getString("msg");
                                    ComUtils.showMsg(getContext(), msg);
                                    if (status == 1) {
                                        UserBean userBean = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("data"),UserBean.class);
                                        PreferencesUtils.putSharePre(getContext(),Constant.USER_INFO,jsonObject.getString("data"));
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        intent.putExtra("user",userBean);
                                        startActivity(intent);
                                        getActivity().finish();
//                                        intent.setAction(Constant.REGIST_SUCESS);
//                                        intent.putExtra("username",phoneCodeBean.getD() + etPhonenum.getText().toString());
//                                        intent.putExtra("psw",etPassword.getText().toString());
//                                        getActivity().sendBroadcast(intent);
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
                break;
        }
    }

    PhoneCodeBean phoneCodeBean;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == locCode && resultCode == 2) {

            phoneCodeBean = (PhoneCodeBean) data.getSerializableExtra("res");
            tvLocation.setText(phoneCodeBean.getA() + "   " + phoneCodeBean.getD());

        }

    }
}
