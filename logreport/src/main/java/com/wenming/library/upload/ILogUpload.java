/*
 * @(#)CrashListener.java		       Project: crash
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

import java.io.File;

/**
 * 上传日志的接口
 */
public interface ILogUpload {

    interface OnUploadFinishedListener {
        void onSuceess();


        void onError(String error);
    }


    /**
     * 发送日志文件。
     *
     * @param file 发送的文件
     */
    void sendFile(File file, String content, OnUploadFinishedListener onUploadFinishedListener);

}