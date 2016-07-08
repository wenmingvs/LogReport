/*
 * @(#)AbstractReportHandler.java		       Project: crash
 * Date:2014-5-27
 *
 * Copyright (c) 2014 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wenming.library.upload;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 抽象的日志报告类
 */
public abstract class LogUpload implements ILogUpload {
    /**
     * 。系统默认异常处理
     */
    private static final Thread.UncaughtExceptionHandler sDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

    private Context mContext;

    private ExecutorService mSingleExecutor = Executors.newSingleThreadExecutor();
    protected Future mFuture;
    private int TIMEOUT = 5;

    public LogUpload(Context context) {
        mContext = context;
    }

    /**
     * 发送报告
     *
     * @param title 报告标题
     * @param body  报告正文，为设备信息及安装包的版本信息
     * @param file  崩溃日志
     */
    protected abstract void sendReport(String title, String body, File file);

    @Override
    public void sendFile(final File file) {
        if (mFuture != null && !mFuture.isDone()) {
            mFuture.cancel(false);
        }
        mFuture = mSingleExecutor.submit(new Runnable() {
            @Override
            public void run() {
                sendReport(buildTitle(mContext), buildBody(mContext), file);
            }
        });
    }

    /**
     * 构建标题
     *
     * @param context
     * @return
     */
    public String buildTitle(Context context) {
        return "Crash Log: " + context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
    }

    /**
     * 构建正文
     *
     * @param context
     * @return
     */
    public String buildBody(Context context) {
        StringBuilder sb = new StringBuilder();

        sb.append("APPLICATION INFORMATION").append('\n');
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = context.getApplicationInfo();
        sb.append("Application : ").append(pm.getApplicationLabel(ai)).append('\n');

        try {
            PackageInfo pi = pm.getPackageInfo(ai.packageName, 0);
            sb.append("Version Code: ").append(pi.versionCode).append('\n');
            sb.append("Version Name: ").append(pi.versionName).append('\n');
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        sb.append('\n').append("DEVICE INFORMATION").append('\n');
        sb.append("Board: ").append(Build.BOARD).append('\n');
        sb.append("BOOTLOADER: ").append(Build.BOOTLOADER).append('\n');
        sb.append("BRAND: ").append(Build.BRAND).append('\n');
        sb.append("CPU_ABI: ").append(Build.CPU_ABI).append('\n');
        sb.append("CPU_ABI2: ").append(Build.CPU_ABI2).append('\n');
        sb.append("DEVICE: ").append(Build.DEVICE).append('\n');
        sb.append("DISPLAY: ").append(Build.DISPLAY).append('\n');
        sb.append("FINGERPRINT: ").append(Build.FINGERPRINT).append('\n');
        sb.append("HARDWARE: ").append(Build.HARDWARE).append('\n');
        sb.append("HOST: ").append(Build.HOST).append('\n');
        sb.append("ID: ").append(Build.ID).append('\n');
        sb.append("MANUFACTURER: ").append(Build.MANUFACTURER).append('\n');
        sb.append("PRODUCT: ").append(Build.PRODUCT).append('\n');
        sb.append("TAGS: ").append(Build.TAGS).append('\n');
        sb.append("TYPE: ").append(Build.TYPE).append('\n');
        sb.append("USER: ").append(Build.USER).append('\n');

        return sb.toString();
    }
}
