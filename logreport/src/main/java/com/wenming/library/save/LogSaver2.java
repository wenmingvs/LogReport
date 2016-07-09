package com.wenming.library.save;

import android.content.Context;

import java.io.File;

/**
 * 此类区别于MultipCrash，每发生崩溃，就写入到一个文件中，方便提交到GitHub中
 * Created by wenmingvs on 2016/7/8.
 */
public class LogSaver2 extends BaseSave {


    public LogSaver2(Context context) {
        super(context);
    }

    @Override
    public File writeLog(String tag, String content) {
        return null;
    }

    @Override
    public File writeCrash(String tag, String content) {
        return null;
    }
}
