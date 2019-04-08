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

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.traffic.pd.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        tvBtn.setVisibility(View.VISIBLE);
        niceDialog = NiceDialog.init();

    }

    @OnClick({R.id.ll_back, R.id.tv_btn, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                break;
            case R.id.tv_btn:
                // 注册
                niceDialog.setLayoutId(R.layout.confirm_identity)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                // 生成view
                                LinearLayout ll_Consigner = holder.getView(R.id.ll_Consigner);
                                LinearLayout ll_Drivers = holder.getView(R.id.ll_Drivers);
                                LinearLayout ll_Company = holder.getView(R.id.ll_Company);

                                ll_Consigner.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                        intent.putExtra("tag", Constant.Val_Consigner);
                                        startActivity(intent);

                                        niceDialog.cancelDialog();
                                    }
                                });
                                ll_Drivers.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                        intent.putExtra("tag", Constant.Val_Drivers);
                                        startActivity(intent);
                                        finish();

                                        niceDialog.cancelDialog();
                                    }
                                });
                                ll_Company.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                        intent.putExtra("tag", Constant.Val_Company);
                                        startActivity(intent);
                                        finish();

                                        niceDialog.cancelDialog();
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true).show(getSupportFragmentManager());

                break;
            case R.id.tv_commit:
                if (TextUtils.isEmpty(etNum.getText().toString())) {
                    ComUtils.showMsg(getContext(), "please enter phone num");
                    return;
                }
                if (TextUtils.isEmpty(etPsw.getText().toString())) {
                    ComUtils.showMsg(getContext(), "please enter psw");
                    return;
                }
                toLogin(etNum.getText().toString(),etPsw.getText().toString());
                break;
        }
    }

    private void toLogin(final String username, String psw) {
        String url = Constant.USER_LOGIN;
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", psw);
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
                                PreferencesUtils.putSharePre(getContext(), Constant.USER_SIGN, String.valueOf(jsonObject.getInt("result")));
                                PreferencesUtils.putSharePre(getContext(), Constant.USER_NUM, username);
                                Intent intent = new Intent(Constant.LOGIN_SUCESS);
                                intent.putExtra("num",username);
                                sendBroadcast(intent);
                                finish();
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
}
