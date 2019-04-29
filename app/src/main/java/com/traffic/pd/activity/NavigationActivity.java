package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.traffic.pd.R;
import com.traffic.pd.data.Carousel;
import com.traffic.pd.utils.FrescoImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity {

    @BindView(R.id.banner)
    Banner banner;
    List<Carousel> list;
    int[] resId = {R.mipmap.nv01,R.mipmap.nv02,R.mipmap.nv03,R.mipmap.nv04};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        list = new ArrayList<>();
        for (int i = 0; i < resId.length; i++) {
            Carousel carousel = new Carousel();
            carousel.setPicurl(resId[i]);
            carousel.setTitle("");
            list.add(carousel);
        }
        //设置图片加载器
        banner.setImageLoader(new FrescoImageLoader(R.mipmap.test0));
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        banner.update(list);
    }
}
