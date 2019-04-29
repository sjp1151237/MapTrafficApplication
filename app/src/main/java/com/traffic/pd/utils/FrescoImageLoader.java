package com.traffic.pd.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.traffic.pd.data.Carousel;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2017/8/17.
 */
public class FrescoImageLoader extends ImageLoader {

    private int image;
    public FrescoImageLoader(int image){
           this.image = image;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Carousel carousel = (Carousel)path;
        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(image);
//        requestOptions.error(image);
        Glide.with(context).load((int)carousel.getPicurl()).apply(requestOptions).into(imageView);
    }

    //提供createImageView 方法，方便fresco自定义ImageView
    @Override
    public ImageView createImageView(Context context) {
        ImageView simpleDraweeView = new ImageView(context);
        return simpleDraweeView;
    }
}
