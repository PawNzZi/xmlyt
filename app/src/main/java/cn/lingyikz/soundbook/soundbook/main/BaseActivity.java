package cn.lingyikz.soundbook.soundbook.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.KongzueStyle;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        DialogX.init(this);
        //设置为Kongzue主题
        DialogX.globalStyle = new KongzueStyle();
        setContentView(setLayout());
        setView();
        setData();
    }

    @Override
    protected void onStart() {
//        Constans.user = SharedPreferences.getUser(this);
//        Log.i("TAG",Constans.user+"");
        super.onStart();
    }

    protected abstract void setData();

    protected abstract void setView();

    protected abstract View setLayout();

    //活动管理器
    public static class ActivityCollector {

        public static List<Activity> activitys = new ArrayList<Activity>();

        /**
         * 向List中添加一个活动
         *
         * @param activity 活动
         */
        public static void addActivity(Activity activity) {

            activitys.add(activity);
        }

        /**
         * 从List中移除活动
         *
         * @param activity 活动
         */
        public static void removeActivity(Activity activity) {

            activitys.remove(activity);
        }

        /**
         * 将List中存储的活动全部销毁掉
         */
        public static void finishAll() {

            for (Activity activity : activitys) {

                if (!activity.isFinishing()) {

                    activity.finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity管理
        ActivityCollector.removeActivity(this);
    }
}