package com.javen.okhttpdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Javen
 * @since 2016/05/24
 */
public class ApkController {
    /**
     * 描述: 安装
     */
    public static boolean install(String apkPath,Context context){
        // 先判断手机是否有root权限
        if(hasRootPerssion()){
            // 有root权限，利用静默安装实现
            return clientInstall(apkPath);
        }else{
            // 没有root权限，利用意图进行安装
            File file = new File(apkPath);
            if(!file.exists())
                return false; 
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        }
    }
     
    /**
     * 描述: 卸载
     */
    public static boolean uninstall(String packageName,Context context){
        if(hasRootPerssion()){
            // 有root权限，利用静默卸载实现
            return clientUninstall(packageName);
        }else{
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
            return true;
        }
    }
     
    /**
     * 判断手机是否有root权限
     */
    private static boolean hasRootPerssion(){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();  
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }
     
    /**
     * 静默安装
     */
    private static boolean clientInstall(String apkPath){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 "+apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r "+apkPath);
//          PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();  
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }
     
    /**
     * 静默卸载
     */
    private static boolean clientUninstall(String packageName){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall "+packageName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();  
            return returnResult(value); 
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }
     
    /**
     * 启动app
     * com.exmaple.client/.MainActivity
     * com.exmaple.client/com.exmaple.client.MainActivity
     */
    public static boolean startApp(String packageName,String activityName){
        boolean isSuccess = false;
        String cmd = "am start -n " + packageName + "/" + activityName + " \n";
        Process process = null;
        try {
           process = Runtime.getRuntime().exec(cmd);
           int value = process.waitFor();  
           return returnResult(value);
        } catch (Exception e) {
          e.printStackTrace();
        } finally{
            if(process!=null){
                process.destroy();
            }
        }
        return isSuccess;
    }
     
     
    private static boolean returnResult(int value){
        // 代表成功  
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }  
    }
    
    /**
     * 查询所有非系统app的信息
	 * @param mContext
	 * @return
	 */
	public static List<Map<String, Object>> getAPPInstalled(Context mContext) {

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		// 获取系统内的所有程序信息
		Intent mainintent = new Intent(Intent.ACTION_MAIN, null);
		mainintent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<PackageInfo> packageinfo = mContext.getPackageManager()
				.getInstalledPackages(0);

		int count = packageinfo.size();
		for (int i = 0; i < count; i++) {

			PackageInfo pinfo = packageinfo.get(i);
			ApplicationInfo appInfo = pinfo.applicationInfo;
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// 系统程序 忽略
			} else {
				// 非系统程序
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("app_logo", pinfo.applicationInfo.loadIcon(mContext
						.getPackageManager()));
				map.put("app_name", pinfo.applicationInfo.loadLabel(mContext
						.getPackageManager()));
				map.put("package_name", pinfo.applicationInfo.packageName);
				map.put("app_version_name", pinfo.versionName);
				map.put("app_version_code", pinfo.versionCode);

				listItems.add(map);
			}
		}
		return listItems;
	}
	
	
	/**
	 * 判断应用是否需要安装
	 * @param mContext
	 * @param packageName
	 * @param versionCode
	 * @return
	 */
	public static boolean isInstalled(Context mContext,String packageName,int versionCode) {

		// 获取系统内的所有程序信息
		Intent mainintent = new Intent(Intent.ACTION_MAIN, null);
		mainintent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<PackageInfo> packageinfo = mContext.getPackageManager()
				.getInstalledPackages(0);

		int count = packageinfo.size();
		String pn;
		int vc;
		for (int i = 0; i < count; i++) {

			PackageInfo pinfo = packageinfo.get(i);
			ApplicationInfo appInfo = pinfo.applicationInfo;
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// 系统程序 忽略
			} else {
				// 非系统程序
				pn=pinfo.applicationInfo.packageName;
				vc=pinfo.versionCode;

				if (pn.equalsIgnoreCase(packageName) && vc >= versionCode) {
					return true;
				}
			}
		}
		return false;
	}

    /**
     * 卸载apk
     * @param context
     * @param packageName
     */
    public static void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }
}