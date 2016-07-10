package com.wenming.library;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.wenming.library.crash.CrashHandler;
import com.wenming.library.crash.LogWriter;
import com.wenming.library.encryption.IEncryption;
import com.wenming.library.save.ISave;
import com.wenming.library.upload.LogUpload;

/**
 * Created by wenmingvs on 2016/7/7.
 */
public class LogReport {

    private static LogReport logReport = new LogReport();
    /**
     * 设置上传的方式
     */
    private LogUpload mUpload;
    /**
     * 设置缓存文件夹的大小,默认是30MB
     */
    private long mCacheSize = 30 * 1024 * 1024;

    /**
     * 设置日志保存的路径
     */
    public static String LOGDIR;

    /**
     * 设置加密方式
     */
    private IEncryption mEncryption;

    /**
     * 设置日志的保存方式
     */
    private ISave mLogSaver;

    private LogReport() {
    }

    public static LogReport getInstance() {
        return logReport;
    }

    public LogReport setCacheSize(long cacheSize) {
        this.mCacheSize = mCacheSize;
        return this;
    }

    public LogReport setEncryption(IEncryption encryption) {
        this.mEncryption = encryption;
        return this;
    }

    public LogReport setUploadType(LogUpload logUpload) {
        mUpload = logUpload;
        return this;
    }

    public LogReport setLogDir(Context context, String logDir) {
        if (TextUtils.isEmpty(logDir)) {
            //如果SD不可用，则存储在沙盒中
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                LOGDIR = context.getExternalCacheDir().getAbsolutePath();
            } else {
                LOGDIR = context.getCacheDir().getAbsolutePath();
            }
        } else {
            LOGDIR = logDir;
        }
        return this;
    }

    public LogReport setLogSaver(ISave logSaver) {
        this.mLogSaver = logSaver;
        return this;
    }

    public void init(Context context) {
        if (TextUtils.isEmpty(LOGDIR)) {
            //如果SD不可用，则存储在沙盒中
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                LOGDIR = context.getExternalCacheDir().getAbsolutePath();
            } else {
                LOGDIR = context.getCacheDir().getAbsolutePath();
            }
        }
        if (mEncryption != null) {
            mLogSaver.setEncodeType(mEncryption);
        }
        CrashHandler.getInstance().init(context, mLogSaver);
        LogWriter.getInstance().setLogSaver(mLogSaver);
    }

    /**
     * 调用此方法，上传日志信息
     * @param applicationContext
     */
    public void upload(Context applicationContext) {
        Intent intent = new Intent(applicationContext, LogService.class);
        applicationContext.startService(intent);
    }


}
