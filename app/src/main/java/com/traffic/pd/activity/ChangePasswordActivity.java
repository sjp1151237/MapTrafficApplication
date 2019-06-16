package com.traffic.pd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChangePasswordActivity extends AppCompatActivity {

    String TAG = ChangePasswordActivity.class.getSimpleName();
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.et_phonenum)
    EditText etPhonenum;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.ll_code)
    LinearLayout llCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_regist)
    TextView btnRegist;

    private static int locCode = 1024;
    PhoneCodeBean phoneCodeBean;

    private Context context;
    private String smscode;

    private Handler handler;
    private Runnable runnable;
    private int runTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        context = this;
        phoneCodeBean = ComUtils.getCountryZipCodeLoc(this);

    }

    @OnClick({R.id.ll_back, R.id.tv_btn, R.id.ll_location, R.id.btn_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_btn:

                break;
            case R.id.ll_location:
                startActivityForResult(new Intent(context, ChoosePhoneCodeActivity.class), locCode);
                break;
            case R.id.btn_regist:
                toChange();
                break;
            case R.id.ll_code:
                if(tvCode.getText().toString().contains("获取验证码")){
                    getCode();
                }
                break;
        }
    }

    private void toChange() {
        if(null ==phoneCodeBean){
            ComUtils.showMsg(getContext(), "please select phone country");
            return;
        }
        if (TextUtils.isEmpty(etPhonenum.getText().toString())) {
            ComUtils.showMsg(getContext(), "please enter phone num");
            return;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            ComUtils.showMsg(getContext(), "please enter psw");
            return;
        }

        String url = Constant.USER_CHANGEPASS;
        Map<String, String> map = new HashMap<>();
        map.put("smscode", smscode);
        map.put("mobile", etPhonenum.getText().toString());
        map.put("phonecode", phoneCodeBean.getD());
        map.put("password", etPassword.getText().toString());
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
                                Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
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

    private void getCode() {
        if (null == phoneCodeBean || TextUtils.isEmpty(tvLocation.getText().toString())) {
            ComUtils.showMsg(getContext(), "Please select your phone country");
            return;
        }
        if (TextUtils.isEmpty(etPhonenum.getText().toString())) {
            ComUtils.showMsg(getContext(), "please enter phonenum");
            return;
        }
        startTimer();
        Map<String, String> map = new HashMap<>();
        map.put("phonecode", phoneCodeBean.getD());
        map.put("mobile",    etPhonenum.getText().toString());
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

                                Toast.makeText(getContext(),"验证码发送成功",Toast.LENGTH_SHORT).show();

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
                }, Constant.GET_MSG_CODE, map);

    }

    public void startTimer(){
        closeTimer();
        runnable = new Runnable() {

            @Override
            public void run() {
                handler.postDelayed(runnable, 1000);
                runTimer = runTimer-1;
                if (runTimer <= 0) {
                    closeTimer();
                    tvCode.setText("获取验证码");
                    tvCode.setTextColor(Color.RED);

                }else {
                    tvCode.setText("倒计时"+String.valueOf(runTimer)+"秒");
                    tvCode.setTextColor(getResources().getColor(R.color.item_content_font));
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void closeTimer(){
        if (runnable!=null) {
            handler.removeCallbacks(runnable);
        }
        runTimer = 90;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == locCode && resultCode == 2) {

            phoneCodeBean = (PhoneCodeBean) data.getSerializableExtra("res");
            tvLocation.setText(phoneCodeBean.getA());

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeTimer();
    }
}
