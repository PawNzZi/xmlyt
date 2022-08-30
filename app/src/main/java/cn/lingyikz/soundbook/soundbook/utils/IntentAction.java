package cn.lingyikz.soundbook.soundbook.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Map;

import cn.lingyikz.soundbook.soundbook.R;


public class IntentAction {
    private static Intent intent = new Intent();

    public static void toNextActivity(Activity activity,Class Glass){
        intent.setClass(activity,Glass);
        activity.startActivity(intent);

    }

    public static void setValueActivity(Activity activity, Class Glass, Bundle bundle) {
        intent.putExtras(bundle);
        intent.setClass(activity,Glass);
        activity.startActivity(intent);
    }


    public static void toNextActivity(Context context, Class Glass){
        intent.setClass(context,Glass);
        context.startActivity(intent);
    }

    public static void setValueContext(Context context, Class Glass, Bundle bundle) {
        intent.putExtras(bundle);
        intent.setClass(context,Glass);
        context.startActivity(intent);
    }

//    public static void startService(Activity activity, Class service, ImageView view,DataBaseHelper dataBaseHelper){
//        Intent intent = new Intent(activity, service);
//        Bundle oldAudioInfo =  SharedPreferences.getOldAudioInfo(activity);
//        long audioDuration = dataBaseHelper.queryPlayHistory(oldAudioInfo.getInt("albumId"),oldAudioInfo.getInt("audioId"));
//        dataBaseHelper.close();
//        if(oldAudioInfo.getString("src")!= null){
//            oldAudioInfo.putBoolean("continuePlay",true);
//            oldAudioInfo.putLong("audioDuration",audioDuration);
//            SharedPreferences.saveOldAudioInfo(activity,oldAudioInfo);
//            intent.putExtras(oldAudioInfo);
//            activity.startService(intent);
//            if(MediaPlayer.getInstance().isPlay()){
//                view.setImageDrawable(activity.getResources().getDrawable(R.mipmap.title_pause, activity.getResources().newTheme()));
//            }else {
//                view.setImageDrawable(activity.getResources().getDrawable(R.mipmap.title_play, activity.getResources().newTheme()));
//            }
//        }else {
//            //提示无播放历史
//            Toast.makeText(activity, "当前无播放", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
}
