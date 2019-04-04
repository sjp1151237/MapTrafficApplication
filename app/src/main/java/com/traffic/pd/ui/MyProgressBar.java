package com.traffic.pd.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.traffic.pd.R;


public class MyProgressBar extends LinearLayout {

	private int[] imgs = { R.mipmap.loading_01, R.mipmap.loading_02,
			R.mipmap.loading_03};

	private AnimationDrawable anim;
	private static final int DURATION = 500;// 帧动画每张图片持续时间（毫秒）
	private ImageView img;


	public MyProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		View root = LayoutInflater.from(context).inflate(
				R.layout.progress_view, null);
		img = (ImageView) root.findViewById(R.id.myProgressImage);


		anim = new AnimationDrawable();
		for (int i = 0; i < imgs.length; i++) {
			Drawable drawable = context.getResources().getDrawable(imgs[i]);
			anim.addFrame(drawable, DURATION);
		}
		// 设置帧动画是运行一次还是重复运行
		anim.setOneShot(false);
		img.setImageDrawable(anim);
		addView(root);
	}
	/**
	 * 开启进度动画
	 * 
	 * @param isShowProgressValue
	 *            true 显示进度文本
	 */
	public void start(boolean isShowProgressValue) {
		anim.start();
	}

	/**
	 * 停止进度
	 */
	public void stop() {
		anim.stop();
	}
}
