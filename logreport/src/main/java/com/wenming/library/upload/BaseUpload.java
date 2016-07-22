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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 抽象的日志报告类
 */
public abstract class BaseUpload implements ILogUpload {
    public Context mContext;

    public final static SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());

    public BaseUpload(Context context) {
        mContext = context;
    }

    /**
     * 发送报告
     *
     * @param title 报告标题
     * @param body  报告正文，为设备信息及安装包的版本信息
     * @param file  崩溃日志
     */
    protected abstract void sendReport(String title, String body, File file, OnUploadFinishedListener onUploadFinishedListener);

    @Override
    public void sendFile(final File file, final String content, final OnUploadFinishedListener onUploadFinishedListener) {
        sendReport(buildTitle(mContext), buildBody(mContext, content), file, onUploadFinishedListener);
    }

    /**
     * 构建标题
     *
     * @param context 上下文
     * @return 返回标题的string文本
     */
    public String buildTitle(Context context) {
        return "【CrashLog】  " + context.getString(context.getApplicationInfo().labelRes) + " " + yyyy_MM_dd_HH_mm_ss_SS.format(Calendar.getInstance().getTime());
    }

    /**
     * 构建正文
     *
     * @param context 上下文
     * @return 返回正文的string文本
     */
    public String buildBody(Context context, String content) {
        return content;
    }


}
