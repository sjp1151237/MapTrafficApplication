package com.traffic.pd.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.LoginActivity;
import com.traffic.pd.constant.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ComUtils {
    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//        View layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
//        TextView text = (TextView) layout.findViewById(R.id.message);
//        text.setText(msg);
//        Toast toast = new Toast(context);
//        toast.setGravity(Gravity.BOTTOM, 0, Utils.dip2px(context, 80));
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
//        toast.show();
    }

    public static void getLocationPermission(Activity context) {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public static final int REQUEST_CALL_PERMISSION = 10111;

    public static boolean checkReadPermission(Activity context) {
        boolean isget = false;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {//已有权限
            isget = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            isget = false;
        }
        return isget;
    }

    /**
     * 获取国家码
     */
    public static String getCountryZipCode(Context context) {
        String CountryID = "";
        String CountryZipCode = "";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CountryID = manager.getSimCountryIso().toUpperCase();
        Log.d("ss", "CountryID--->>>" + CountryID);
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String formatString(String res) {
        if (TextUtils.isEmpty(res)) {
            return "";
        } else {
            return res;
        }
    }

    /**
     * 日期选择
     *
     * @param activity
     * @param tv
     */
    public static void showDatePickerDialog(final Activity activity, final TextView tv) {
        Calendar calendar = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = year+"-" + monthOfYear+"-"+dayOfMonth;
                if(compareDate(date,getNowDate()) == 1){
                    tv.setText("You select ：" + year + "YEAR" + (monthOfYear + 1) + "MONTH" + dayOfMonth + "DAY");
                    try {
                        tv.setTag(dateToStamp(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    tv.setText("请选择今天或今天以后的日期");
                }
                // 此处得到选择的时间，可以进行你想要的操作
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     *
     * @param dateSelect
     * @param dateNow
     * @return
     */
    public static int compareDate(String dateSelect, String dateNow){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(dateSelect);
            Date dt2 = df.parse(dateNow);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return  df.format(new Date());// new Date()为获取当前系统时间
    }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 时间选择
     *
     * @param activity
     * @param tv
     */
    public static void showTimePickerDialog(Activity activity, final TextView tv) {
        Calendar calendar = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.setText("您选择了：" + hourOfDay + "时" + minute + "分");
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }

    public static void showData(Context context) {
        //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
        final Calendar calendar = Calendar.getInstance(Locale.US);//获取日期格式器对象
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override

            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                //修改日历控件的年，月，日

                //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致

                calendar.set(Calendar.YEAR, year);

                calendar.set(Calendar.MONTH, month);

                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public static Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable;
    }

    public static void showCallDialog(final Activity context, final String phone) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Tip");
        builder.setMessage("Are you call mobile " + phone);
        builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if(ComUtils.checkReadPermission(context)){
                    context.startActivity(intent);
                }
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
        builder.show();
    }

    public static String time2Date(Long timeLong){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(timeLong));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String judgeProvider(Activity activity,LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;//网络定位
        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;//GPS定位
        }else{
            Toast.makeText(activity,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static Location beginLocatioon(Activity activity) {
        String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
        //获得位置服务
        LocationManager locationManager = (LocationManager) activity.getSystemService(serviceString);
        String provider = judgeProvider(activity,locationManager);
        //有位置提供器的情况
        if (provider != null) {
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return locationManager.getLastKnownLocation(provider);
        }else{
            //不存在位置提供器的情况
            Toast.makeText(activity,"不存在位置提供器的情况",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static String formatDoubleThree(double lt){
        DecimalFormat df = new DecimalFormat("#.000");
        String str = df.format(lt);
        return str;

    }

}
