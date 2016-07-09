package com.wenming.library.save;

import java.io.File;

/**
 * Created by wenmingvs on 2016/7/7.
 */
public interface ISave {
    
    public File writeLog(String tag, String content);

    public File writeCrash(String tag, String content);
}
