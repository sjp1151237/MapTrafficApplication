package com.traffic.pd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.adapter.HallListAdapter;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.data.UserBean;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.PostRequest;
import com.traffic.pd.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 接单大厅
 */
public class OrderHallFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tv_pos)
    TextView tvPos;
    @BindView(R.id.ll_pos)
    LinearLayout llPos;
    @BindView(R.id.rcv_hall_list)
    RecyclerView rcvHallList;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    private View mView;

    HallListAdapter hallListAdapter;

    int mPage,mSize;

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
            mView = inflater.inflate(R.layout.fragment_order_hall, container, false);
            unbinder = ButterKnife.bind(this, mView);
            hallListAdapter = new HallListAdapter(getContext());
            rcvHallList.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvHallList.setAdapter(hallListAdapter);
            mPage = 1;
            mSize = 100;
            loadData();
        }
        return mView;
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
                            ComUtils.showMsg(getContext(), msg);
                            if (status == 1) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != unbinder){
            unbinder.unbind();
        }
    }

    @OnClick(R.id.ll_pos)
    public void onViewClicked() {
    }


    public void refreshData(){
        mPage = 1;
        mSize = 100;
        loadData();
    }
}
