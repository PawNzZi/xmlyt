package cn.lingyikz.soundbook.soundbook.utils;

import android.app.Activity;
import android.content.Context;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class SharedPreferences {

    private static String OLD_AUDIO_INFO = "oldAudioInfo";
    private static String UUID = "uuid" ;
    public static void saveOldAudioInfo(Activity activity, Map<String,Object> map){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = oldAudioInfo.edit();
        editor.putString("title", Objects.requireNonNull(map.get("title")).toString());
        editor.putString("src", Objects.requireNonNull(map.get("src")).toString());
        editor.putLong("currentPosition", (Long) map.get("currentPosition"));
        editor.commit();
    }
    public static void saveOldAudioInfo(Context activity, Map<String,Object> map){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = oldAudioInfo.edit();
        editor.putString("title", Objects.requireNonNull(map.get("title")).toString());
        editor.putString("src", Objects.requireNonNull(map.get("src")).toString());
        editor.putLong("currentPosition", (Long) map.get("currentPosition"));
        editor.commit();
    }
    public static Map<String,Object> getOldAudioInfo(Activity activity){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        Map<String,Object> map = new HashMap<>();

        map.put("title",oldAudioInfo.getString("title",null));
        map.put("src",oldAudioInfo.getString("src",null));
        map.put("currentPosition",oldAudioInfo.getLong("currentPosition",0L));
        return map ;
    }
    public static Map<String,Object> getOldAudioInfo(Context activity){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        Map<String,Object> map = new HashMap<>();
        map.put("title",oldAudioInfo.getString("title",null));
        map.put("src",oldAudioInfo.getString("src",null));
        map.put("currentPosition",oldAudioInfo.getLong("currentPosition",0L));
        return map ;
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
