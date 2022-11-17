package cn.lingyikz.soundbook.soundbook.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class VersionUtil {

    public static String getVersonName(Context mContext){
        PackageManager packageManager = mContext.getPackageManager();
        String mVersionName = "-1";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            mVersionName = packageInfo.versionName;
//            Log.i("TAG",mVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mVersionName ;
    }
    public static int getVersonCode(Context mContext){
        PackageManager packageManager = mContext.getPackageManager();
        int mVersionCode = -1 ;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            mVersionCode = packageInfo.versionCode;
//            Log.i("TAG",mVersionCode+"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mVersionCode ;
    }
}
