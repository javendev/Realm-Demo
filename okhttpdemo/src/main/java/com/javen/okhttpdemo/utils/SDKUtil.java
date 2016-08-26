package com.javen.okhttpdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Javen
 *
 */
public class SDKUtil {
	private final static String TAG=SDKUtil.class.getSimpleName();
	
	
	/**
	 * 读取某个文件夹中的所有Apk文件路径并打开安装页面
	 * @param context
	 * @param path
	 */
	 public static void readApkAndStart(Context context, String path){
	    	List<String> listpath = readAllApkForPath(context, path);
	    	if (listpath!=null && listpath.size()>0) {
	    		for (String string : listpath) {
	        		SDKUtil.openInstallView(context, string);
	    		}
			}else {
				Log.e("xxxx-----", "readApkAndStart null。。。。。。。");
			}
	 }
	 /**
     * 读取某文件夹下的所有apk文件
     * @param context
     * @param path
     * @return
     */
    public static List<String> readAllApkForPath(Context context,String path){
    	List<String> fileNameList=new ArrayList<String>();
    	File dir;
    	if (Parameter.isDebug) {
    		 dir = getSDir(context, path);
		}else {
			 dir = getDiskCacheDir(context, path);
		}
    	
    	if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				String filePath = file.getAbsolutePath();
				if (filePath.endsWith(".apk")) {
					fileNameList.add(filePath);
				}
			}
			return fileNameList;
		}
		return null;
    }
    
    
    /**
     * 获取SD卡跟目录中的某个文件
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getSDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment
                .getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String path = null;
        if (externalStorageAvailable) {
        	path=Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return new File(path + File.separator + uniqueName);
    }
    
    /**
     * 获取缓存地址
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment
                .getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
           
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        Log.i(TAG, cachePath);
        return new File(cachePath + File.separator + uniqueName);
    }
    
    /**
     * 弹出安装界面
     * @param context 
     * @param pathString apk 路径
     */
    public static void openInstallView(Context context,String pathString){
    	//启动安装界面
//		Intent install = new Intent(Intent.ACTION_VIEW);
//		install.setDataAndType(Uri.fromFile(new File(pathString)),
//				"application/vnd.android.package-archive");
//		install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(install);
    	ApkController.install(pathString, context);
    }

}