package com.wenming.library;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by wenmingvs on 2016/7/9.
 */
public class LogService extends Service {

    public static final String TAG = "LogService";

    public static final String logdir = LogReport.getInstance().LOGDIR;

    public static final String AlreadyUploadLogDir = LogReport.getInstance().LOGDIR + "AlreadyUploadLog/";

    /**
     * 压缩包名称的一部分：时间戳
     */
    public final static SimpleDateFormat ZIP_FOLDER_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("wenming", "logdir = " + LogReport.getInstance().LOGDIR + "Log/");
        File logdir = new File(LogReport.getInstance().LOGDIR + "Log/");//  sdcard/aaa/log
        File savedir = new File(AlreadyUploadLogDir);
        File savefile = new File(savedir, "UploadOn" + ZIP_FOLDER_TIME_FORMAT.format(System.currentTimeMillis()) + ".zip");

        Log.d("wenming", "压缩路径 Logdir = " + logdir.getAbsolutePath());
        Log.d("wenming", "保存zip的 Logdir = " + savefile.getAbsolutePath());
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        if (!savefile.exists()) {
            try {
                savefile.createNewFile();
                if (CompressUtil.zipFileAtPath(logdir.getAbsolutePath(), savefile.getAbsolutePath())) {
                    //TODO 打包成功后，本地删除，然后上传
                    deleteDir(logdir);

                }

            } catch (IOException e) {
                Log.e("wenming", e.getMessage());
                e.printStackTrace();
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


}
