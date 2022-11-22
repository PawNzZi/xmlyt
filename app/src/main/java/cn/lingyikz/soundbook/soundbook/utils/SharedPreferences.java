package cn.lingyikz.soundbook.soundbook.utils;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;

import cn.lingyikz.soundbook.soundbook.modle.v2.User;


public class SharedPreferences {

    private static final String OLD_AUDIO_INFO = "oldAudioInfo";
    private static final String UUID = "uuid" ;
    private static final String BLOCK_CLOSE = "BLOCK_CLOSE";
    private static final String CATEGORY_POSTION = "CATEGORY_POSTION";
    private static final String USER_INFO = "USERINFO";

    public static void clearLoginUserInfo(Activity activity){
        android.content.SharedPreferences userInfo = activity.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = userInfo.edit();
        editor.clear().commit();

    }
    public static void saveCurrentPlayHistoryInfo(Activity activity, Bundle bundle){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = oldAudioInfo.edit();
        editor.putLong("albumId",bundle.getLong("albumId"));
        editor.putInt("episodes",bundle.getInt("episodes"));
        editor.putLong("audioId",bundle.getLong("audioId"));
        editor.putString("audioCreated",bundle.getString("audioCreated"));
        editor.putString("audioDes",bundle.getString("audioDes"));
        editor.putString("audioDuration",bundle.getString("audioDuration"));
        editor.putString("title",bundle.getString("title"));
        editor.putString("src",bundle.getString("src"));
        editor.commit();
    }
    public static void saveCurrentPlayHistoryInfo(Context activity, Bundle bundle){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = oldAudioInfo.edit();
        editor.putLong("albumId",bundle.getLong("albumId"));
        editor.putInt("episodes",bundle.getInt("episodes"));
        editor.putLong("audioId",bundle.getLong("audioId"));
        editor.putString("audioCreated",bundle.getString("audioCreated"));
        editor.putString("audioDes",bundle.getString("audioDes"));
        editor.putString("audioDuration",bundle.getString("audioDuration"));
        editor.putString("title",bundle.getString("title"));
        editor.putString("src",bundle.getString("src"));
        editor.commit();
    }
    public static Bundle  currentPlayHistoryInfo(Activity activity){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        Bundle bundle = new Bundle() ;
        bundle.putLong("albumId", oldAudioInfo.getLong("albumId",0));
        bundle.putInt("episodes", oldAudioInfo.getInt("episodes",0));
        bundle.putLong("audioId", oldAudioInfo.getLong("audioId",0));
        bundle.putString("audioCreated", oldAudioInfo.getString("audioCreated","0"));
        bundle.putString("audioDuration", oldAudioInfo.getString("audioDuration","0"));
        bundle.putString("audioDes", oldAudioInfo.getString("audioDes",null));
        bundle.putString("title", oldAudioInfo.getString("title",null));
        bundle.putString("src", oldAudioInfo.getString("src",null));

        return bundle ;
    }
    public static Bundle  currentPlayHistoryInfo(Context activity){
        android.content.SharedPreferences oldAudioInfo = activity.getSharedPreferences(OLD_AUDIO_INFO, Context.MODE_PRIVATE);
        Bundle bundle = new Bundle() ;

        bundle.putLong("albumId", oldAudioInfo.getLong("albumId",0));
        bundle.putInt("episodes", oldAudioInfo.getInt("episodes",0));
        bundle.putLong("audioId", oldAudioInfo.getLong("audioId",0));
        bundle.putString("audioCreated", oldAudioInfo.getString("audioCreated","0"));
        bundle.putString("audioDuration", oldAudioInfo.getString("audioDuration","0"));
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

    public static void saveUser(Context context, User user){
        android.content.SharedPreferences sp = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putLong("id", user.getId());
        editor.putString("nickname",user.getNickname());
        editor.putString("email",user.getEmail());
        editor.putString("avatar",user.getAvatar());
        editor.putString("phone",user.getPhone());
        editor.putInt("status",user.getStatus());
        editor.putInt("level",user.getLevel());
        editor.commit();
    }
    public static void saveUser(Activity context, User user){
        android.content.SharedPreferences sp = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putLong("id", user.getId());
        editor.putString("nickname",user.getNickname());
        editor.putString("email",user.getEmail());
        editor.putString("avatar",user.getAvatar());
        editor.putString("phone",user.getPhone());
        editor.putInt("status",user.getStatus());
        editor.putInt("level",user.getLevel());
        editor.commit();
    }
    public static User getUser(Context context){
        android.content.SharedPreferences sp = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        User user = new User();
        user.setId(sp.getLong("id",-1));
        if(user.getId() == -1){
            return null ;
        }else{
            user.setNickname(sp.getString("nickname",null));
            user.setEmail(sp.getString("email",null));
            user.setAvatar(sp.getString("avatar",null));
            user.setPhone(sp.getString("phone",null));
            user.setLevel(sp.getInt("level",-1));
            user.setStatus(sp.getInt("status",-1));
            return user ;
        }
    }

}
