package com.javen.demo;

import android.app.Application;

import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Javen on 2016/8/23.
 */
public class App extends Application{
    RealmConfiguration config;
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("Javen");

        if (Common.IS_DEBUG){
//            //加密
//            byte[] key = new byte[64];
//            new SecureRandom().nextBytes(key);

            config = new RealmConfiguration
                    .Builder(this)
//                    .encryptionKey(key)
                    .name("demo.realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
        }else {
            // The Realm file will be located in Context.getFilesDir() with name "default.realm"
            config = new RealmConfiguration
                    .Builder(this)
                    .schemaVersion(2)
                    .migration(new MyMigration())
                    .name("demo.realm")
                    .build();
        }
        Realm.setDefaultConfiguration(config);
    }
}
