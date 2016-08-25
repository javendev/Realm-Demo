package com.javen.demo.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.javen.demo.R;
import com.javen.demo.utils.L;
import com.javen.demo.utils.SDCardUtils;

import java.io.File;

public class ShutCutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shut_cut);
    }

    public void shortCut(View view){
        setUrlLogo("http://apk.qycould.com:9898/source/apk/nineapps.apk");
        deleteShortcut("http://apk.qycould.com:9898/source/apk/nineapps.apk");
        setUrlLogo("http://apk.qycould.com:9898/source/apk/nineapps.apk");
        setLauncherLogo();
        setLauncherLogo2();
        String path=SDCardUtils.getSDCardPath()+"Download"+File.separator+"nineapps.apk";
        L.e(path);
        setInstallLogo(path);
        Toast.makeText(this, "创建完成", Toast.LENGTH_LONG).show();
    }

    private void setLauncherLogo2(){
        Intent intent = new Intent(this,MainActivity.class);
        addShortcut(this,"启动",R.mipmap.ic_launcher,intent,false);
    }

    private void setInstallLogo(String path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
        addShortcut(this,"安装应用",R.mipmap.ic_launcher,intent,false);
    }


    private void setLauncherLogo(){
        //隐式
        Intent intent = new Intent("com.javen.test");
        addShortcut(this,"隐式启动",R.mipmap.ic_launcher,intent,false);
    }
    private void setUrlLogo(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        addShortcut(this,"打开网页",R.mipmap.ic_launcher,intent,false);
    }

    private void  deleteShortcut(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        deleteShortcut(this,"打开网页",R.mipmap.ic_launcher,intent,true);
    }

    /**
     * 创建快捷方式
     */
    public void addShortcut(Context context, String name, int iconRes,Intent actionIntent,boolean allowRepeat){
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //是否允许重复创建
        shortcutintent.putExtra("duplicate",allowRepeat);
        //快捷方式的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //设置快捷方式图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), iconRes);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //设置快捷方式动作
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
        //向系统发送广播
        sendBroadcast(shortcutintent);

    }

    public void deleteShortcut(Context context, String name, int iconRes,Intent actionIntent,boolean allowRepeat){
        Intent shortcutintent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //是否循环删除
        shortcutintent.putExtra("duplicate",allowRepeat);
        //快捷方式的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //设置快捷方式动作
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
        //向系统发送广播
        sendBroadcast(shortcutintent);
    }
}
