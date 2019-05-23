package com.traffic.pd.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.services.AlarmReceiver;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnlyUserActivity extends AppCompatActivity {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.take_picture)
    ImageView takePicture;
    @BindView(R.id.my_name)
    TextView myName;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_invite)
    TextView tvInvite;
    @BindView(R.id.rl_to_share)
    RelativeLayout rlToShare;
    @BindView(R.id.ll_order_center)
    LinearLayout llOrderCenter;
    @BindView(R.id.ll_out_login)
    LinearLayout llOutLogin;
    private FragmentManager supportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_user);
        ButterKnife.bind(this);

        if (null != MainActivity.userBean) {
            myName.setText(ComUtils.formatString(MainActivity.userBean.getNickname()));
        }

    }

    @OnClick({R.id.ll_order_center, R.id.ll_out_login,R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_order_center:
                Intent intent1 = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_out_login:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("提醒");
                builder.setMessage("确定退出？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlarmManager manager = (AlarmManager) getActivity()
                                .getSystemService(Context.ALARM_SERVICE);
                        Intent intent1 = new Intent(getActivity(), AlarmReceiver.class);
                        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, intent1, 0);
                        //取消正在执行的服务
                        manager.cancel(pi);

                        getActivity().finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));

                        PreferencesUtils.putSharePre(getActivity(), Constant.USER_INFO, "");
                        MainActivity.userBean = null;
                        MainActivity.companyInfo = null;
                        MainActivity.carInfo = null;

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
                builder.show();
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private Activity getActivity() {
        return this;
    }
}
