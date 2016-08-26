package com.javen.okhttpdemo;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by Javen on 2016/8/26.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("Javen");
    }
}
