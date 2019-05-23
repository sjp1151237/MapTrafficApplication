package com.traffic.pd.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.traffic.pd.MainActivity;
import com.traffic.pd.PermissionUtils;
import com.traffic.pd.R;
import com.traffic.pd.activity.OnlyUserActivity;
import com.traffic.pd.activity.PublishActivity;
import com.traffic.pd.adapter.MyOrderListAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.data.CarInfo;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.data.UserBean;
import com.traffic.pd.services.AddressResultReceiver;
import com.traffic.pd.services.FetchAddressIntentService;
import com.traffic.pd.services.LongPressLocationSource;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.GoogleMapManager;
import com.traffic.pd.utils.PostRequest;
import com.traffic.pd.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMapManager.GetLoc, LongPressLocationSource.ResetLoc,
        MyOrderListAdapter.ClickOrder {
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.iv_to_order_list)
    ImageView ivToOrderList;
    @BindView(R.id.floatbutton)
    FloatingActionButton floatbutton;
    Unbinder unbinder;
    @BindView(R.id.tv_loc_detail)
    TextView tvLocDetail;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private View mView;
    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    GoogleMapManager googleMapManager;

    private UiSettings mUiSettings;

    List<OrderBean> orderBeanList;
    List<CarInfo> carInfoList;

    NiceDialog niceDialogOrder;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    UserBean userBean;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.current_order_fragment, container, false);
            unbinder = ButterKnife.bind(this, mView);
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            googleMapManager = new GoogleMapManager(getContext(), this);
            orderBeanList = new ArrayList<>();
            carInfoList = new ArrayList<>();

            userBean  = com.alibaba.fastjson.JSONObject.parseObject(PreferencesUtils.getSharePreStr(getContext(), Constant.USER_INFO), UserBean.class);

            MainActivity.userBean = userBean;

            niceDialogOrder = NiceDialog.init();
            llBottom.setVisibility(View.GONE);
            mResultReceiver = new AddressResultReceiver(mHandler, getContext());
            ivToOrderList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (orderBeanList.size() > 0) {

                        niceDialogOrder.setLayoutId(R.layout.my_order_list_dialog).setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                                RecyclerView rcv_list = holder.getView(R.id.rcv_list);
                                rcv_list.setLayoutManager(new LinearLayoutManager(getContext()));
                                MyOrderListAdapter orderListAdapter = new MyOrderListAdapter(getContext(), orderBeanList, CurrentCarsFragment.this);
                                rcv_list.setAdapter(orderListAdapter);


                            }
                        }).setDimAmount(0.3f).setShowBottom(true).show(getChildFragmentManager());

                    }else{
                        if(!isLoadSuss){
                            loadData();
                        }
                    }

                }
            });
            floatbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), PublishActivity.class));
                }
            });
            mView.findViewById(R.id.iv_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), OnlyUserActivity.class));
                }
            });
            EventBus.getDefault().register(this);
        }
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
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
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
        if (null == userBean) {
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
        map.put("user_sign", userBean.getUser_id());
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
                                isLoadSuss = true;
                                orderBeanList.clear();
                                orderBeanList.addAll(JSONArray.parseArray(jsonObject.getString("data"), OrderBean.class));

                                if (orderBeanList.size() > 0) {
                                    tvOrder.setText("订单号：" + orderBeanList.get(0).getId());

                                    loadDrivers(orderBeanList.get(0).getId());
                                } else {
                                    tvOrder.setText("暂无进行中的订单");
                                }

                            }else{
                                isLoadSuss = true;
                                tvOrder.setText("订单信息加载失败，点击右边按钮重试");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        isLoadSuss = false;
                        tvOrder.setText("订单信息加载失败，点击右边按钮重试");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                    }
                }, url, map);
    }

    boolean isLoadSuss = true;

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

                                if (carInfoList.size() > 0) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(carInfoList.get(0).getLat_n()), Double.parseDouble(carInfoList.get(0).getLong_n())), 13.0f));
                                    addMakers();
                                    tvOrder.setText(tvOrder.getText().toString() + "   车辆数：" + carInfoList.size());
                                } else {
                                    Toast.makeText(getContext(), "当前订单无车辆接单", Toast.LENGTH_SHORT).show();
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
        mMap.clear();
        if (null != carInfoList) {
            for (int i = 0; i < carInfoList.size(); i++) {
                try {
                    if (!TextUtils.isEmpty(carInfoList.get(i).getLat_n()) && !TextUtils.isEmpty(carInfoList.get(i).getLong_n())) {
                        LatLng BRISBANE = new LatLng(Double.parseDouble(carInfoList.get(i).getLat_n()), Double.parseDouble(carInfoList.get(i).getLong_n()));
                        mMap.addMarker(new MarkerOptions()
                                .position(BRISBANE)
                                .title("车牌号:")
                                .zIndex(i)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_logo3))
                                .snippet(carInfoList.get(i).getCar_num()));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void clickOrder(int pos) {
        if (orderBeanList.size() >= (pos + 1) && pos != 0) {
            tvOrder.setText("订单号：" + orderBeanList.get(0).getId());
            niceDialogOrder.cancelDialog();
            loadDrivers(orderBeanList.get(pos).getId());
        }else{
            niceDialogOrder.cancelDialog();
        }
    }
    private AddressResultReceiver mResultReceiver;
    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMessage(EventMessage eventMessage) {
        switch (eventMessage.getType()) {
            case EventMessage.TYPE_GET_LOCATION:
                try {
                    if (eventMessage.getObject() instanceof Address) {
                        Address addressS = (Address) eventMessage.getObject();
                        String addresss = ComUtils.formatString(addressS.getCountryName()) + "  " + ComUtils.formatString(addressS.getAdminArea()) + "   " + ComUtils.formatString(addressS.getLocality()) + "   " + ComUtils.formatString(addressS.getSubLocality());
                        tvLocDetail.setText(addresss);
                    }
                    if (eventMessage.getObject() instanceof LatLng) {
                        LatLng latLng = (LatLng) eventMessage.getObject();
                        tvLocDetail.setText("经度：" +latLng.latitude + "纬度：" + latLng.longitude);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int pos = (int) marker.getZIndex();
        llBottom.setVisibility(View.VISIBLE);
        tvLocDetail.setText("位置信息加载中...");

        if(carInfoList.size() >= pos){
            try {
                CarInfo carInfo = carInfoList.get(pos);
                LatLng latLng = new LatLng(Double.parseDouble(carInfo.getLat_n()), Double.parseDouble(carInfo.getLong_n()));
                Intent intent = new Intent(getContext(), FetchAddressIntentService.class);
                intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
                intent.putExtra(FetchAddressIntentService.LATLNG_DATA_EXTRA, latLng);
                getActivity().startService(intent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        llBottom.setVisibility(View.GONE);
    }
}
