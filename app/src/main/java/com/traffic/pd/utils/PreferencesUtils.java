package com.traffic.pd.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.traffic.pd.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PreferencesUtils {

    public static String getSharePreStr(Context context, String field) {
        String FILE = context.getResources().getString(R.string.app_dir);
        SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(FILE, 0);
        String s = sp.getString(field, "");//濠碘�鍊归悘澶屾嫚閵夈儳鎽熸繛鍫濈仛閻ュ懐锟介悷鎵畨闁稿﹦銆嬬槐婵嬪礆濞嗗繐绲块柛鎴濇惈閻⊙呯箔閿旇儻顪�
        return s;
    }

    public static int getSharePreInt(Context context, String field) {
        String FILE = context.getResources().getString(R.string.app_dir);
        SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(FILE, 0);
        int i = sp.getInt(field, 0);//濠碘�鍊归悘澶屾嫚閵夈儳鎽熸繛鍫濈仛閻ュ懐锟介悷鎵畨闁稿﹦銆嬬槐婵嬪礆濞嗗繐绲块柛鎴嫹
        return i;
    }

    public static boolean getSharePreBoolean(Context context, String field) {
        String FILE = context.getResources().getString(R.string.app_dir);
        SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(FILE, 0);
        boolean i = sp.getBoolean(field, false);//濠碘�鍊归悘澶屾嫚閵夈儳鎽熸繛鍫濈仛閻ュ懐锟介悷鎵畨闁稿﹦銆嬬槐婵嬪礆濞嗗繐绲块柛鎴嫹
        return i;
    }

    public static void putSharePre(Context context, String field, String value) {
        String FILE = context.getResources().getString(R.string.app_dir);
        SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(FILE, 0);
        sp.edit().putString(field, value).commit();
    }

    public static void putSharePre(Context context, String field, int value) {
        String FILE = context.getResources().getString(R.string.app_dir);
        SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(FILE, 0);
        sp.edit().putInt(field, value).commit();
    }

    public static void putSharePre(Context context, String field, Boolean value) {
        String FILE = context.getResources().getString(R.string.app_dir);
        SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(FILE, 0);
        sp.edit().putBoolean(field, value).commit();
    }

    public static void clear(Context context) {
        String FILE = context.getResources().getString(R.string.app_dir);
        SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(FILE, 0);
        sp.edit().clear().commit();
    }

    public static String isNull(JSONObject dataObject, String property) {
        String data = "";
        try {
            if (!dataObject.isNull(property)) {
                data = dataObject.getString(property);
            }
        } catch (JSONException e) {

        }
        return data;
    }

    public static int getInt(JSONObject dataObject, String property) {
        int data = 0;
        try {
            if (!dataObject.isNull(property)) {
                data = dataObject.getInt(property);
            }
        } catch (JSONException e) {
        }
        return data;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 格式化时间
     *
     * @param time 时间值
     * @return 时间
     */
    public static String formatTime(int time) {
        // TODO Auto-generated method stub
        if (time == 0) {
            return "00:00";
        }
        time = time / 1000;
        int m = time / 60 % 60;
        int s = time % 60;
        return (m > 9 ? m : "0" + m) + ":" + (s > 9 ? s : "0" + s);
    }

    public static String formatLongTime(long time) {
        // TODO Auto-generated method stub
        if (time == 0) {
            return "00:00";
        }
        int m = (int) (time / 1000 / 60);
        int s = (int) (time / 1000 % 60);
        return (m > 9 ? m : "0" + m) + ":" + (s > 9 ? s : "0" + s);
    }

}
