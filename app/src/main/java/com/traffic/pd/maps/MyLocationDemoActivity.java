package com.traffic.pd.maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
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
import android.widget.ImageView;
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
 *
 */
public class MyLocationDemoActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMapManager.GetLoc, LongPressLocationSource.ResetLoc {

    /**
     *
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
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_sure)
    TextView tvSure;

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
                            tvPos.setText(address.getThoroughfare() + "   " + address.getLocality());
                            tvPos2.setText(address.getFeatureName());
                            tvCity.setVisibility(View.VISIBLE);
                            tvCity.setText(ComUtils.formatString(address.getSubAdminArea()));
//                            StringBuilder stringBuilder = new StringBuilder();
//                            stringBuilder.append("CountryName: ").append(address.getCountryName());//国家
//                              stringBuilder.append("    FeatureName: ").append(address.getFeatureName());//周边地址
//                              stringBuilder.append("    Locality: ").append(address.getLocality());//市
//                              stringBuilder.append("    PostalCode: ").append(address.getPostalCode());
//                             stringBuilder.append("    CountryCode: ").append(address.getCountryCode());//国家编码
//                              stringBuilder.append("    AdminArea: ").append(address.getAdminArea());//省份
//                              stringBuilder.append("    SubAdminArea: ").append(address.getSubAdminArea());// 市
//                              stringBuilder.append("    Thoroughfare: ").append(address.getThoroughfare());//道路
//                              stringBuilder.append("    SubLocality: ").append(address.getSubLocality());//香洲区
//                             stringBuilder.append("    Latitude: ").append(address.getLatitude());//经度
//                             stringBuilder.append("    Longitude: ").append(address.getLongitude());//维度
//                            tvPos2.setText(stringBuilder.toString());
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
        ComUtils.immerseHeadT(this, Color.TRANSPARENT);
        setContentView(R.layout.my_location_demo);
        ButterKnife.bind(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        tvBtn.setText("sure");
        tvBtn.setVisibility(View.VISIBLE);
        tvTitle.setText("Location");
        tvCity.setVisibility(View.GONE);
        tvPos.setText("详细地址加载中...");
        tvPos2.setText("");
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
        mUiSettings.setMyLocationButtonEnabled(false);
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
                    tvPos.setText("详细地址加载中...");
                    tvPos2.setText("");
                    tvCity.setVisibility(View.GONE);
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

        mUiSettings.setZoomControlsEnabled(false);
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

    @OnClick({R.id.ll_back, R.id.tv_title, R.id.tv_pos, R.id.tv_btn, R.id.iv_back, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_sure:
                if (address != null && address.getCountryName() != null) {
                    Log.e("loc", address.toString());
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    if (!TextUtils.isEmpty(tvPos2.getText().toString())) {
                        intent.putExtra("detail", tvPos2.getText().toString());
                    }
                    setResult(0, intent);
                    finish();
                } else {
                    ComUtils.showMsg(MyLocationDemoActivity.this, "获取定位信息失败");
                }
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_title:
                break;
            case R.id.tv_pos:
                break;
            case R.id.tv_btn:

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
