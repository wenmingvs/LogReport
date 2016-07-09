package com.wenming.library.crash;

import android.util.Log;

import com.wenming.library.save.ISave;

/**
 * Created by wenmingvs on 2016/7/9.
 */
public class LogWriter {
    private static LogWriter logWriter = new LogWriter();
    private static ISave mSave;

    private LogWriter() {
    }

    public static LogWriter getInstance() {
        return logWriter;
    }

    public LogWriter setLogSaver(ISave save) {
        this.mSave = save;
        return this;
    }

    public static void writeLog(String tag, String content) {
        Log.d(tag, content);
        mSave.writeLog(tag, content);
    }
}
