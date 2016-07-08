package com.wenming.library.helper;

import android.text.TextUtils;

import com.wenming.library.CacheManagerConfiguration;

import java.io.File;

/**
 * Created by wenmingvs on 2016/6/24.
 */
public class AppFolderHelper extends CommentHelper{

    public static String APP_FOLDER_PATH;
    public static String CACHE;
    public static String CACHE_IMAGE;
    public static String CACHE_SOUND;
    public static String CACHE_VIDEO;
    public static String CACHE_OTHER;
    public static String DOWNLOAD;
    public static String DOWNLOAD_IMAGE;
    public static String DOWNLOAD_SOUND;
    public static String DOWNLOAD_VIDEO;
    public static String DOWNLOAD_OTHER;
    public static String LOG;

    private static CacheManagerConfiguration config;
    public static CleanHelper cleanHelper;
    public static DataAndroidHelper dataAndroidHelper;
    public static DataHepler dataHepler;
    public static SDcardHelper sdcardHelper;

    public AppFolderHelper(CacheManagerConfiguration config, CleanHelper cleanHelper, DataAndroidHelper dataAndroidHelper, DataHepler dataHepler, SDcardHelper sdcardHelper) {
        this.config = config;
        this.cleanHelper = cleanHelper;
        this.dataAndroidHelper = dataAndroidHelper;
        this.dataHepler = dataHepler;
        this.sdcardHelper = sdcardHelper;
        initDefaultFolder();
    }

    public void initDefaultFolder() {
        APP_FOLDER_PATH = getAppFolderPath();
        CACHE = APP_FOLDER_PATH + ".cache/";
        CACHE_IMAGE = CACHE + "image/";
        CACHE_SOUND = CACHE + "sound/";
        CACHE_VIDEO = CACHE + "video/";
        CACHE_OTHER = CACHE + "other/";
        DOWNLOAD = APP_FOLDER_PATH + "download/";
        DOWNLOAD_IMAGE = DOWNLOAD + "image/";
        DOWNLOAD_SOUND = DOWNLOAD + "sound/";
        DOWNLOAD_VIDEO = DOWNLOAD + "video/";
        DOWNLOAD_OTHER = DOWNLOAD + "other/";
        LOG = APP_FOLDER_PATH + "log/";
    }

    public String getCACHE() {
        File file = new File(CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return CACHE;
    }

    public String getCacheImage() {
        File file = new File(CACHE_IMAGE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return CACHE_IMAGE;
    }

    public String getCacheSound() {
        File file = new File(CACHE_SOUND);
        if (!file.exists()) {
            file.mkdirs();
        }
        return CACHE_SOUND;
    }

    public String getCacheVideo() {
        File file = new File(CACHE_VIDEO);
        if (!file.exists()) {
            file.mkdirs();
        }
        return CACHE_VIDEO;
    }

    public String getDownload() {
        File file = new File(DOWNLOAD);
        if (!file.exists()) {
            file.mkdirs();
        }
        return DOWNLOAD;
    }

    public String getDownloadImage() {
        File file = new File(DOWNLOAD_IMAGE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return DOWNLOAD_IMAGE;
    }

    public String getDownloadSound() {
        File file = new File(DOWNLOAD_SOUND);
        if (!file.exists()) {
            file.mkdirs();
        }
        return DOWNLOAD_SOUND;
    }

    public String getDownloadVideo() {
        File file = new File(DOWNLOAD_VIDEO);
        if (!file.exists()) {
            file.mkdirs();
        }
        return DOWNLOAD_VIDEO;
    }

    public String getLog() {
        File file = new File(LOG);
        if (!file.exists()) {
            file.mkdirs();
        }
        return LOG;
    }

    public String getCacheOther() {
        File file = new File(CACHE_OTHER);
        if (!file.exists()) {
            file.mkdirs();
        }
        return CACHE_OTHER;
    }

    public String getDownloadOther() {
        File file = new File(DOWNLOAD_OTHER);
        if (!file.exists()) {
            file.mkdirs();
        }
        return DOWNLOAD_OTHER;
    }

    /**
     * 根据config 的配置，在对应的地方创建应用的根目录。如果配置了公司文件夹且在内置sd卡内创建根目录的话，会首先
     * 创建公司文件夹，然后在公司文件夹下创建应用的根文件夹
     */
    public String getAppFolderPath() {
        String appFolderPath = "";
        switch (config.appRootFolderParentPath) {
            case CacheManagerConfiguration.APPFOLDER_IN_DATA:
                appFolderPath = createAppFolder_In_Data();
                break;
            case CacheManagerConfiguration.APPFOLDER_IN_ANDROID_DATA:
                appFolderPath = createAppFolder_In_AndroidData();
                break;
            case CacheManagerConfiguration.APPFOLDER_IN_INTERNAL_SD:
                appFolderPath = createAppFolder_In_Internal_SD();
                break;
            case CacheManagerConfiguration.APPFOLDER_IN_EXTERNAL_SD:
                appFolderPath = createAppFolder_In_EXTERNAL_SD();
                break;
            case CacheManagerConfiguration.APPFOLDER_IN_AUTO:
                if (SDcardHelper.isSDCardEnable()) {
                    appFolderPath = createAppFolder_In_Internal_SD();
                } else {
                    appFolderPath = createAppFolder_In_Data();
                }
                break;
        }
        if (appFolderPath != null) {
            if (!appFolderPath.endsWith(File.separator)) {
                appFolderPath += File.separator;
            }
            File file = new File(appFolderPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return appFolderPath;
    }

    private static String createAppFolder_In_Data() {
        return dataHepler.getFilesPath();
    }

    private static String createAppFolder_In_AndroidData() {
        return dataAndroidHelper.getFilesPath();
    }

    private static String createAppFolder_In_Internal_SD() {
        if (TextUtils.isEmpty(config.companyFolder)) {
            return sdcardHelper.getInternalSDcardPath() + config.appRootName + File.separator;
        } else {
            return sdcardHelper.getInternalSDcardPath() + config.companyFolder + File.separator + config.appRootName + File.separator;
        }
    }

    private static String createAppFolder_In_EXTERNAL_SD() {
        if (TextUtils.isEmpty(config.companyFolder)) {
            return sdcardHelper.getExternalSDcardPath() + config.appRootName + File.separator;
        } else {
            return sdcardHelper.getExternalSDcardPath() + config.companyFolder + File.separator + config.appRootName + File.separator;
        }
    }

}
