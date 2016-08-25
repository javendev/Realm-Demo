package com.javen.demo.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Javen on 2016/8/24.
 */
public class Dog extends RealmObject{
    @PrimaryKey
    public int id;
    public String name;
    public int type;

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
