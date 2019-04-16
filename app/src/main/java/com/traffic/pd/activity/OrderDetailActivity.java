package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.traffic.pd.MainActivity;
import com.traffic.pd.OnMapAndViewReadyListener;
import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.tv_send_phone)
    TextView tvSendPhone;
    @BindView(R.id.tv_get_phone)
    TextView tvGetPhone;
    @BindView(R.id.ll_car_detail)
    LinearLayout llCarDetail;
    OrderBean orderBean;

    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    @BindView(R.id.tv_add_cars)
    TextView tvAddCars;

    private GoogleMap mMap = null;

    /**
     * Keeps track of the selected marker.
     */
    private Marker mSelectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);

        tvBtn.setVisibility(View.VISIBLE);
        tvBtn.setText("ADD");
        orderBean = (OrderBean) getIntent().getSerializableExtra("info");
        tvAddCars.setText("已有" + orderBean.getGrab_num() + "辆车接单");
        if (null != orderBean) {
            tvGetPhone.setText(orderBean.getRecive_mobile());

            try {
                if (null != MainActivity.carTypeList) {
                    String[] cars = orderBean.getCar_type().split(",");

                    for (int i = 0; i < cars.length; i++) {
                        String[] carT = cars[i].split(":");
                        String carTypeId = carT[0];
                        String carTypeNum = carT[1];
                        for (int j = 0; j < MainActivity.carTypeList.size(); j++) {
                            if (MainActivity.carTypeList.get(j).getId().equals(carTypeId)) {
                                Log.e("OrderDetailActivity", "====================" + carTypeId);
                                CarType carType = MainActivity.carTypeList.get(i);
                                carType.setNum(Integer.parseInt(carTypeNum));
                                View view = LayoutInflater.from(this).inflate(R.layout.order_car_select_item, null);
                                TextView tv_weight, tv_volume, tv_capacity, tv_car_num;
                                tv_weight = view.findViewById(R.id.tv_weight);
                                tv_volume = view.findViewById(R.id.tv_volume);
                                tv_capacity = view.findViewById(R.id.tv_capacity);
                                tv_car_num = view.findViewById(R.id.tv_car_num);
                                tv_weight.setText(carType.getWeight());
                                tv_volume.setText(carType.getVolume());
                                tv_capacity.setText(carType.getCapacity());
                                tv_car_num.setText(carType.getNum() + "辆");
                                llCarDetail.addView(view);
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
//            BRISBANE = new LatLng(-27.47093, 153.0235);
//            MELBOURNE = new LatLng(-37.81319, 144.96298);
        }
    }

    @OnClick({R.id.ll_back, R.id.ll_car_detail,R.id.tv_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_car_detail:

                break;
            case R.id.tv_btn:
                if(MainActivity.userBean.getIdentity().equals("2")){
                    toAddOrder();
                }else{
                    toCars();
                }

                break;
        }
    }

    private void toCars() {
    }

    private void toAddOrder() {
        String url = Constant.GRAB_ORDER;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign",MainActivity.userBean.getUser_id());
        map.put("order_id",orderBean.getId());
        new PostRequest("toAddOrder", this, true)
                .go(this, new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status == 1) {

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
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Hide the zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();

        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        mMap.setOnMapClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localized.
        googleMap.setContentDescription("Demo showing how to close the info window when the currently"
                + " selected marker is re-tapped.");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(BRISBANE)
                .include(MELBOURNE)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
    }

    private void addMarkersToMap() {
        mMap.addMarker(new MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane")
                .snippet("Population: 2,074,200"));

        mMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("Melbourne")
                .snippet("Population: 4,137,400"));

    }

}
