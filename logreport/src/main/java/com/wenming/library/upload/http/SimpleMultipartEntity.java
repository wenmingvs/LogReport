/*  
 * @(#)MultipartEntity.java              Project:androidkit  
 * Date:2013-5-5  
 *  
 * Copyright (c) 2013 Geek_Soledad.  
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

package com.wenming.library.upload.http;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;


public class SimpleMultipartEntity implements HttpEntity {

    /**
     * ASCII的字符池，用于生成分界线。
     */
    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();

    private ByteArrayOutputStream out;
    /**
     * 是否设置了开头的分界线
     */
    boolean isSetFirst = false;
    /**
     * 是否设置了最后的分界线
     */
    boolean isSetLast = false;

    /**
     * 分界线
     */
    private String boundary;

    public SimpleMultipartEntity() {
        out = new ByteArrayOutputStream();
        boundary = generateBoundary();
    }

    /**
     * 生成分界线
     *
     * @return 返回生成的分界线
     */
    protected String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }

    public void writeFirstBoundaryIfNeeds() {
        if (!isSetFirst) {
            try {
                out.write(("--" + boundary + "\r\n").getBytes());
            } catch (final IOException e) {
                Log.w(e.getMessage(), e);
            }
        }
        isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if (!isSetLast) {
            try {
                out.write(("\r\n--" + boundary + "--\r\n").getBytes());
            } catch (final IOException e) {
                Log.w(e.getMessage(), e);
            }
            isSetLast = true;
        }
    }

    public void addPart(final String key, final String value) {
        writeFirstBoundaryIfNeeds();
        try {
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
            out.write(value.getBytes());
            out.write(("\r\n--" + boundary + "\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void addPart(final String key, final String fileName, final InputStream fin,
                        final boolean isLast) {
        addPart(key, fileName, fin, "application/octet-stream", isLast);
    }

    public void addPart(final String key, final String fileName, final InputStream fin,
                        String type, final boolean isLast) {
        writeFirstBoundaryIfNeeds();
        try {
            type = "Content-Type: " + type + "\r\n";
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
                    + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

            final byte[] tmp = new byte[4096];
            int l;
            while ((l = fin.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            if (!isLast)
                out.write(("\r\n--" + boundary + "\r\n").getBytes());
            out.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPart(final String key, final File value, final boolean isLast) {
        try {
            addPart(key, value.getName(), new FileInputStream(value), isLast);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return out.toByteArray().length;
    }

    @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        outstream.write(out.toByteArray());
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
                    "Streaming entity does not implement #consumeContent()");
        }
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return new ByteArrayInputStream(out.toByteArray());
    }
}