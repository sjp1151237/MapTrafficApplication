package com.traffic.pd.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.traffic.pd.MainActivity;
import com.traffic.pd.R;
import com.traffic.pd.activity.CarTypeSelectActivity;
import com.traffic.pd.activity.ChoosePhoneCodeActivity;
import com.traffic.pd.constant.Constant;
import com.traffic.pd.data.CarType;
import com.traffic.pd.data.PhoneCodeBean;
import com.traffic.pd.data.TestBean;
import com.traffic.pd.maps.MyLocationDemoActivity;
import com.traffic.pd.utils.ComUtils;
import com.traffic.pd.utils.FrescoUtils;
import com.traffic.pd.utils.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DriversRegisterFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.car_type)
    TextView tvCarType;
    @BindView(R.id.et_introduce)
    EditText etIntroduce;
    private String mParam1;
    private String mParam2;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_district)
    TextView tvDistrict;
    @BindView(R.id.ll_select_car_location)
    LinearLayout llSelectCarLocation;
    @BindView(R.id.tv_address_detail)
    EditText tvAddressDetail;
    @BindView(R.id.et_car_license_num)
    EditText etCarLicenseNum;
    @BindView(R.id.iv_car_license_num)
    ImageView ivCarLicenseNum;
    @BindView(R.id.ll_cartype)
    LinearLayout llCartype;
    @BindView(R.id.ll_introduce)
    LinearLayout llIntroduce;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    Unbinder unbinder;
    @BindView(R.id.ll_location_phone)
    LinearLayout llLocationPhone;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.rcv_pic)
    RecyclerView rcvPic;
    Unbinder unbinder1;
    private View mView;

    EditText et_company_name, et_contacts;

    private static int Location_phone = 1001;
    private static int Location_map = 1002;

    PhoneCodeBean phoneCodeBean;

    String carLicenseImg;
    CarType carType;

    private RequestOptions requestOptions;
    List<LocalMedia> selectListImg;
    List<String> imgs;
    List<String> selectImgs;
    private int mWith;
    ImgAdapter imgAdapter;

    public static DriversRegisterFragment newInstance(String param1, String param2) {

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        DriversRegisterFragment fragment = new DriversRegisterFragment();
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
            if (mParam1.equals("2")) {
                mView = inflater.inflate(R.layout.activity_register_driver, container, false);
            }
            if (mParam1.equals("3")) {
                mView = inflater.inflate(R.layout.activity_register_company, container, false);
                et_contacts = mView.findViewById(R.id.et_contacts);
            }
            et_company_name = mView.findViewById(R.id.et_company_name);
            unbinder = ButterKnife.bind(this, mView);
            initGlide();
            selectListImg = new ArrayList<>();
            selectImgs = new ArrayList<>();
            imgs = new ArrayList<>();
            imgs.add("");
            WindowManager wm = getActivity().getWindowManager();
            mWith = wm.getDefaultDisplay().getWidth();
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
            rcvPic.setLayoutManager(layoutManager);
            imgAdapter = new ImgAdapter(imgs, selectListImg);
            rcvPic.setAdapter(imgAdapter);

            int withL = (mWith - ComUtils.dip2px(getContext(), 50)) / 4;
            int heightL = withL;

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivCarLicenseNum.getLayoutParams();
            lp.width = withL;
            lp.height = heightL;

            if(mParam1.equals("3")){
                if(null != MainActivity.companyInfo){
                    tvLocation.setText(MainActivity.companyInfo.getCode());
                    etPhoneNum.setText(MainActivity.companyInfo.getMobile());
                    et_company_name.setText(MainActivity.companyInfo.getName());
                    et_contacts.setText(MainActivity.companyInfo.getOwner());
                    tvCountry.setText(MainActivity.companyInfo.getCountry());
                    tvProvince.setText(MainActivity.companyInfo.getProvince());
                    tvCity.setText(MainActivity.companyInfo.getCity());
                    tvDistrict.setText(MainActivity.companyInfo.getDistrict());
                    tvAddressDetail.setText(MainActivity.companyInfo.getAddress());
                    etCarLicenseNum.setText(MainActivity.companyInfo.getLicense_num());
                    Drawable drawable = ComUtils.loadImageFromNetwork(MainActivity.companyInfo.getLicense_pic());
                    ivCarLicenseNum.setImageDrawable(drawable);

                    etIntroduce.setText(MainActivity.companyInfo.getIntroduce());
                    // 审核中
                    // 审核失败
                    // 审核成功 不可编辑
                    if(MainActivity.companyInfo.getStatus().equals("1") || MainActivity.companyInfo.getStatus().equals("3")){
                        imgs.clear();
                        imgs.add("");
                        if(null != MainActivity.companyInfo.getPics() && MainActivity.companyInfo.getPics().size() > 0){
                            imgs.addAll(imgs);
                        }
                        imgAdapter.notifyDataSetChanged();
                    }else{
                        imgs.clear();
                        if(null != MainActivity.companyInfo.getPics() && MainActivity.companyInfo.getPics().size() > 0){
                            imgs.addAll(imgs);
                        }
                        rcvPic.setEnabled(false);
                        imgAdapter.notifyDataSetChanged();
                    }
                    if(MainActivity.companyInfo.getStatus().equals("3")){

                        llLocationPhone.setEnabled(false);
                        llSelectCarLocation.setEnabled(false);
                        tvCommit.setVisibility(View.GONE);

                    }
                }else{
                    getActivity().finish();
                }
            }

            if(mParam1.equals("2")){
                if(null != MainActivity.carInfo){
                    tvCarType.setText(MainActivity.carInfo.getType());
                    tvLocation.setText(MainActivity.carInfo.getCode());
                    etPhoneNum.setText(MainActivity.carInfo.getMobile());
                    et_company_name.setText(MainActivity.carInfo.getDriver());
                    tvCountry.setText(MainActivity.carInfo.getCountry());
                    tvProvince.setText(MainActivity.carInfo.getProvince());
                    tvCity.setText(MainActivity.carInfo.getCity());
                    tvDistrict.setText(MainActivity.carInfo.getDistrict());
                    tvAddressDetail.setText(MainActivity.carInfo.getAddress());
                    etCarLicenseNum.setText(MainActivity.carInfo.getLicense_num());
                    Drawable drawable = ComUtils.loadImageFromNetwork(MainActivity.carInfo.getCar_num_pic());
                    ivCarLicenseNum.setImageDrawable(drawable);

                    etIntroduce.setText(MainActivity.carInfo.getIntroduce());
                    // 审核中
                    // 审核失败
                    // 审核成功 不可编辑
                    if(MainActivity.carInfo.getStatus().equals("1") || MainActivity.carInfo.getStatus().equals("3")){
                        imgs.clear();
                        imgs.add("");
                        if(null != MainActivity.carInfo.getCar_pic() && MainActivity.carInfo.getCar_pic().size() > 0){
                            imgs.addAll(imgs);
                        }
                        imgAdapter.notifyDataSetChanged();
                    }else{
                        imgs.clear();
                        if(null != MainActivity.carInfo.getCar_pic() && MainActivity.carInfo.getCar_pic().size() > 0){
                            imgs.addAll(imgs);
                        }
                        rcvPic.setEnabled(false);
                        imgAdapter.notifyDataSetChanged();
                    }
                    if(MainActivity.carInfo.getStatus().equals("2")){

                        llLocationPhone.setEnabled(false);
                        llSelectCarLocation.setEnabled(false);
                        tvCommit.setVisibility(View.GONE);

                    }
                }else{
                    getActivity().finish();
                }
            }

        }
        unbinder1 = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_location_phone, R.id.ll_select_car_location, R.id.ll_cartype, R.id.ll_introduce, R.id.tv_commit, R.id.iv_car_license_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_car_license_num:
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQUEST_CODE);
                    return;
                }
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                        .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .compressMaxKB(20)
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .isZoomAnim(false)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                        .enableCrop(false)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                        .isGif(false)// 是否显示gif图片 true or false
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                        .openClickSound(true)// 是否开启点击声音 true or false
                        .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.ll_location_phone:
                startActivityForResult(new Intent(getContext(), ChoosePhoneCodeActivity.class), Location_phone);
                break;
            case R.id.ll_select_car_location:
                startActivityForResult(new Intent(getContext(), MyLocationDemoActivity.class), Location_map);
                break;
            case R.id.ll_cartype:
                startActivityForResult(new Intent(getContext(), CarTypeSelectActivity.class), Location_phone);
                break;
            case R.id.ll_introduce:
                break;
            case R.id.tv_commit:
                if (null == phoneCodeBean) {
                    ComUtils.showMsg(getContext(), "Please select your phone country");
                    return;
                }
                if (TextUtils.isEmpty(etPhoneNum.getText().toString())) {
                    ComUtils.showMsg(getContext(), "Please enter phonenum");
                    return;
                }
