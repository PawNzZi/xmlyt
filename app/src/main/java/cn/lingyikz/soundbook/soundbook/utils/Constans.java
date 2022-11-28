package cn.lingyikz.soundbook.soundbook.utils;

import android.os.Environment;

import java.io.File;

import cn.lingyikz.soundbook.soundbook.modle.v2.User;

public class Constans {

    public final static String CHANGE_PLAY_IMG = "change_play_img";
    public final static String EMPTY_TOAST = "无数据";
    public final static int PAGE_SIZE = 50;
    public final static int INDEX_PAGE_SIZE = 15 ;
    public final static int PLAY_MODLE_LIST = 0;
    public final static int PLAY_MODLE_ICON = 1;
    public final static int PLAY_MODLE_INNER = 3;
    public final static String SET_BLOCK = "SET_BLOCK";
    public final static String NO_OLD_AUDIOINFO = "无播放数据";
    public final static String START_SERVICE = "start_service";
    public final static String BIND_SERVICE = "bind_service";
    public final static String ALBUM_CONTENT_NULL = "该内容正在上传中";
    public final static String DO_NOT_COLLECT = "无资源，不能收藏";

    public final static int UPDATE_AUDIO = 0 ;
    public final static int UPDATE_BLOCK = 1 ;
    public final static int PLAY_PAUSE = 0 ;
    public final static String APP_CENTER_SECRET= "462377c6-9210-44ce-8eff-05033ca3ee4f" ;
    public final static String VERSION_ERROR = "获取版本失败";
    public final static String VERSION_NUBMER = "0.5.7";
    public final static String LATEST_VERSION = "已是最新版本";
    public final static String CHECKING_UPDATE = "检查中...";
    public final static String NO_LOADING = "别拉了，无了！" ;
    public final static String PLAY_HISTORY_TIP = "上次播放 ";
    public final static String HOME_BANNER = "home_banner";
    public static final String REGISTER_USERNAME_REGEX = "^(?![0-9]+$)(?![a-z]+$)[0-9a-z]{8,16}$";
    public static final String REGISTER_PASSWORD_REGEX = "^(?![0-9]+$)(?![a-z]+$)[0-9a-z]{6,12}$";

    public final static String ENDWITH = ".mp3";
    public final static String APP_NAME = "zmlm" ;

    public final static String DOWNLOAD_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + APP_NAME ;
    public final static String FSEPARATOR = "_" ;
    public final static String WARN_TIP = "友情提示";
    public final static String DIALOG_SURE_BUTTON = "确定";
    public final static String DIALOG_CANCEL_BUTTON = "取消";
    public final static String BUGLY_APPID = "65570d87b8";
    public final static String APP_INSTALL = "安装";
    public final static String DOWNLOAD_APP_PATH = DOWNLOAD_FILE_PATH + File.separator + APP_NAME + ".apk";
    public static User user = new User() ;

}
