package com.traffic.pd.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest extends StringRequest {


    String url;

    public BaseRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.url = url;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
//        if(url.equals(NetContant.HOST_TOUTIAO + "login")){
//            headers.put("authorization",NetContant.API_COMMON_KEY);
//        }else{
//            headers.put("authorization",PreferencesUtils.getSharePreStr(MyApplication.getInstance().getApplicationContext(), NetContant.API_KEY));
//        }
        return headers;
    }

}
