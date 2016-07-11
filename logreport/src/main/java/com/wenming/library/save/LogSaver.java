package com.wenming.library.save;

import android.content.Context;
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
        LOG_DIR = LogReport.LOGDIR +"/Log/"+ CREATE_DATE_FORMAT.format(new Date(System.currentTimeMillis()));
        RandomAccessFile randomAccessFile = null;
        File logFile = null;
        try {
            File logsDir = new File(LOG_DIR);
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }
            logFile = new File(logsDir, LOG_FILE_NAME_EXCEPTION);
            if (!logFile.exists()) {
                createFile(logFile, mContext);
            }
            randomAccessFile = new RandomAccessFile(logFile, "rw");
            randomAccessFile.seek(logFile.length());
            randomAccessFile.write(("\r\n" + formatLogMsg(tag, content)).getBytes());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
