package com.traffic.pd.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class GoogleMapManager {
    public static final int UPDATE_INTERVAL = 5000;
    public static final int FASTEST_UPDATE_INTNERVAL = 2000;

    private static GoogleMapManager googleMapManager;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private LatLng myLocation;
    private Context mContext;
    GetLoc getLoc;

    private GoogleApiLinkListener mApiLinkListener;

    public interface GoogleApiLinkListener {
        void onConnected(@Nullable Bundle bundle);

        void onConnectionFailed(@NonNull ConnectionResult connectionResult);
    }


    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public GoogleMapManager(Context context,GetLoc getLocc){

        this.getLoc = getLocc;
        this.mContext = context;

    }

    public GoogleMapManager initGoogleApiClient(GoogleApiLinkListener apiLinkListener) {
        this.mApiLinkListener = apiLinkListener;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            if (mApiLinkListener == null) {
                                return;
                            }
                            mApiLinkListener.onConnected(bundle);
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            if (mApiLinkListener == null) {
                                return;
                            }
                            mApiLinkListener.onConnectionFailed(connectionResult);
                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        }

        return googleMapManager;
    }

    public GoogleMapManager initGoogleLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(FASTEST_UPDATE_INTNERVAL);
        }

        return googleMapManager;
    }

    // 该方法为获取location
    public LatLng getMyLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        } else {
// 这个地方不能直接获取location
// Task task = mFusedLocationProviderClient.getLastLocation();
// location = (Location) task.getResult();
// 参考链接：https://teamtreehouse.com/community/i-wanted-to-make-the-weather-app-detect-your-location
            LocationServices.getFusedLocationProviderClient(mContext)
                    .getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                getLoc.getLoc(myLocation);
                            }
                        }
                    });
        }

        return myLocation;
    }

    public void resetGoogleApiLinkListener() {
        mApiLinkListener = null;
    }

    public interface GetLoc{
        public void getLoc(LatLng location);
    }
}
