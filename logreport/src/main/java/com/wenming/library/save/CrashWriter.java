package com.wenming.library.save;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.wenming.library.LogReport;

import java.io.File;
import java.util.Date;

/**
 * 此类区别于SingleCrash，是将崩溃信息都写在一个文件中
 * Created by wenmingvs on 2016/7/7.
 */
public class CrashWriter extends LogWriter {

    private final static String TAG = "CrashWriter";

    /**
     * 崩溃日志全名拼接
     */
    public final static String LOG_FILE_NAME_EXCEPTION = "CrashLog" + LOG_CREATE_TIME + SAVE_FILE_TYPE;

    public CrashWriter(Context context) {
        super(context);
    }

    @Override
    public synchronized void writeCrash(String tag, String content) {
        LOG_DIR = LogReport.LOGDIR + "Log/" + CREATE_DATE_FORMAT.format(new Date(System.currentTimeMillis())) + "/";
        File logsDir = new File(LOG_DIR);
        File logFile = new File(logsDir, LOG_FILE_NAME_EXCEPTION);
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (!logsDir.exists()) {
                    Log.d("wenming", "logsDir.mkdirs() =  +　" + logsDir.mkdirs());
                }
                if (!logFile.exists()) {
                    createFile(logFile, mContext);
                }
                StringBuilder preContent = new StringBuilder(decodeString(getText(logFile)));
                Log.d("wenming", "读取本地的Crash文件，并且解密 = \n" + preContent.toString());
                preContent.append(formatLogMsg(tag, content) + "\n");
                Log.d("wenming", "即将保存的Crash文件内容 = \n" + preContent.toString());
                saveText(logFile, preContent.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }


}
