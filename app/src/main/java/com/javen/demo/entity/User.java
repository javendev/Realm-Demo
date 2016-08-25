package com.javen.demo.entity;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Javen on 2016/8/22.
 */
public class User extends RealmObject {
    @PrimaryKey
    public int id;
    @Required
    public String name;//不为空
    public int age;
    @Ignore
    public int sessionId;//可忽略

    public String className;//添加的新字段


    public User() {
    }

    public User(int id, String name, int age, int sessionId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sessionId=" + sessionId +
                '}';
    }
}