//                if (null == address) {
//                    ComUtils.showMsg(getContext(), "Please select car location");
//                    return;
//                }
                if (TextUtils.isEmpty(etCarLicenseNum.getText().toString())) {
                    ComUtils.showMsg(getContext(), "Please input car license num");
                    return;
                }
                if (TextUtils.isEmpty(carLicenseImg)) {
                    ComUtils.showMsg(getContext(), "Please up car license picture");
                    return;
                }
                if (null == selectImgs || selectImgs.size() == 0) {
                    ComUtils.showMsg(getContext(), "Please up car pictures");
                    return;
                }
                if (mParam1.equals("2")) {
                    if(TextUtils.isEmpty(et_company_name.getText().toString())){
                        ComUtils.showMsg(getContext(), "Please input driver name");
                        return;
                    }
                    if (null == carType) {
                        ComUtils.showMsg(getContext(), "Please select car type");
                        return;
                    }
                    upDriverInfo();
                }
                if (mParam1.equals("3")) {
                    if(TextUtils.isEmpty(et_company_name.getText().toString())){
                        ComUtils.showMsg(getContext(), "Please input company name");
                        return;
                    }
                    if(TextUtils.isEmpty(et_contacts.getText().toString())){
                        ComUtils.showMsg(getContext(), "Please input contacts name");
                        return;
                    }
                    upCompanyInfo();
                }
                break;
        }
    }

    Address address;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        if (requestCode == Location_phone && resultCode == 2) {
            phoneCodeBean = (PhoneCodeBean) data.getSerializableExtra("res");
            tvLocation.setText(phoneCodeBean.getA() + "   " + phoneCodeBean.getD());
        }
        if (requestCode == Location_phone && resultCode == 3) {
            carType = (CarType) data.getSerializableExtra("car");
            tvCarType.setText(carType.getId());
        }
        if (requestCode == Location_map) {
            address = (Address) data.getSerializableExtra("address");
            if (null != address) {
                tvCountry.setText(ComUtils.formatString(address.getCountryName()));
                tvProvince.setText(ComUtils.formatString(address.getAdminArea()));
                tvCity.setText(ComUtils.formatString(address.getLocality()));
                tvDistrict.setText(ComUtils.formatString(address.getSubLocality()));
            }
        }
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    try {
                        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                        LocalMedia localMedia = selectList.get(0);
                        String path = localMedia.getCompressPath();
                        Glide.with(this).load(path).apply(requestOptions).into(ivCarLicenseNum);
                        setAvatar(path);
                        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                    } catch (Exception e) {
                        Log.i("1111111111", e.getMessage());
                    }
                    break;
                case PictureConfig.CHOOSE_REQUEST_RIGHT:
                    try {
                        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                        selectListImg.clear();
                        selectListImg.addAll(selectList);
                        imgs.clear();
                        imgs.add("");
                        for (int i = 0; i < selectList.size(); i++) {
                            imgs.add(selectList.get(i).getCompressPath());
                        }
                        imgAdapter.notifyDataSetChanged();
                        setAvatars(selectListImg);
                    } catch (Exception e) {
                        Log.i("1111111111", e.getMessage());
                    }
                    break;
            }
        }
    }

    private void initGlide() {
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.testimg);
        requestOptions.error(R.mipmap.testimg);
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    private void setAvatar(String path) {
        String url = Constant.UP_IMG;
        Map<String, String> map = new HashMap<>();
        map.put("dir", "driver");
        map.put("file", path);
        new PostRequest("setAvatar", getContext(), false).uploadFile(new PostRequest.PostListener() {
            @Override
            public TestBean postSuccessful(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("state");
                    if (status.equals("SUCCESS")) {
                        List<String> imgs = JSONArray.parseArray(jsonObject.getString("url"), String.class);
                        if (null != imgs && imgs.size() > 0) {
                            carLicenseImg = imgs.get(0);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void postError(String error) {
                super.postError(error);
            }

            @Override
            public void postNull() {
                super.postNull();
            }
        }, url, new File(path), map);
    }

    private void setAvatars(List<LocalMedia> imgs) {
        List<File> files = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < imgs.size(); i++) {
            files.add(new File(imgs.get(i).getCompressPath()));
            strings.add(imgs.get(i).getCompressPath());
        }
        String url = Constant.UP_IMG;
        Map<String, String> map = new HashMap<>();
        map.put("dir", "driver");
        map.put("file[]", strings.toString());
        new PostRequest("setAvatar", getContext(), true).uploadFiles(new PostRequest.PostListener() {
            @Override
            public TestBean postSuccessful(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    selectImgs.clear();
                    selectImgs.addAll(JSONArray.parseArray(jsonObject.getString("url"),String.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("tag", response);
                return null;
            }

            @Override
            public void postError(String error) {
                super.postError(error);
            }

            @Override
            public void postNull() {
                super.postNull();
            }
        }, url, files, map);
    }


    class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.MyViewHolder> {

        List<String> imgs;
        List<LocalMedia> selectListAl;

        public ImgAdapter(List<String> imgs, List<LocalMedia> selectListAl) {

            this.imgs = imgs;
            this.selectListAl = selectListAl;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View views = LayoutInflater.from(getActivity()).inflate(R.layout.pic_select_listview, parent, false);
            return new MyViewHolder(views);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            int withL = (mWith - ComUtils.dip2px(getContext(), 50)) / 4;
            int heightL = withL;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.iv_des.getLayoutParams();
            params.width = withL;
            params.height = heightL;

            if (imgs.get(position).equals("")) {
                Uri uri = Uri.parse("res:///" + R.mipmap.pic_add);
                FrescoUtils.showThumb(uri, holder.iv_des, withL, heightL);
            } else {
                Uri uri = Uri.parse("file://" + imgs.get(position));
                FrescoUtils.showThumb(uri, holder.iv_des, withL, heightL);
            }

            if (imgs.get(position).equals("")) {

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PictureSelector.create(getActivity())
                                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                                .maxSelectNum(6)// 最大图片选择数量 int
                                .minSelectNum(1)// 最小选择数量 int
                                .imageSpanCount(4)// 每行显示个数 int
                                .compressMaxKB(1024)
                                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                                .previewImage(true)// 是否可预览图片 true or false
                                .compressGrade(Luban.FIRST_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                                .isCamera(true)// 是否显示拍照按钮 true or false
                                .isZoomAnim(false)// 图片列表点击 缩放效果 默认true
                                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                                .enableCrop(false)// 是否裁剪 true or false
                                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                                .compress(true)// 是否压缩 true or false
                                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                                .isGif(false)// 是否显示gif图片 true or false
                                .selectionMedia(selectListImg)
                                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                                .openClickSound(true)// 是否开启点击声音 true or false
                                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                                .forResult(PictureConfig.CHOOSE_REQUEST_RIGHT);//结果回调onActivityResult code
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return imgs.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            SimpleDraweeView iv_des;

            public MyViewHolder(View itemView) {
                super(itemView);
                iv_des = (SimpleDraweeView) itemView.findViewById(R.id.iv_des);
            }
        }
    }


    private void upDriverInfo() {
        String url = Constant.DIVER_UPINFO;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("mobile", phoneCodeBean.getD() + etPhoneNum.getText().toString());
        map.put("driver", et_company_name.getText().toString());
        map.put("code", phoneCodeBean.getD());
//        if(null != address){
//            map.put("lat", String.valueOf(address.getLatitude()));
//            map.put("longi", String.valueOf(address.getLongitude()));
//            map.put("country", ComUtils.formatString(address.getCountryName()));
//            map.put("province", ComUtils.formatString(address.getAdminArea()));
//            map.put("city", ComUtils.formatString(address.getLocality()));
//            map.put("district", ComUtils.formatString(address.getSubLocality()));
//            map.put("address", tvAddressDetail.getText().toString());
//        }


        map.put("lat", "30");
        map.put("longi", "120");
        map.put("car_num", etCarLicenseNum.getText().toString());
        map.put("car_num_pic", carLicenseImg);
        map.put("car_pic", selectImgs.toString());
        map.put("type", carType.getId());
        map.put("introduce", etIntroduce.getText().toString());

        new PostRequest("upInfo", getContext(), true)
                .go(getContext(), new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {

                                ComUtils.showMsg(getContext(), "注册成功");
                                getActivity().finish();
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

    private void upCompanyInfo() {
        String url = Constant.COMPANY_UPINFO;
        Map<String, String> map = new HashMap<>();
        map.put("user_sign", MainActivity.userBean.getUser_id());
        map.put("mobile", phoneCodeBean.getD() + etPhoneNum.getText().toString());
//        if(null != address){
//            map.put("lat", String.valueOf(address.getLatitude()));
//            map.put("longi", String.valueOf(address.getLongitude()));
//            map.put("country", ComUtils.formatString(address.getCountryName()));
//            map.put("province", ComUtils.formatString(address.getAdminArea()));
//            map.put("city", ComUtils.formatString(address.getLocality()));
//            map.put("district", ComUtils.formatString(address.getSubLocality()));
//            map.put("address", tvAddressDetail.getText().toString());
//        }
        map.put("name", et_company_name.getText().toString());
        map.put("owner", et_contacts.getText().toString());
        map.put("lat", "35");
        map.put("longi", "136");
        map.put("license_num", etCarLicenseNum.getText().toString());
        map.put("license_pic", carLicenseImg);
        map.put("pics", selectImgs.toString());
        map.put("code", phoneCodeBean.getD());
        map.put("introduce", etIntroduce.getText().toString());

        new PostRequest("upInfo", getContext(), true)
                .go(getContext(), new PostRequest.PostListener() {
                    @Override
                    public TestBean postSuccessful(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {

                                ComUtils.showMsg(getContext(), "注册成功");
                                getActivity().finish();
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

}
