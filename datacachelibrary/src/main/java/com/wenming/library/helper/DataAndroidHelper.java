package com.wenming.library.helper;

import android.content.Context;
import android.os.Environment;

/**
 * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
 * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
 * Created by wenmingvs on 2016/6/23.
 */
public class DataAndroidHelper extends CommentHelper {
    private static Context mContext;

    public static String CACHE_DIR;

    public static String FILES_DIR;


    public DataAndroidHelper(Context context) {
        this.mContext = context;
        CACHE_DIR = Environment.getExternalStorageDirectory() + "/data/" + mContext.getPackageName() + "/cache/";
        FILES_DIR = Environment.getExternalStorageDirectory() + "/data/" + mContext.getPackageName() + "/files/";
    }


    public String getCachePath() {
        return CACHE_DIR;
    }


    public String getFilesPath() {
        return FILES_DIR;
    }


}
