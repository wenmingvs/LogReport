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
     * @param receiveEmail 设置接收者
     */
    public void setReceiver(String receiveEmail) {
        mReceiveEmail = receiveEmail;
    }

    /**
     * @param email 设置发送者邮箱
     */
    public void setSender(String email) {
        mSendEmail = email;
    }

    /**
     * @param password 设置发送者密码
     */
    public void setSendPassword(String password) {
        mSendPassword = password;
    }

    /**
     * @param host 设置SMTP 主机
     */
    public void setSMTPHost(String host) {
        mHost = host;
    }

    /**
     * @param port 设置端口
     */
    public void setPort(String port) {
        mPort = port;
    }

    /**
     * @param title                    报告标题
     * @param body                     报告正文，为设备信息及安装包的版本信息
     * @param file                     崩溃日志，发送压缩包
     * @param onUploadFinishedListener 成功与否的回调
     */
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
            onUploadFinishedListener.onError("Send Email fail！Accout or SMTP verification error ！");
            e.printStackTrace();
        }
    }
}
