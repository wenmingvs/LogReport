package com.wenming.library;

import android.content.Context;

import com.wenming.library.log.CrashHandler;
import com.wenming.library.upload.LogUpload;

/**
 * Created by wenmingvs on 2016/7/7.
 */
public class LogReport {

    private static LogReport logReport = new LogReport();
    private LogUpload mUpload;


    private LogReport() {
    }

    public static LogReport getInstance() {
        return logReport;
    }

    /**
     * 设置日志的上传的方式。
     *
     * @param logUpload
     */
    public LogReport setUploadType(LogUpload logUpload) {
        mUpload = logUpload;
        return this;
    }

    public void init(Context context) {
        CrashHandler.getInstance().init(context, mUpload);
    }


}
