package com.traffic.pd.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.traffic.pd.MainActivity;
import com.traffic.pd.MainActivityDemo;
import com.traffic.pd.OnMapAndViewReadyListener;
import com.traffic.pd.PermissionUtils;
import com.traffic.pd.R;
import com.traffic.pd.activity.OrderDetailActivity;
import com.traffic.pd.adapter.OrderListAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.services.LongPressLocationSource;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.GoogleMapManager;
import com.traffic.pd.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 接单大厅
 */
public class OrderHallFragment extends Fragment implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMapManager.GetLoc, LongPressLocationSource.ResetLoc,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rcv_hall_list)
    RecyclerView rcvHallList;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    private View mView;

    OrderListAdapter hallListAdapter;

    int mPage,mSize;


    private GoogleMap mMap = null;

    private Marker mSelectedMarker;

    List<OrderBean> orderBeans;

    boolean isMapReady;
    boolean isDataLoad;
    boolean isAddMaker;

    private boolean mPermissionDenied = false;
    private UiSettings mUiSettings;
    private LongPressLocationSource mLocationSource;
    GoogleMapManager googleMapManager;

    MyReceive myReceive;

    public static OrderHallFragment newInstance(String param1, String param2) {

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        OrderHallFragment fragment = new OrderHallFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == mView) {
            Log.e("onCreateView","onCreateView");
            EventBus.getDefault().register(this);
            orderBeans = new ArrayList<>();
            mView = inflater.inflate(R.layout.fragment_order_hall, container, false);
            unbinder = ButterKnife.bind(this, mView);
            hallListAdapter = new OrderListAdapter(getContext(),orderBeans,"home");
            rcvHallList.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvHallList.setAdapter(hallListAdapter);
            mPage = 1;
            mSize = 100;
            isMapReady = false;
            isDataLoad = false;
            isAddMaker = false;

            loadData();

            mLocationSource = new LongPressLocationSource(this);
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if(null != mapFragment){
                new OnMapAndViewReadyListener(mapFragment, this);
            }
            googleMapManager = new GoogleMapManager(getContext(), this);

        }
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationSource.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationSource.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMessage(EventMessage eventMessage) {
        switch (eventMessage.getType()) {
            case EventMessage.REFRESH_ORDER_HALL_DATA:
                try {
                    refreshData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void loadDataR() {
        String[] lats = {"30","-21","-56","69","80","60","32","78"};
        String[] longitudes = {"-25.36","108.256","29.265","39.245","78.598","88.598","-25.36","-28.63"};
        orderBeans = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            OrderBean orderBean = new OrderBean();
            orderBean.setLat(lats[i]);
            orderBean.setLongi(longitudes[i]);
            orderBean.setCar_type("车型");
            orderBeans.add(orderBean);
        }
        isDataLoad = true;
        if(isMapReady && !isAddMaker){
            addMakers();
        }
    }

    private void loadData() {
        if(null == MainActivity.userBean){
            return;
        }
        String url = Constant.GET_ORDER_LIST;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("page", String.valueOf(mPage));
        map.put("size", String.valueOf(mSize));
        new PostRequest("loadData", getContext(), true)
                .go(getContext(), new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                isDataLoad = true;
                                orderBeans.clear();
                                orderBeans.addAll(JSONArray.parseArray(jsonObject.getString("data"),OrderBean.class));

                                hallListAdapter.notifyDataSetChanged();
                                if(!isAddMaker && isMapReady){
                                    addMakers();
                                }
                                Log.e("tag",response);
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
    String[] lats = {"30","-21","-56","69","80","60","32","78"};
    String[] longitudes = {"-25.36","108.256","29.265","39.245","78.598","88.598","-25.36","-28.63"};
    private void addMakers() {
        isAddMaker = true;

        if(null != orderBeans){

            for (int i = 0; i < orderBeans.size(); i++) {
                LatLng BRISBANE = new LatLng(Double.parseDouble(orderBeans.get(i).getLat()), Double.parseDouble(orderBeans.get(i).getLongi()));
//                LatLng BRISBANE = new LatLng(Double.parseDouble(lats[new Random().nextInt(6)]), Double.parseDouble(longitudes[new Random().nextInt(6)]));
                mMap.addMarker(new MarkerOptions()
                        .position(BRISBANE)
                        .title("Date:"+orderBeans.get(i).getStart_time())
                        .zIndex(i)
                        .snippet("To:" + orderBeans.get(i).getRecive_country() + "  "+ orderBeans.get(i).getProvince() + "  "+ orderBeans.get(i).getCity()));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != unbinder){
            unbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
    }

    public void refreshData(){
        mPage = 1;
        mSize = 100;
        loadData();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mSelectedMarker = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(mSelectedMarker)) {
            // The showing info window has already been closed - that's the first thing to happen
            // when any marker is clicked.
            // Return true to indicate we have consumed the event and that we do not want the
            // the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            mSelectedMarker = null;
            return true;
        }

        mSelectedMarker = marker;

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur.
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Hide the zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        mMap.setOnMapClickListener(this);

        mMap.setLocationSource(mLocationSource);
        isMapReady = true;

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        enableMyLocation();

        googleMapManager.getMyLocation();

        if(isDataLoad && !isAddMaker){
            addMakers();
        }
    }
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetLoc(LatLng point) {

    }

    @Override
    public void getLoc(LatLng location) {
        if (null != location) {
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(location)
                    .build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(MainActivity.userBean.getIdentity().equals("2")){
            if(null != MainActivity.carInfo && MainActivity.carInfo.getStatus().equals("2")){
                OrderBean orderBean = orderBeans.get((int) marker.getZIndex());
                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                intent.putExtra("info",orderBean);
                intent.putExtra("from", "home");
                startActivity(intent);
            }else{
                ComUtils.showMsg(getContext(),"审核通过才能接单");
            }
        }
        if(MainActivity.userBean.getIdentity().equals("3")){
            if(null != MainActivity.companyInfo && MainActivity.companyInfo.getStatus().equals("2")){
                OrderBean orderBean = orderBeans.get((int) marker.getZIndex());
                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                intent.putExtra("info",orderBean);
                intent.putExtra("from", "home");
                startActivity(intent);
            }else{
                ComUtils.showMsg(getContext(),"审核通过才能接单");
            }
        }

//        ComUtils.showMsg(getContext(),"这是第" + marker.getZIndex() + "  " + marker.getTitle() + "  " + marker.getId()+ "  " + marker.getSnippet()+ "  " + marker.getTag());
    }

    class MyReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

}
