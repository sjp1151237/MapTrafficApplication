package com.traffic.pd.update;

import android.content.Context;

import com.traffic.pd.MainActivity;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * 更新、上传车辆当前位置
 */
public class UpdateCarLoction {

    public static void updateCarLoc(final Context context, String lat_n, String long_n) {
        if(null == MainActivity.carInfo){
            return;
        }
        String url = Constant.DIVER_EDIT;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("id", MainActivity.carInfo.getId());
        map.put("lat_n", lat_n);
        map.put("long_n", long_n);
        new PostRequest("updateCarLoc", context, false)
                .go(context, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status == 1) {

                            }
                            if (status == 0) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        ComUtils.showMsg(context, "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(context, "error");
                    }
                }, url, map);
    }

}
