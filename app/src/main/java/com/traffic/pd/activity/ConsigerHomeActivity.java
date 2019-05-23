package com.traffic.pd.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.traffic.pd.R;
import com.traffic.pd.utils.ComUtils;

public class ConsigerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ComUtils.immerseHeadT(this, Color.TRANSPARENT);
        setContentView(R.layout.activity_consiger_home);
    }
}
