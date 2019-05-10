package com.traffic.pd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.UserBean;
import com.traffic.pd.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity {

    int[] resId = {R.mipmap.nv01, R.mipmap.nv02, R.mipmap.nv03, R.mipmap.nv04};
    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    View view1, view2, view3;
    MyPagerAdapter myPagerAdapter;
    List<View> views;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(PreferencesUtils.getSharePreStr(this, "gudio"))) {
            setContentView(R.layout.activity_splash);
            mContext = this;
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toMain();
                }
            }, 3000);
        } else {
            PreferencesUtils.putSharePre(this, "gudio", "dfsdfsdfsdfsdfsfs");
            setContentView(R.layout.activity_navigation);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ButterKnife.bind(this);
            view1 = LayoutInflater.from(this).inflate(R.layout.img_only, null);
            ImageView img1 = view1.findViewById(R.id.iv_img_only);
            img1.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.nv01));
            view2 = LayoutInflater.from(this).inflate(R.layout.img_only, null);
            ImageView img2 = view2.findViewById(R.id.iv_img_only);
            img2.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.nv02));
            view3 = LayoutInflater.from(this).inflate(R.layout.img_only, null);
            ImageView img3 = view3.findViewById(R.id.iv_img_only);
            img3.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.nv03));

            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
                    finish();
                }
            });
            views = new ArrayList<>();
            views.add(view1);
            views.add(view2);
            views.add(view3);
            myPagerAdapter = new MyPagerAdapter(views);
            vpGuide.setAdapter(myPagerAdapter);
            iv1.setSelected(true);
            iv2.setSelected(false);
            iv3.setSelected(false);
            vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    switch (i) {
                        case 0:
                            iv1.setSelected(true);
                            iv2.setSelected(false);
                            iv3.setSelected(false);
                            break;
                        case 1:
                            iv1.setSelected(false);
                            iv2.setSelected(true);
                            iv3.setSelected(false);
                            break;
                        case 2:
                            iv1.setSelected(false);
                            iv2.setSelected(false);
                            iv3.setSelected(true);
                            break;
                    }

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        List<View> views;

        public MyPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

    }


    private void toMain() {
        if (TextUtils.isEmpty(PreferencesUtils.getSharePreStr(this, Constant.USER_INFO))) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            UserBean userBean = com.alibaba.fastjson.JSONObject.parseObject(PreferencesUtils.getSharePreStr(this, Constant.USER_INFO), UserBean.class);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", userBean);
            startActivity(intent);
            finish();
        }
    }
}
