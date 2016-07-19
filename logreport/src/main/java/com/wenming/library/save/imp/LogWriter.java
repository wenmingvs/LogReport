package com.wenming.library.save.imp;

import com.wenming.library.save.ISave;
import com.wenming.library.util.LogUtil;

/**
 * 用于写入Log到本地
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

    public LogWriter init(ISave save) {
        this.mSave = save;
        return this;
    }

    public static void writeLog(String tag, String content) {
        LogUtil.d(tag, content);
        mSave.writeLog(tag, content);
    }
}