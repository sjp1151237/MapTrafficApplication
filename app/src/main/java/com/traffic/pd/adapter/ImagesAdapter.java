package com.traffic.pd.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.traffic.pd.R;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.FrescoUtils;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesHolder> {
    List<String> stringList;
    Context context;
    int mWith;
    public ImagesAdapter(List<String> stringList, Activity context){
        this.context = context;
        this.stringList = stringList;
        WindowManager wm = context.getWindowManager();
        mWith = wm.getDefaultDisplay().getWidth();
    }
    @NonNull
    @Override
    public ImagesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.images_item, viewGroup, false);
        return new ImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesHolder imagesHolder, int i) {
        int withL = (int) ((mWith - ComUtils.dip2px(context, 30)) * (3/4.00));

//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imagesHolder.iv_img.getLayoutParams();
//        params.width = withL;
        //        imagesHolder.iv_img.setImageURI(uri);
        Uri uri = Uri.parse(stringList.get(i));

        FrescoUtils.showThumb(uri, imagesHolder.iv_img, 300, 500);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class ImagesHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView iv_img;
        public ImagesHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
        }
    }
}
