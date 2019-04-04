package com.traffic.pd.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private final String TAG = "MyApplication";
    private com.android.volley.RequestQueue mRequestQueue;
    /**
     * 添加网络请求标识符
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setRetryPolicy(new DefaultRetryPolicy(10000,
                1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(false);
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        mRequestQueue.add(req);
    }


    /**
     * 撤销网络请求
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
