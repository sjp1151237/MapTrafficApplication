package com.traffic.pd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.traffic.pd.R;
import com.traffic.pd.data.CodeSelectTip;

import java.util.List;

public class CodeSelectTipAdapter extends RecyclerView.Adapter<CodeSelectTipAdapter.CodeSelectTipHolder> {
    List<CodeSelectTip> tipList;
    Context mContext;
    ClickTip clickTip;

    public CodeSelectTipAdapter(List<CodeSelectTip> tipList,Context mContext,ClickTip clickTip){
        this.mContext = mContext;
        this.tipList = tipList;
        this.clickTip = clickTip;
    }

    @NonNull
    @Override
    public CodeSelectTipHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.code_tip_item, viewGroup, false);
        return new CodeSelectTipHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeSelectTipHolder codeSelectTipHolder, final int i) {
        CodeSelectTip codeSelectTip = tipList.get(i);
        codeSelectTipHolder.tv_tip.setText(codeSelectTip.getTip());
        if(codeSelectTip.isSelect()){
            codeSelectTipHolder.tv_tip.setBackground(mContext.getResources().getDrawable(R.drawable.bg_code_select_shape));
        }else{
            codeSelectTipHolder.tv_tip.setBackground(null);
        }
        codeSelectTipHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTip.clickTip(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    class CodeSelectTipHolder extends RecyclerView.ViewHolder{

        TextView tv_tip;
        public CodeSelectTipHolder(@NonNull View itemView) {
            super(itemView);
            tv_tip = itemView.findViewById(R.id.tv_tip);
        }
    }

    public interface ClickTip{
        void clickTip(int pos);
    }
}
