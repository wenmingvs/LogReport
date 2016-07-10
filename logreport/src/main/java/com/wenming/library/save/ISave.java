package com.wenming.library.save;

import com.wenming.library.encryption.IEncryption;

import java.io.File;

/**
 * Created by wenmingvs on 2016/7/7.
 */
public interface ISave {

    public File writeLog(String tag, String content);

    public File writeCrash(String tag, String content);

    public void setEncodeType(IEncryption encodeType);
}
