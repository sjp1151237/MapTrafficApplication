/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.traffic.pd.maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.traffic.pd.PermissionUtils;
import com.traffic.pd.R;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.services.AddressResultReceiver;
import com.traffic.pd.services.FetchAddressIntentService;
import com.traffic.pd.services.LongPressLocationSource;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.GoogleMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class MyLocationDemoActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMapManager.GetLoc, LongPressLocationSource.ResetLoc {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.tv_pos)
    TextView tvPos;
    @BindView(R.id.layout)
    FrameLayout layout;
    @BindView(R.id.tv_pos_2)
    EditText tvPos2;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    GoogleMapManager googleMapManager;

    private UiSettings mUiSettings;
    private AddressResultReceiver mResultReceiver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    Address address;
    LatLng latLng;

    Marker markerLongClick;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMessage(EventMessage eventMessage) {
        switch (eventMessage.getType()) {
            case EventMessage.TYPE_GET_LOCATION:
                try {
                    if (eventMessage.getObject() instanceof Address) {
                        address = (Address) eventMessage.getObject();
                        if (null != address) {
                            tvPos.setText(ComUtils.formatString(address.getCountryName()) + "   " + ComUtils.formatString(address.getAdminArea()) + "    " + ComUtils.formatString(address.getLocality()) + "    " + ComUtils.formatString(address.getSubLocality()));
                            tvPos2.setText(address.getThoroughfare() + " * " + ComUtils.formatString(address.getSubThoroughfare()));
                        }
                    }
                    if (eventMessage.getObject() instanceof LatLng) {
                        Locale locale = new Locale("", "");
                        address = new Address(locale);
                        address.setLatitude(latLng.latitude);
                        address.setLongitude(latLng.longitude);
                        latLng = (LatLng) eventMessage.getObject();
                        tvPos.setText("no address found");
                        if (null != latLng) {
                            tvPos2.setText(latLng.latitude + "    " + latLng.longitude);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location_demo);
        ButterKnife.bind(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        tvBtn.setText("sure");
        tvBtn.setVisibility(View.VISIBLE);
        tvTitle.setText("Location");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleMapManager = new GoogleMapManager(getApplicationContext(), this);
        mResultReceiver = new AddressResultReceiver(mHandler, this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (mMap != null) {
                    if (null != markerLongClick) {
                        markerLongClick.remove();
                    }
                    markerLongClick = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    address = null;
                    Intent intent = new Intent(MyLocationDemoActivity.this, FetchAddressIntentService.class);
                    intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
                    intent.putExtra(FetchAddressIntentService.LATLNG_DATA_EXTRA, latLng);
                    startService(intent);
                }
            }
        });
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

        googleMapManager.getMyLocation();

        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);

        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);

    }

    /**
     */
    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void getLoc(LatLng location) {
        Log.e("getLoc", "获取定位信息==========");
        if (null != location) {
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(location)
                    .build();

            resetLoc(location);
            address = null;

        }
    }

    @OnClick({R.id.ll_back, R.id.tv_title, R.id.tv_pos, R.id.tv_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_title:
                break;
            case R.id.tv_pos:
                break;
            case R.id.tv_btn:
                if (address != null && address.getCountryName() != null) {
                    Log.e("loc", address.toString());
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    if(!TextUtils.isEmpty(tvPos2.getText().toString())){
                        intent.putExtra("detail", tvPos2.getText().toString());
                    }
                    setResult(0, intent);
                    finish();
                } else {
                    ComUtils.showMsg(MyLocationDemoActivity.this, "获取定位信息失败");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void resetLoc(LatLng point) {
        Log.e("getLoc", "resetLoc 重新设置位置==========");
        if (null != markerLongClick) {
            markerLongClick.remove();
        }
        address = null;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.latitude, point.longitude), 16.0f));
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LATLNG_DATA_EXTRA, point);
        startService(intent);
    }

}
