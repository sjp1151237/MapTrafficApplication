package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.traffic.pd.OnMapAndViewReadyListener;
import com.traffic.pd.R;
import com.traffic.pd.utils.ComUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderLocActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener{

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_btn)
    TextView tvBtn;

    private GoogleMap mMap = null;
    private Marker mSelectedMarker;

    private LatLng BRISBANE;
    private LatLng MELBOURNE;

    double lat,logni,reclat,reclong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_loc);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);
        try{
            if(null != getIntent().getStringExtra("begin_lat")){
                lat = Double.parseDouble(getIntent().getStringExtra("begin_lat"));
            }else{
                finish();
                Toast.makeText(this,"坐标错误",Toast.LENGTH_SHORT).show();
            }
            if(null != getIntent().getStringExtra("begin_long")){
                logni =  Double.parseDouble(getIntent().getStringExtra("begin_long"));
            }else{
                finish();
                Toast.makeText(this,"坐标错误",Toast.LENGTH_SHORT).show();
            }
            if(null != getIntent().getStringExtra("over_lat")){
                reclat = Double.parseDouble(getIntent().getStringExtra("over_lat"));
            }else{
                finish();
                Toast.makeText(this,"坐标错误",Toast.LENGTH_SHORT).show();
            }
            if(null != getIntent().getStringExtra("over_long")){
                reclong = Double.parseDouble(getIntent().getStringExtra("over_long"));
            }else{
                finish();
                Toast.makeText(this,"坐标错误",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception i){
            finish();
            Toast.makeText(this,"坐标错误",Toast.LENGTH_SHORT).show();
        }
        tvTitle.setText("订单位置信息");
        initMaker();
    }

    private void initMaker() {
        BRISBANE = new LatLng(lat, logni);
        MELBOURNE = new LatLng(reclat, reclong);
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mSelectedMarker = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(mSelectedMarker)) {
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
                .title("起点")
                .snippet("Point: " + lat +"," + logni));

        mMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("终点")
                .snippet("Point: " + reclat +","+ reclong));

    }
}
