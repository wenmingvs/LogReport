package com.wenming.library.upload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.wenming.library.LogReport;
import com.wenming.library.util.CompressUtil;
import com.wenming.library.util.FileUtil;
import com.wenming.library.util.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by wenmingvs on 2016/7/9.
 */
public class UploadService extends Service {

    public static final String TAG = "UploadService";

    public static final String ROOT = LogReport.getInstance().ROOT;


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
        final File logfolder = new File(LogReport.getInstance().ROOT + "Log/");
        // 如果Log文件夹都不存在，说明不存在崩溃日志，检查缓存是否超出大小后退出
        if (!logfolder.exists() || logfolder.listFiles().length == 0) {
            LogUtil.d("Log文件夹都不存在，无需上传");
            return Service.START_NOT_STICKY;
        }
        //只存在log文件，但是不存在崩溃日志，也不会上传
        ArrayList<File> crashFileList = FileUtil.getCrashList(logfolder);
        if (crashFileList.size() == 0) {
            LogUtil.d("只存在log文件，但是不存在崩溃日志，所以不上传");
            return Service.START_NOT_STICKY;
        }

        File zipfolder = new File(LogReport.getInstance().ROOT + "AlreadyUploadLog/");
        File zipfile = new File(zipfolder, "UploadOn" + ZIP_FOLDER_TIME_FORMAT.format(System.currentTimeMillis()) + ".zip");
        final File rootdir = new File(ROOT);
        StringBuilder content = new StringBuilder();

        //创建文件，如果父路径缺少，创建父路径
        zipfile = FileUtil.createFile(zipfolder, zipfile);

        //把日志文件压缩到压缩包中
        if (CompressUtil.zipFileAtPath(logfolder.getAbsolutePath(), zipfile.getAbsolutePath())) {
            LogUtil.d("把日志文件压缩到压缩包中 ----> 成功");
            for (File crash : crashFileList) {
                content.append(FileUtil.getText(crash));
                content.append("\n");
            }
            LogReport.getInstance().getUpload().sendFile(zipfile, content.toString(), new ILogUpload.OnUploadFinishedListener() {
                @Override
                public void onSuceess() {
                    LogUtil.d("日志发送成功！！");
                    FileUtil.deleteDir(logfolder);
                    boolean checkresult = checkCacheSize(rootdir);
                    LogUtil.d("缓存大小检查，是否删除root下的所有文件 = " + checkresult);
                    stopSelf();
                }

                @Override
                public void onError(String error) {
                    LogUtil.d("日志发送失败：  = " + error);
                    boolean checkresult = checkCacheSize(rootdir);
                    LogUtil.d("缓存大小检查，是否删除root下的所有文件 " + checkresult);
                    stopSelf();
                }
            });
        } else {
            LogUtil.d("把日志文件压缩到压缩包中 ----> 失败");
        }
        return Service.START_NOT_STICKY;
    }


    /**
     * 检查文件夹是否超出缓存大小
     *
     * @param dir
     * @return
     */

    public boolean checkCacheSize(File dir) {
        long dirSize = FileUtil.folderSize(dir);
        if (dirSize >= LogReport.getInstance().getCacheSize()) {
            return FileUtil.deleteDir(dir);
        } else {
            return false;
        }
    }
}
