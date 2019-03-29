package com.traffic.pd.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.traffic.pd.MainActivity;

public class ComUtils {
    public static void showMsg(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
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
}
