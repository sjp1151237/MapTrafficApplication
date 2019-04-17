package com.traffic.pd.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout.LayoutParams;

import com.traffic.pd.R;

public class Loading extends Dialog {
	private View mView;
	private LayoutInflater mInflater;

	public Loading(Context context, int theme) {
		super(context,theme);
		mInflater = LayoutInflater.from(context);
		mView = mInflater.inflate(R.layout.loading, null);
		setContentView(mView);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		Window w = getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.width= LayoutParams.WRAP_CONTENT;
	}

	public void show(){
		super.show();
	}

	public void dismiss(){
		super.dismiss();
	}
}
