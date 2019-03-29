package com.traffic.pd.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.traffic.pd.R;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.utils.PreferencesUtils;

public class HomeActivity extends AppCompatActivity {
    String tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        tag = getIntent().getStringExtra("tag");
        if(TextUtils.isEmpty(tag)){
            if(!TextUtils.isEmpty(PreferencesUtils.getSharePreStr(this, Constant.Identity_Name))){
                tag = PreferencesUtils.getSharePreStr(this, Constant.Identity_Name);
            }
        }

        if(tag.equals(Constant.Val_Company)){

        }
        if(tag.equals(Constant.Val_Consigner)){

        }
        if(tag.equals(Constant.Val_Drivers)){

        }
    }


}
