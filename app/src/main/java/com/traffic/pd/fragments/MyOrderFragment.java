package com.traffic.pd.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.adapter.OrderListAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.constant.EventMessage;
import com.traffic.pd.data.OrderBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.utils.ComUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyOrderFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rcvList;
    TextView tvLoading;
    SwipeRefreshLayout srl;

    private String mParam1;
    private String mParam2;

    private View mView;
    List<OrderBean> orderBeans;
    OrderListAdapter hallListAdapter;

    public static MyOrderFragment newInstance(String param1, String param2) {

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        MyOrderFragment fragment = new MyOrderFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null == mView) {
            EventBus.getDefault().register(this);
            mView = inflater.inflate(R.layout.my_order_fragment, container, false);
            rcvList = mView.findViewById(R.id.rcv_list);
            tvLoading = mView.findViewById(R.id.tv_loading);
            srl = mView.findViewById(R.id.srl);

            orderBeans = new ArrayList<>();
            hallListAdapter = new OrderListAdapter(getContext(), orderBeans,"user",mParam1);
            srl.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    rcvList.setVisibility(View.GONE);
                    tvLoading.setVisibility(View.VISIBLE);
                    tvLoading.setText("loading...");
                    orderBeans.clear();
                    hallListAdapter.notifyDataSetChanged();
                    loadData();
                }
            });
            rcvList.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvList.setAdapter(hallListAdapter);
            rcvList.setVisibility(View.GONE);
            tvLoading.setVisibility(View.VISIBLE);
            loadData();
        }
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void loadData() {
        if (null == MainActivity.userBean) {
            return;
        }
        String url = "";
        Map<String, String> map = new HashMap<>();
        if(MainActivity.userBean.getIdentity().equals("1")){
            url = Constant.GET_ORDER_LIST;
            map.put("is_my", "1");
        }else{
            url = Constant.GET_ORDER_GRAB_LIST;
        }
        map.put("status", mParam1);
        map.put("page", String.valueOf(1));
        map.put("size", String.valueOf(1000000));
        map.put("user_sign", MainActivity.userBean.getUser_id());
        new PostRequest("loadData" + mParam1, getContext(), false)
                .go(getContext(), new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {
                        Log.e("MyOrderFragment","参数 ==== "+ mParam1 +"       "+ response);
                        srl.setRefreshing(false);
                        rcvList.setVisibility(View.VISIBLE);
                        tvLoading.setVisibility(View.GONE);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                orderBeans.clear();
                                orderBeans.addAll(JSONArray.parseArray(jsonObject.getString("data"), OrderBean.class));

                                hallListAdapter.notifyDataSetChanged();
//                                if(!isAddMaker && isMapReady){
//                                    addMakers();
//                                }
                                Log.e("tag", response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void postError(String error) {
                        super.postError(error);
                        srl.setRefreshing(false);
                        rcvList.setVisibility(View.GONE);
                        tvLoading.setVisibility(View.VISIBLE);
                        tvLoading.setText("load error");
                    }

                    @Override
                    public void postNull() {
                        super.postNull();
                        srl.setRefreshing(false);
                        rcvList.setVisibility(View.GONE);
                        tvLoading.setVisibility(View.VISIBLE);
                        tvLoading.setText("load error");
                    }
                }, url, map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMessage(EventMessage eventMessage) {
        switch (eventMessage.getType()) {
            case EventMessage.REFRESH_ORDER_HALL_DATA:
                try {
                    loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
