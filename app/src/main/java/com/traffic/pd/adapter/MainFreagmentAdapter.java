package com.traffic.pd.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.traffic.pd.fragments.OrderHallFragment;
import com.traffic.pd.fragments.PublishFragment;
import com.traffic.pd.fragments.UserFragment;

import java.util.List;

public class MainFreagmentAdapter extends FragmentPagerAdapter {

    private FragmentManager mFm;
    List<Fragment> fragments;
    String tag;
    public MainFreagmentAdapter(FragmentManager fm, String tag, List<Fragment> fragments) {
        super(fm);
        this.tag = tag;
        mFm = fm;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int pos) {
        Fragment fragment = null;
        switch (pos){
            case 0:
                if(tag.equals("1")){
                    fragment = fragments.get(0);
                }
                if(tag.equals("2")){
                    fragment  = fragments.get(1);
                }
                if(tag.equals("3")){
                    fragment = fragments.get(2);
                }
                break;
            case 1:
                fragment = UserFragment.newInstance(tag,"");
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment f = (Fragment) super.instantiateItem(container, position);
        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
