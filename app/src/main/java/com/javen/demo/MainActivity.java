package com.javen.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.javen.demo.entity.User;
import com.javen.demo.service.PollingService;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    Realm myRealm;
    RealmChangeListener changeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        myRealm = RealmUtils.getInstance(this);
        //获取默认配置
        myRealm = Realm.getDefaultInstance();
    }

    public  void save1(View view){
        //主键Id不能自动增长 默认值为0
        myRealm.beginTransaction();
        User user = myRealm.createObject(User.class);
        user.age=18;
        user.name="Javen";
        user.sessionId = 123456789;
        myRealm.commitTransaction();
    }
    public void save2(View view){
        //主键Id不能自动增长
        User user = new User();
        user.id=2;
        user.age=20;
        user.name="张三";
        user.sessionId = 789456;

        myRealm.beginTransaction();
        myRealm.copyToRealm(user);
        myRealm.commitTransaction();
    }
    public void save3(View view){
       myRealm.executeTransaction(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {
               User user = realm.createObject(User.class);
               user.id=3;
               user.age=19;
               user.name="李四";
               user.sessionId = 123456789;
           }
       });
    }

    public void saveOrUpdate(View view){
        final User user = new User(1,"nm",12,852);

        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // This will create a new object in Realm or throw an exception if the
                // object already exists (same primary key)
                // realm.copyToRealm(obj);

                // This will update an existing object with the same primary key
                // or create a new object if an object with no primary key = 1
                realm.copyToRealmOrUpdate(user);
            }
        });
    }

    public  void select(View view){
        //查询所有
        RealmResults<User> users = myRealm.where(User.class).findAll();
        for (User user:users) {
            Logger.i("查询所有:"+user.toString());
        }
        //查询年龄大于18的用户 并根据来年龄升序
        users = myRealm.where(User.class).greaterThan("age", 12).findAllSorted("id", Sort.ASCENDING);
        for (User user:users) {
            Logger.i("查询年龄大于18的用户:"+user.toString());
        }

        //查询年龄等于20 的用户
        users = myRealm.where(User.class).equalTo("age",20).findAll();
        for (User user:users) {
            Logger.i("查询年龄等于20 的用户:"+user.toString());
        }
    }

    //通知
    public void findAllAsync(View view){
        RealmResults<User> users = myRealm.where(User.class).findAllAsync();
        changeListener = new RealmChangeListener<RealmResults<User>>() {
            @Override
            public void onChange(RealmResults<User> results) {
                // This is called anytime the Realm database changes on any thread.
                // Please note, change listeners only work on Looper threads.
                // For non-looper threads, you manually have to use Realm.waitForChange() instead.
                Logger.i("数据改变："+results.size());
            }
        };
        users.addChangeListener(changeListener);

        //添加数据
        startService(new Intent(this, PollingService.class));

    }

    public void getPath(View view){
        //获取文件保存的路径
        String path = myRealm.getPath();
        Logger.e(path);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the listener.
        myRealm.removeChangeListener(changeListener);
        // Remove all listener.
        myRealm.removeAllChangeListeners();
        // Close the Realm instance.
        myRealm.close();
    }
}
