package com.traffic.pd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.adapter.HallListAdapter;

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
        }
        return mView;
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
}
