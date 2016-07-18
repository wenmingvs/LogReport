package com.wenming.crashcachedemo;

import android.app.Application;
import android.os.Environment;

import com.wenming.library.LogReport;
import com.wenming.library.save.imp.CrashWriter;
import com.wenming.library.upload.email.EmailReporter;

/**
 * Created by wenmingvs on 2016/7/4.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initEmailReporter();
    }

    /**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
        EmailReporter reporter = new EmailReporter(this);
        reporter.setReceiver("wenmingvs@gmail.com");
        reporter.setSender("387960742@163.com");
        reporter.setSendPassword("gwy9439007836");
        reporter.setSMTPHost("smtp.163.com");
        reporter.setPort("465");
        LogReport.getInstance()
                .setCacheSize(30 * 1024 * 1024)
                .setLogDir(getApplicationContext(), Environment.getExternalStorageDirectory().getPath() + "/aaa/")
                .setUploadType(reporter)
                .setUploadNetWork(true)
                //.setEncryption(new AESEncode())
                .setLogSaver(new CrashWriter(getApplicationContext()))
                .init(getApplicationContext());
    }
}
