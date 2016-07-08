package com.wenming.library.log;

import android.content.Context;

import com.wenming.library.save.CrashAllInOne;
import com.wenming.library.save.ISave;

/**
 * 助于记录和保存日志信息的
 * Created by wenmingvs on 2016/7/7.
 */
public class LogSaver {

    public static final String TAG = "LogSaver";

    /**
     * 打印并且保存你需要的Log到本地
     *
     * @param log
     */
    public static void writeDebugLog(Context context, String log) {
        ISave save = new CrashAllInOne(context);
        save.writeLogToFile(CrashAllInOne.LOG_FILE_NAME_MONITOR, TAG, log);
    }

}
