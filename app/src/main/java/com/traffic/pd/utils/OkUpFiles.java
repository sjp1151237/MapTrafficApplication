package com.traffic.pd.utils;

import java.io.File;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkUpFiles {

    /**
     * 通过上传的文件的完整路径生成RequestBody
     * @param fileNames 完整的文件路径
     * @return
     */
    private static RequestBody getRequestBody(List<String> fileNames) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
//        for (int i = 0; i < fileNames.size(); i++) { //对文件进行遍历
//            File file = new File(fileNames.get(i)); //生成文件
//            //根据文件的后缀名，获得文件类型
//            String fileType = getMimeType(file.getName());
//            builder.addFormDataPart( //给Builder添加上传的文件
//                    "image",  //请求的名字
//                    file.getName(), //文件的文字，服务器端用来解析的
//                    RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
//            );
//        }
        return builder.build(); //根据Builder创建请求
    }

    /**
     * 获得Request实例
     * @param url
     * @param fileNames 完整的文件路径
     * @return
     */
    private static Request getRequest(String url, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody(fileNames));
        return builder.build();
    }

    /**
     * 根据url，发送异步Post请求
     * @param url 提交到服务器的地址
     * @param fileNames 完整的上传的文件的路径名
     * @param callback OkHttp的回调接口
     */
    public static void upLoadFile(String url, List<String> fileNames, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(getRequest(url,fileNames)) ;
        call.enqueue(callback);
    }
}
