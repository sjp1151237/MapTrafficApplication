package com.traffic.pd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.othershe.nicedialog.NiceDialog;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
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

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    NiceDialog niceDialog;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;

    private static int locCode = 1024;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tvTitle.setText("Sign in");
        tvBtn.setVisibility(View.VISIBLE);
        tvBtn.setText("Sign up");
        niceDialog = NiceDialog.init();
        phoneCodeBean = ComUtils.getCountryZipCodeLoc(this);
        if(null != phoneCodeBean){
            tvLocation.setText(phoneCodeBean.getA());
        }
    }

    @OnClick({R.id.ll_back, R.id.tv_btn, R.id.tv_commit,R.id.ll_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_location:
                startActivityForResult(new Intent(getContext(), ChoosePhoneCodeActivity.class), locCode);
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_btn:
                // 注册
                startActivity(new Intent(getContext(), RegisterActivity.class));
                finish();
                break;
            case R.id.tv_commit:
                if(null ==phoneCodeBean){
                    ComUtils.showMsg(getContext(), "please select phone country");
                    return;
                }
                if (TextUtils.isEmpty(etNum.getText().toString())) {
                    ComUtils.showMsg(getContext(), "please enter phone num");
                    return;
                }
                if (TextUtils.isEmpty(etPsw.getText().toString())) {
                    ComUtils.showMsg(getContext(), "please enter psw");
                    return;
                }
                toLogin(etNum.getText().toString(), etPsw.getText().toString());
                break;
        }
    }

    private void toLogin(final String username, String psw) {
        String url = Constant.USER_LOGIN;
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", psw);
        map.put("phonecode", phoneCodeBean.getD());
        new PostRequest("toLogin", getContext(), true)
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
                                UserBean userBean = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("result"), UserBean.class);
                                PreferencesUtils.putSharePre(getContext(), Constant.USER_INFO, jsonObject.getString("result"));
//                                if(userBean.getIdentity().equals("1")){
//                                    Intent intent = new Intent(getContext(),ConsigerHomeActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }else{
//                                    Intent intent = new Intent(getContext(),MainActivity.class);
//                                    intent.putExtra("user",userBean);
//                                    startActivity(intent);
//                                    finish();
//                                }
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.putExtra("user", userBean);
                                startActivity(intent);

                            }else{

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

    private Context getContext() {
        return LoginActivity.this;
    }

    PhoneCodeBean phoneCodeBean;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == locCode && resultCode == 2) {

            phoneCodeBean = (PhoneCodeBean) data.getSerializableExtra("res");
            tvLocation.setText(phoneCodeBean.getA());

        }

    }

}
