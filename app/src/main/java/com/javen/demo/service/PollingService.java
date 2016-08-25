package com.javen.demo.service;

import android.app.IntentService;
import android.content.Intent;

import com.javen.demo.entity.User;

import io.realm.Realm;

public class PollingService extends IntentService {
    public PollingService() {
        super("PollingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            //模拟数据请求
            Thread.sleep(1000*5);
            for (int i=100;i<150;i++){
                User user = realm.createObject(User.class);
                user.id=i;
                user.age=i;
                user.name="张三"+i;
                user.sessionId = 123456789;
            }
            realm.commitTransaction();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onHandleIntent(Intent intent) {
        Realm realm = Realm.getDefaultInstance();
        // go do some network calls/etc and get some data and stuff it into a 'json' var
        String json = customerApi.getCustomers();
        realm.beginTransaction();
        realm.createObjectFromJson(Customer.class, json); // Save a bunch of new Customer objects
        realm.commitTransaction();
        // At this point, the data in the UI thread is already up to date.
        // ...
    }
    // ...*/
}