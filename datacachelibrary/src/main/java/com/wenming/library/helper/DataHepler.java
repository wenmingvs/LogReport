package com.wenming.library.helper;

import android.content.Context;

/**
 * Created by wenmingvs on 2016/6/23.
 */
public class DataHepler extends CommentHelper {
    private static Context mContext;

    public static String CACHE_DIR;
    public static String FILES_DIR;
    public static String SHAREDPREFS_DIR;
    public static String DATABASES_DIR;
    public static String LIB_DIR;

    public DataHepler(Context mContext) {
        this.mContext = mContext;
        CACHE_DIR = "/data/data/" + mContext.getPackageName() + "/cache/";
        FILES_DIR = "/data/data/" + mContext.getPackageName() + "/files/";
        SHAREDPREFS_DIR = "/data/data/" + mContext.getPackageName() + "/shared_prefs/";
        DATABASES_DIR = "/data/data/" + mContext.getPackageName() + "/databases/";
        LIB_DIR = "/data/data/" + mContext.getPackageName() + "/lib/";
    }


    public String getCachePath() {
        return CACHE_DIR;
    }


    public String getFilesPath() {
        return FILES_DIR;
    }


    public String getDataBasesPath() {
        return DATABASES_DIR;
    }


    public String getSharePrefsencePath() {
        return SHAREDPREFS_DIR;
    }


    public String getLibPath() {
        return LIB_DIR;
    }




}
