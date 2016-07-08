package com.wenming.library.save;

import android.content.Context;

import java.io.File;

/**
 * Created by wenmingvs on 2016/7/7.
 */
public interface ISave {

    public void writePhoneInfo(File file, Context context);

    public File writeLogToFile(String logFileName, String tag, String tips);
}
