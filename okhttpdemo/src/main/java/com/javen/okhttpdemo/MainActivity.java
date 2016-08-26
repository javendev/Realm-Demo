package com.javen.okhttpdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.javen.okhttpdemo.utils.ApkController;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    String url = "http://192.168.111.151:8080/android";
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image);

    }

    public void get(View veiw) {

        OkHttpUtils
                .get()
                .url(url)
                .addParams("username", "javen")
                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i("get response:"+response);
                    }
                });
    }

    public void post(View view){
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", "javen")
                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i("post response:"+response);
                    }
                });
    }

    public void postStr(View view){
        OkHttpUtils
                .postString()
                .url(url)
                .content(new Gson().toJson(new User("javen", "123")))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i("postStr response:"+response);
                    }
                });
    }

    public  void showImage(View view){
        OkHttpUtils
                .get()//
                .url("http://img.taopic.com/uploads/allimg/110316/292-110316121G647.jpg")//
                .build()//
                .execute(new BitmapCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap)
                    {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
    }

    public void downloadFile(View view){
        OkHttpUtils//
                .get()//
                .url("http://apk.qycould.com:9898/source/apk/nineapps.apk")//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "nineapps.apk")//
                {
                    @Override
                    public void inProgress(float progress)
                    {
                       Logger.i("下载进度："+progress);
                    }

                    @Override
                    public void onError(Request request, Exception e)
                    {
                       Logger.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(File file)
                    {
                        Logger.e("downloadFile onResponse :" + file.getAbsolutePath());
                        ApkController.install(file.getAbsolutePath(),MainActivity.this);
                    }
                });
    }
}
