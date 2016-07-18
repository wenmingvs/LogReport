/*
 * @(#)CrashEmailReport.java		       Project: CrashHandler
 * Date: 2014-5-27
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
package com.wenming.library.upload.email;

import android.content.Context;

import com.wenming.library.upload.BaseUpload;

import java.io.File;

/**
 * 已经实现的日志报告类，这里通过邮件方式发送日志报告
 */
public class EmailReporter extends BaseUpload {
    private String mReceiveEmail;
    private String mSendEmail;
    private String mSendPassword;
    private String mHost;
    private String mPort;

    public EmailReporter(Context context) {
        super(context);
    }

    /**
     * 设置接收者
     *
     * @param receiveEmail
     */
    public void setReceiver(String receiveEmail) {
        mReceiveEmail = receiveEmail;
    }

    /**
     * 设置发送者邮箱
     *
     * @param email
     */
    public void setSender(String email) {
        mSendEmail = email;
    }

    /**
     * 设置发送者密码
     *
     * @param password
     */
    public void setSendPassword(String password) {
        mSendPassword = password;
    }

    /**
     * 设置SMTP 主机
     *
     * @param host
     */
    public void setSMTPHost(String host) {
        mHost = host;
    }

    /**
     * 设置端口
     *
     * @param port
     */
    public void setPort(String port) {
        mPort = port;
    }

    @Override
    protected void sendReport(String title, String body, File file, OnUploadFinishedListener onUploadFinishedListener) {
        MailInfo sender = new MailInfo()
                .setUser(mSendEmail)
                .setPass(mSendPassword)
                .setFrom(mSendEmail)
                .setTo(mReceiveEmail)
                .setHost(mHost)
                .setPort(mPort)
                .setSubject(title)
                .setBody(body);
        sender.init();
        try {
            sender.addAttachment(file.getPath(), file.getName());
            sender.send();
            onUploadFinishedListener.onSuceess();
        } catch (Exception e) {
            onUploadFinishedListener.onError(e.getMessage().toString());
            e.printStackTrace();
        }
    }
}
