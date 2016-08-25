package com.javen.demo;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Javen on 2016/8/23.
 */
public class RealmUtils {
    public static Realm getInstance(Context context){
        return Realm.getInstance(new RealmConfiguration.Builder(context).name("demo.realm").build());
    }
}
