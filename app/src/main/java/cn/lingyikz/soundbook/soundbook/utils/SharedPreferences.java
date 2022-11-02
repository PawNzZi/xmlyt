package cn.lingyikz.soundbook.soundbook.utils;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;



public class SharedPreferences {

    private static final String OLD_AUDIO_INFO = "oldAudioInfo";
    private static final String UUID = "uuid" ;
    private static final String BLOCK_CLOSE = "BLOCK_CLOSE";
    private static final String CATEGORY_POSTION = "CATEGORY_POSTION";
    public static void saveOldAudioInfo(Activity activity, Bundle bundle){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = oldAudioInfo.edit();
        editor.putInt("albumId",bundle.getInt("albumId"));
        editor.putInt("episodes",bundle.getInt("episodes"));
        editor.putInt("audioId",bundle.getInt("audioId"));
        editor.putString("audioCreated",bundle.getString("audioCreated"));
        editor.putString("audioDes",bundle.getString("audioDes"));
        editor.putLong("audioDuration",bundle.getLong("audioDuration"));
        editor.putString("title",bundle.getString("title"));
        editor.putString("src",bundle.getString("src"));
        editor.commit();
    }
    public static void saveOldAudioInfo(Context activity, Bundle bundle){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = oldAudioInfo.edit();
        editor.putInt("albumId",bundle.getInt("albumId"));
        editor.putInt("episodes",bundle.getInt("episodes"));
        editor.putInt("audioId",bundle.getInt("audioId"));
        editor.putString("audioCreated",bundle.getString("audioCreated"));
        editor.putString("audioDes",bundle.getString("audioDes"));
        editor.putLong("audioDuration",bundle.getLong("audioDuration"));
        editor.putString("title",bundle.getString("title"));
        editor.putString("src",bundle.getString("src"));
        editor.commit();
    }
    public static Bundle  getOldAudioInfo(Activity activity){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        Bundle bundle = new Bundle() ;
        bundle.putInt("albumId", oldAudioInfo.getInt("albumId",0));
        bundle.putInt("episodes", oldAudioInfo.getInt("episodes",0));
        bundle.putInt("audioId", oldAudioInfo.getInt("audioId",0));
        bundle.putString("audioCreated", oldAudioInfo.getString("audioCreated","0"));
        bundle.putLong("audioDuration", oldAudioInfo.getLong("audioDuration",0));
        bundle.putString("audioDes", oldAudioInfo.getString("audioDes",null));
        bundle.putString("title", oldAudioInfo.getString("title",null));
        bundle.putString("src", oldAudioInfo.getString("src",null));

        return bundle ;
    }
    public static Bundle  getOldAudioInfo(Context activity){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        Bundle bundle = new Bundle() ;

        bundle.putInt("albumId", oldAudioInfo.getInt("albumId",0));
        bundle.putInt("episodes", oldAudioInfo.getInt("episodes",0));
        bundle.putInt("audioId", oldAudioInfo.getInt("audioId",0));
        bundle.putString("audioCreated", oldAudioInfo.getString("audioCreated","0"));
        bundle.putLong("audioDuration", oldAudioInfo.getLong("audioDuration",0));
        bundle.putString("audioDes", oldAudioInfo.getString("audioDes",null));
        bundle.putString("title", oldAudioInfo.getString("title",null));
        bundle.putString("src", oldAudioInfo.getString("src",null));

        return bundle ;
    }

    public static void saveUUid(Activity activity,String uuid){
//        Log.i("TAG","UUID："+uuid);
        android.content.SharedPreferences UUIDInfo = activity.getSharedPreferences(UUID, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = UUIDInfo.edit();
        editor.putString("UUID",uuid);
        editor.commit();
    }

    public static String getUUID(Activity activity){

        android.content.SharedPreferences UUIDInfo = activity.getSharedPreferences(UUID, Context.MODE_PRIVATE);
        return UUIDInfo.getString("UUID",null);
    }
    public static void saveUUid(Context activity,String uuid){
        android.content.SharedPreferences UUIDInfo = activity.getSharedPreferences(UUID, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = UUIDInfo.edit();
        editor.putString("UUID",uuid);
        editor.commit();
    }

    public static String getUUID(Context activity){

        android.content.SharedPreferences UUIDInfo = activity.getSharedPreferences(UUID, Context.MODE_PRIVATE);

        return  UUIDInfo.getString("UUID",null);
    }

    public static void saveBolckClose(Activity activity,Bundle bundle){
        android.content.SharedPreferences bolckClose = activity.getSharedPreferences(BLOCK_CLOSE, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = bolckClose.edit();
        editor.putString("lable",bundle.getString("lable"));
        editor.putInt("index",bundle.getInt("index"));
        editor.commit();

    }
    public static void saveBolckClose(Context activity,Bundle bundle){
        android.content.SharedPreferences bolckClose = activity.getSharedPreferences(BLOCK_CLOSE, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = bolckClose.edit();
        editor.putString("lable",bundle.getString("lable"));
        editor.putInt("index",bundle.getInt("index"));
        editor.commit();

    }
    public static Bundle getBolckClose(Activity activity){
        android.content.SharedPreferences bolckClose = activity.getSharedPreferences(BLOCK_CLOSE, Context.MODE_PRIVATE);
        Bundle bundle = new Bundle();
        bundle.putString("lable",bolckClose.getString("lable",""));
        bundle.putInt("index",bolckClose.getInt("index",-1));
        return bundle;
    }
    public static void  saveCategoryIndex(Context context,int position){
        android.content.SharedPreferences categoryIndex = context.getSharedPreferences(CATEGORY_POSTION, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = categoryIndex.edit();
        editor.putInt("position",position);
        editor.commit();
    }
    public static int getCategoryIndex(Context context){
        android.content.SharedPreferences categoryIndex = context.getSharedPreferences(CATEGORY_POSTION, Context.MODE_PRIVATE);
        return categoryIndex.getInt("position",0);
    }
}
