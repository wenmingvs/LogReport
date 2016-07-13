package com.wenming.library.save;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.wenming.library.LogReport;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

/**
 * 此类区别于SingleCrash，是将崩溃信息都写在一个文件中
 * Created by wenmingvs on 2016/7/7.
 */
public class LogSaver extends BaseSave {

    private final static String TAG = "LogSaver";

    /**
     * 崩溃日志全名拼接
     */
    public final static String LOG_FILE_NAME_EXCEPTION = "CrashLog" + LOG_CREATE_TIME + SAVE_FILE_TYPE;

    public LogSaver(Context context) {
        super(context);
    }

    @Override
    public synchronized File writeCrash(String tag, String content) {
        LOG_DIR =
            LogReport.LOGDIR + "/Log/" + CREATE_DATE_FORMAT.format(new Date(System.currentTimeMillis()))
                + File.separator;
        File logsDir = new File(LOG_DIR);
        RandomAccessFile randomAccessFile = null;
        File logFile = new File(logsDir, LOG_FILE_NAME_EXCEPTION);
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (!logsDir.exists()) {
                    logsDir.mkdirs();
                }
                if (!logFile.exists()) {
                    createFile(logFile, mContext);
                }
                // 读取文件中的文本内容，并且解密
                StringBuilder preContent = new StringBuilder(mEncryption.decrypt(getText(logFile)));
                Log.d("wenming", "读取本地的Crash文件，并且解密 = \n" + preContent);
                // 添加log内容
                preContent.append("\r\n" + formatLogMsg(tag, content));

                Log.d("wenming", "即将保存的Crash文件内容 = \n" + preContent);
                saveText(logFile, preContent.toString());

                // randomAccessFile = new RandomAccessFile(logFile, "rw");
                // randomAccessFile.seek(logFile.length());
                // randomAccessFile.write(("\r\n" + formatLogMsg(tag, content)).getBytes());
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
        return logFile;
    }


}
