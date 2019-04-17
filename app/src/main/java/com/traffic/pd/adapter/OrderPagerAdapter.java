package com.traffic.pd.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

public class OrderPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    String[] titles = {"all","pub","ing","over"};

    public OrderPagerAdapter(FragmentManager fm,List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int positon) {
        return mFragments.get(positon);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position <= titles.length - 1){
            return titles[position];
        }
        return "";
    }
}