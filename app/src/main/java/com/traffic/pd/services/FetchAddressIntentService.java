package com.traffic.pd.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.traffic.pd.constant.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 */
public class FetchAddressIntentService extends IntentService {
    protected ResultReceiver mReceiver;
    private final String TAG = "FetchAddress";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.example.mylocationdemo";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LATLNG_DATA_EXTRA = PACKAGE_NAME +
            ".LATLNG_DATA_EXTRA";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FetchAddressIntentService(String name) {
        super(name);
    }

    public FetchAddressIntentService(){
        this("AddressIntentService");
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";

        // Get the location passed to this service through an extra.
        LatLng latLng = intent.getParcelableExtra(
                LATLNG_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(RECEIVER);

        List<Address> addresses = null;
        // 通过经纬度来获取地址，由于地址可能有多个，这和经纬度的精确度有关，本例限制最大返回数为5
        try {
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    5);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "service_not_available";
            Log.e(TAG,errorMessage);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "invalid_lat_long_used";
            Log.e(TAG,errorMessage);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
                    EventBus.getDefault().post(new EventMessage(EventMessage.TYPE_GET_LOCATION, latLng));
            if (errorMessage.isEmpty()) {
                errorMessage = "no_address_found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
//            ArrayList<String> addressFragments = new ArrayList<String>();
//
//            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//            }
//            Log.i(TAG, "address_found");
//            deliverResultToReceiver(SUCCESS_RESULT,
//                    TextUtils.join(System.getProperty("line.separator"),
//                            addressFragments));
            EventBus.getDefault().post(new EventMessage(EventMessage.TYPE_GET_LOCATION, address));
            /**
             * stringBuilder.append(address.getCountryName()).append("_");//国家
             *                 stringBuilder.append(address.getFeatureName()).append("_");//周边地址
             *                 stringBuilder.append(address.getLocality()).append("_");//市
             *                 stringBuilder.append(address.getPostalCode()).append("_");
             *                 stringBuilder.append(address.getCountryCode()).append("_");//国家编码
             *                 stringBuilder.append(address.getAdminArea()).append("_");//省份
             *                 stringBuilder.append(address.getSubAdminArea()).append("_");
             *                 stringBuilder.append(address.getThoroughfare()).append("_");//道路
             *                 stringBuilder.append(address.getSubLocality()).append("_");//香洲区
             *                 stringBuilder.append(address.getLatitude()).append("_");//经度
             *                 stringBuilder.append(address.getLongitude());//维度
             *                 /*System.out.println(stringBuilder.toString());
             *                 */
        }
    }
}
