package com.wenming.library.save;

import com.wenming.library.encryption.IEncryption;

/**
 * Created by wenmingvs on 2016/7/7.
 */
public interface ISave {

    public void writeLog(String tag, String content);

    public void writeCrash(String tag, String content);

    public void setEncodeType(IEncryption encodeType);
}
