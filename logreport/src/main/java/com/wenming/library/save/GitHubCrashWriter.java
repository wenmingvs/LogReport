package com.wenming.library.save;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.wenming.library.LogReport;
import com.wenming.library.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

/**
 * 逐个保存崩溃信息
 * Created by wenmingvs on 2016/7/8.
 */
public class GitHubCrashWriter extends BaseSaver {

    private final static String TAG = "GitHubCrashWriter";

    public GitHubCrashWriter(Context context) {
        super(context);
    }

    /**
     * 崩溃日志全名拼接
     */
    public final static String LOG_FILE_NAME_EXCEPTION = "CrashLog" + yyyy_MM_dd_HH_mm_ss_SS.format(new Date(System.currentTimeMillis())) + SAVE_FILE_TYPE;

    @Override
    public synchronized void writeCrash(Thread thread, Throwable ex, String tag, String content) {
        LOG_DIR = LogReport.LOGDIR + "/Log/" + yyyy_mm_dd.format(new Date(System.currentTimeMillis()));
        RandomAccessFile randomAccessFile = null;
        File logsDir = new File(LOG_DIR);
        File logFile = new File(logsDir, LOG_FILE_NAME_EXCEPTION);
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (!logsDir.exists()) {
                    logsDir.mkdirs();
                }

                if (!logFile.exists()) {
                    createFile(logFile, mContext);
                }
                StringBuilder preContent = new StringBuilder(mEncryption.decrypt(FileUtil.getText(logFile)));
                Log.d("wenming", "读取本地的Crash文件，并且解密 = \n" + preContent);
                preContent.append("\r\n" + formatLogMsg(tag, content));
                Log.d("wenming", "即将保存的Crash文件内容 = \n" + preContent);
                saveText(logFile, preContent.toString());

            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
    }

    @Override
    public void closeApp(Thread thread, Throwable ex) {

    }

}
