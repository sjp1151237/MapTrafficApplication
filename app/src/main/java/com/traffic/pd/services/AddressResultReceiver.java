package com.traffic.pd.services;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class AddressResultReceiver extends ResultReceiver {
    private String mAddressOutput;
    Context context;
    Handler handler;

    public AddressResultReceiver(Handler handler, Context context) {
        super(handler);
        this.handler = handler;
        this.context = context;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        mAddressOutput = resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);
        if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
            Log.i("MapsActivity", "mAddressOutput-->" + mAddressOutput);
            new AlertDialog.Builder(context)
                    .setTitle("Position")
                    .setMessage(mAddressOutput)
                    .create()
                    .show();
        }

    }
}