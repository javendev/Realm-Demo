package com.javen.demo;

import com.orhanobut.logger.Logger;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by Javen on 2016/8/23.
 * 迁移 版本发送改变
 */
public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        Logger.e("oldVersion:"+oldVersion+"  newVersion:"+newVersion);
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0){
            schema.get("User")
                    .addField("className",String.class);
            oldVersion++;
        }
        if (oldVersion == 1){
            schema.create("Dog")
                    .addField("id", int.class, FieldAttribute.PRIMARY_KEY)
                    .addField("name", String.class)
                    .addField("type",int.class);
            oldVersion++;
        }
    }
}
