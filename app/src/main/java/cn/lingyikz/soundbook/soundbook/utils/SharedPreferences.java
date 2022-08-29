package cn.lingyikz.soundbook.soundbook.utils;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;



public class SharedPreferences {

    private static String OLD_AUDIO_INFO = "oldAudioInfo";
    private static String UUID = "uuid" ;
    public static void saveOldAudioInfo(Activity activity, Bundle bundle){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = oldAudioInfo.edit();
        editor.putInt("albumId",bundle.getInt("albumId"));
        editor.putInt("episodes",bundle.getInt("episodes"));
        editor.putInt("audioId",bundle.getInt("audioId"));
        editor.putLong("audioCreated",bundle.getLong("audioCreated"));
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
        editor.putLong("audioCreated",bundle.getLong("audioCreated"));
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
        bundle.putLong("audioCreated", oldAudioInfo.getLong("audioCreated",0));
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
        bundle.putLong("audioCreated", oldAudioInfo.getLong("audioCreated",0));
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
}
