package com.traffic.pd.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.traffic.pd.MainActivity;
import com.traffic.pd.PermissionUtils;
import com.traffic.pd.R;
import com.traffic.pd.activity.OrderDriversActivity;
import com.traffic.pd.adapter.OrderDriverAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CarInfo;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.services.LongPressLocationSource;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.GoogleMapManager;
import com.traffic.pd.utils.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CurrentCarsFragment extends Fragment implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMapManager.GetLoc, LongPressLocationSource.ResetLoc {
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.iv_to_order_list)
    ImageView ivToOrderList;
    @BindView(R.id.floatbutton)
    FloatingActionButton floatbutton;
    Unbinder unbinder;
    private View mView;
    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    GoogleMapManager googleMapManager;

    private UiSettings mUiSettings;

    List<OrderBean> orderBeanList;
    List<CarInfo> carInfoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.current_order_fragment, container, false);
            loadMyCurrentOrder();
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            googleMapManager = new GoogleMapManager(getContext(), this);
            orderBeanList = new ArrayList<>();
            carInfoList = new ArrayList<>();
        }
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    private void loadMyCurrentOrder() {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(false);
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

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

        loadData();
    }

    @Override
    public void resetLoc(LatLng point) {

    }

    @Override
    public void getLoc(LatLng location) {

    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     */
    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
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

    private void loadData() {
        if (null == MainActivity.userBean) {
            return;
        }
        String url = "";
        Map<String, String> map = new HashMap<>();
        if (MainActivity.userBean.getIdentity().equals("1")) {
            url = Constant.GET_ORDER_LIST;
            map.put("is_my", "1");
        } else {
            return;
        }
        map.put("status", "4");
        map.put("page", String.valueOf(1));
        map.put("size", String.valueOf(1000000));
        map.put("user_sign", MainActivity.userBean.getUser_id());
        new PostRequest("loadorder", getContext(), true)
                .go(getContext(), new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                orderBeanList.clear();
                                orderBeanList.addAll(JSONArray.parseArray(jsonObject.getString("data"), OrderBean.class));

                                if (orderBeanList.size() > 0) {
                                    tvOrder.setText("订单号：" + orderBeanList.get(0).getId());
                                    loadDrivers(orderBeanList.get(0).getId());

                                }else{
                                    tvOrder.setText("暂无进行中的订单");
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                    }
                }, url, map);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadDrivers(String orderId) {
        String url = Constant.ORDER_DTIVERS;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("order_id", orderId);
        new PostRequest("loadDriver", getContext(), true)
                .go(getContext(), new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                carInfoList.clear();
                                carInfoList.addAll(JSONArray.parseArray(jsonObject.getString("data"), CarInfo.class));
                                CarInfo carInfo1 = new CarInfo();
                                carInfo1.setLat_n("-8.65");
                                carInfo1.setLong_n("36.56");
                                CarInfo carInfo2 = new CarInfo();
                                carInfo2.setLat_n("-9.65");
                                carInfo2.setLong_n("40.56");
                                CarInfo carInfo3 = new CarInfo();
                                carInfo3.setLat_n("-12.65");
                                carInfo3.setLong_n("56.56");
                                CarInfo carInfo4 = new CarInfo();
                                carInfo4.setLat_n("-20.65");
                                carInfo4.setLong_n("54.56");
                                CarInfo carInfo5 = new CarInfo();
                                carInfo5.setLat_n("-25.65");
                                carInfo5.setLong_n("36.56");
                                CarInfo carInfo6 = new CarInfo();
                                carInfo6.setLat_n("-13.65");
                                carInfo6.setLong_n("45.56");
                                carInfoList.add(carInfo1);
                                carInfoList.add(carInfo2);
                                carInfoList.add(carInfo3);
                                carInfoList.add(carInfo4);
                                carInfoList.add(carInfo5);
                                carInfoList.add(carInfo6);

                                if(carInfoList.size() > 0){
                                    addMakers();
                                    tvOrder.setText(tvOrder.getText().toString() + "   车辆数：" + carInfoList.size());
                                }else{
                                    Toast.makeText(getContext(),"当前订单无车辆接单",Toast.LENGTH_SHORT).show();
                                }

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
                        ComUtils.showMsg(getContext(), "error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        ComUtils.showMsg(getContext(), "error");
                    }
                }, url, map);

    }

    private void addMakers() {
        if (null != carInfoList) {
            for (int i = 0; i < carInfoList.size(); i++) {
                try {
                    if(!TextUtils.isEmpty(carInfoList.get(i).getLat_n()) && !TextUtils.isEmpty(carInfoList.get(i).getLong_n())){
                        LatLng BRISBANE = new LatLng(Double.parseDouble(carInfoList.get(i).getLat_n()), Double.parseDouble(carInfoList.get(i).getLong_n()));
                        mMap.addMarker(new MarkerOptions()
                                .position(BRISBANE)
                                .title("坐标:")
                                .zIndex(i)
                                .snippet(BRISBANE.latitude + "  " + BRISBANE.longitude));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
