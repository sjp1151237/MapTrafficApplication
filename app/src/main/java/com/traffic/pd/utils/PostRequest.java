package com.traffic.pd.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.traffic.pd.R;
import com.traffic.pd.app.MyApplication;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.ui.Loading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostRequest {

    private String TAG;
    private Context mContext;

    private boolean isLoading;
    private Loading mLoading;

    public static abstract class PostListener {
        public void postStart() {
        }

        public void postError(String error) {
        }

        public abstract TestBean postSuccessful(String response);

        public void postNull() {
        }

    }

    public PostRequest(String tag, Context context, boolean isLoading) {
        this.TAG = tag;
        mContext = context;
        try {
            if (context != null) {
                this.isLoading = isLoading;
                if (isLoading) {
                    mLoading = new Loading(context, R.style.Dialog);
                }
            }
        } catch (Exception e) {

        }
    }

    public void go(Context context, final PostListener postRequest, String url, final Map<String, String> map) {
        if(!NetUtil.isNetworkAvailable(context)){
            Toast.makeText(context,"请检查网络", Toast.LENGTH_SHORT).show();
            postRequest.postError("");
            return;
        }
        MyApplication.getInstance().cancelPendingRequests(TAG);
        Log.i(TAG, map.toString());
        postRequest.postStart();
        showLoading();
        StringRequest objectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dismissLoading();
                    if (response == null || response.length() == 0) {
                        postRequest.postNull();
                        return;
                    }
                    Log.i(TAG, response);
                    postRequest.postSuccessful(response);
                } catch (Exception e) {

                    Log.e("dfsd",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                        postRequest.postError(error.getMessage());
                    dismissLoading();
                } catch (Exception e) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };
        MyApplication.getInstance().addToRequestQueue(objectRequest, TAG);

    }

    private void showLoading() {
        if (isLoading && null != mLoading) {
            mLoading.show();
        }
    }

    private void dismissLoading() {
        if (isLoading && null != mLoading && mLoading.isShowing())
            mLoading.cancel();
    }

    public void  goCache(final PostListener postRequest, String url, final Map<String, String> map){
        MyApplication.getInstance().cancelPendingRequests(TAG);
        Log.i(TAG, map.toString());
        postRequest.postStart();
        showLoading();
        BaseRequest objectRequest = new BaseRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dismissLoading();
                    if (response == null || response.length() == 0) {
                        postRequest.postNull();
                        return;
                    }
                    Log.i(TAG, response);
                    postRequest.postSuccessful(response);
                } catch (Exception e) {

                    Log.e("dfsd",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    postRequest.postError(mContext.getString(R.string.net_error));
                    dismissLoading();
                } catch (Exception e) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };
        MyApplication.getInstance().addToRequestQueue(objectRequest, TAG);
    }

    public void uploadFile(final PostListener postRequest, String url, File file, final Map<String, String> map) {
        MultipartRequestUpload request = new MultipartRequestUpload(url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                postRequest.postError(error.getMessage());
            }
        }, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                PictureFileUtils.deleteCacheDirFile(mContext);
                postRequest.postSuccessful(response);
            }
        }, "file", file, map);
        MyApplication.getInstance().addToRequestQueue(request, TAG);
    }

    public void uploadFiles(final PostListener postRequest, String url, List<File> files, final Map<String, String> map) {
        MultipartRequestUpload request = new MultipartRequestUpload(url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                postRequest.postError(error.getMessage());
            }
        }, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                PictureFileUtils.deleteCacheDirFile(mContext);
                postRequest.postSuccessful(response);
            }
        }, "file", files, map);
        MyApplication.getInstance().addToRequestQueue(request, TAG);
    }


}
