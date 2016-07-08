package com.wenming.library.helper;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by wenmingvs on 2016/6/23.
 */
public class SDcardHelper extends CommentHelper {

    private static String TAG = "SDcardHelper";
    public String Internal_SDcard_Path;
    public String External_SDcard_Path;

    public SDcardHelper() {
        Internal_SDcard_Path = getInternalSDcardPath();
        External_SDcard_Path = getExternalSDcardPath();
    }

    public String getInternalSDcardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }


    public String getExternalSDcardPath() {
        String exSDpath = null;
        String sd_default = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (sd_default.endsWith("/")) {
            sd_default = sd_default.substring(0, sd_default.length() - 1);
        }
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        exSDpath = columns[1];
                    }
                } else if (line.contains("fuse") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        exSDpath = columns[1];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(exSDpath)) {
            Log.e(TAG, "Exception: 不存在外置SD卡", new RuntimeException());
        }
        return exSDpath + File.separator;
    }


    public String getInternalSDcardChildFolderPath(String folderName) {
        File file = new File(getInternalSDcardPath() + folderName + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }


    /**
     * 安卓系统升级为4.4.2，无法保存到外置存储卡Android 4.4，代号为KitKat。因为根据新版本的API改进，应用程序将不能再往SD卡中写入文件。
     * 在Android开发者网站的 “外部存储技术信息”文档 中描述道： WRITE_EXTERNAL_STORAGE只为设备上的主要外部存储授予写权限，
     * 应用程序无法将数据写入二级外部存储设备 ，除非综合权限指定了应用程序的包目录。这目前只影响双存储设备， 如果你的设备有内部存储空间，
     * 即通常所说的机身存储（这就是指主要外部存储），那么你的SD卡就是一个二级外部存储设备。 在Android 4.4中，如果你同时使用了机身存储和SD卡，
     * 那么应用程序将无法在SD卡中创建、修改、删除数据。比如，你无法使用多看下载书籍保存到外置存储卡了。但是应用程序仍然可以往主存储的任意目录中写入数据，
     * 不受任何限 制。 Google表示， 这样做的目的是，通过这种方式进行限 制，系统可以在应用程序被卸载后清除遗留文件。
     *
     * @param folderName
     * @return
     */
    public String getExternalSDcardChildFolderPath(String folderName) {
        File file = new File(getExternalSDcardPath() + folderName + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

}
