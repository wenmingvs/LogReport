package com.wenming.library.save;

import android.content.Context;

import java.io.File;

/**
 * 此类区别于MultipCrash，每发生崩溃，就写入到一个文件中，方便提交到GitHub中
 * Created by wenmingvs on 2016/7/8.
 */
public class SingleCrash implements ISave {
    @Override
    public void writePhoneInfo(File file, Context context) {

    }

    @Override
    public File writeLogToFile(String logFileName, String tag, String tips) {

        return null;
    }
}
