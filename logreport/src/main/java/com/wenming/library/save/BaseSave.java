package com.wenming.library.save;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.wenming.library.LogReport;
import com.wenming.library.encryption.IEncryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 提供通用的保存操作log的日志和设备信息的方法
 * Created by wenmingvs on 2016/7/9.
 */
public abstract class BaseSave implements ISave {

    private final static String TAG = "BaseSave";


    /**
     * 根据日期创建文件夹,文件夹的名称以日期命名,下面是日期的格式
     */
    public final static SimpleDateFormat CREATE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * 在每一条log前面增加一个时间戳
     */
    public final static SimpleDateFormat LOG_FOLDER_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());


    /**
     * 日志的保存的类型
     */
    public static final String SAVE_FILE_TYPE = ".txt";

    /**
     * 日志命名的其中一部分：时间戳
     */
    public final static String LOG_CREATE_TIME = CREATE_DATE_FORMAT.format(new Date(System.currentTimeMillis()));

    public static String LOG_DIR;

    /**
     * 操作日志全名拼接
     */
    public final static String LOG_FILE_NAME_MONITOR = "MonitorLog" + LOG_CREATE_TIME + SAVE_FILE_TYPE;

    public Context mContext;

    /**
     * 加密方式
     */
    public static IEncryption mEncryption;

    public BaseSave(Context context) {
        this.mContext = context;
    }

    /**
     * 写入设备的各种参数信息之前，请确保File文件以及他的父路径是存在的
     *
     * @param file
     */
    public File createFile(File file, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Application Information").append('\n');
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = context.getApplicationInfo();
        sb.append("App Name : ").append(pm.getApplicationLabel(ai)).append('\n');
        try {
            PackageInfo pi = pm.getPackageInfo(ai.packageName, 0);
            sb.append("Version Code: ").append(pi.versionCode).append('\n');
            sb.append("Version Name: ").append(pi.versionName).append('\n');
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sb.append('\n');
        sb.append("DEVICE INFORMATION").append('\n');
        sb.append("BOOTLOADER: ").append(Build.BOOTLOADER).append('\n');
        sb.append("BRAND: ").append(Build.BRAND).append('\n');
        sb.append("DEVICE: ").append(Build.DEVICE).append('\n');
        sb.append("HARDWARE: ").append(Build.HARDWARE).append('\n').append('\n');

        // TODO 支持添加更多信息

        Log.d("wenming", "创建的设备信息（加密前） = " + sb.toString());
        //加密信息
        sb = new StringBuilder(encodeString(sb.toString()));
        Log.d("wenming", "创建的设备信息（加密后） = " + sb.toString());
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public File writeLog(String tag, String content) {
        LOG_DIR =
            LogReport.LOGDIR + File.separator + "Log" + File.separator
                + CREATE_DATE_FORMAT.format(new Date(System.currentTimeMillis())) + File.separator;
        File logsDir = new File(LOG_DIR);
        RandomAccessFile randomAccessFile = null;
        File logFile = new File(logsDir, LOG_FILE_NAME_MONITOR);
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (!logsDir.exists()) {
                    Log.d("wenming", "logsDir.mkdirs() =  +　" + logsDir.mkdirs());
                }
                if (!logFile.exists()) {
                    createFile(logFile, mContext);
                }
                //读取文件中的文本内容，并且解密
                StringBuilder preContent = new StringBuilder(decodeString(getText(logFile)));
                Log.d("wenming", "读取本地的Log文件，并且解密 = \n" + preContent.toString());
                //添加log内容
                preContent.append(formatLogMsg(tag, content) + "\n");

                Log.d("wenming", "即将保存的Log文件内容 = \n" + preContent.toString());
                saveText(logFile, preContent.toString());

                // saveText(logFile,preContent.toString());
                // //加密后保存回去
                // randomAccessFile = new RandomAccessFile(logFile, "rw");
                // //清空本地内容
                // randomAccessFile.setLength(0);
                // //重新保存回去
                // randomAccessFile.write(mEncryption.decrypt(preContent.toString()).getBytes());
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return logFile;
    }

    @Override
    public void setEncodeType(IEncryption encodeType) {
        mEncryption = encodeType;
    }

    public static String encodeString(String content) {
        if (mEncryption != null) {
            try {
                return mEncryption.encrypt(content);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                return content;
            }
        }

        return content;

    }

    public static String decodeString(String content) {
        if (mEncryption != null) {
            try {
                return mEncryption.decrypt(content);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                return content;
            }
        }
        return content;
    }

    public String getText(File file) {
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            // You'll need to add proper error handling here
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return text.toString();
    }

    // byte Buffer[] = new byte[1024];
    // // 得到文件输入流
    // FileInputStream in = null;
    // ByteArrayOutputStream outputStream = null;
    // try {
    // in = new FileInputStream(file);
    // // 读出来的数据首先放入缓冲区，满了之后再写到字符输出流中
    // int len = in.read(Buffer);
    // // 创建一个字节数组输出流
    // outputStream = new ByteArrayOutputStream();
    // outputStream.write(Buffer, 0, len);
    // // 把字节输出流转String
    // Log.d("wenming", "读取log文件的加密内容：\n" + new String(outputStream.toByteArray()));
    // return new String(outputStream.toByteArray());
    // } catch (Exception e) {
    // Log.e(TAG, e.toString());
    // e.printStackTrace();
    // } finally {
    // if (in != null) {
    // try {
    // in.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // if (outputStream != null) {
    // try {
    // outputStream.flush();
    // outputStream.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // return null;

    public boolean saveText(File logFile, String content) {
        FileOutputStream outputStream = null;
        try {
            content = encodeString(content);
            Log.d("wenming", "最终写到文本的加密Log：\n" + content);
            outputStream = new FileOutputStream(logFile);
            outputStream.write(content.getBytes("UTF-8"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 用于在每条log前面，增加更多的文本信息，包括时间，线程名字等等
     */
    public static String formatLogMsg(String tag, String tips) {
        String timeStr = LOG_FOLDER_TIME_FORMAT.format(Calendar.getInstance().getTime());
        Thread currThread = Thread.currentThread();
        StringBuilder sb = new StringBuilder();
        sb.append("Trd: ")
            .append(currThread.getId())
            .append(" ")
            .append(currThread.getName())
            .append(" ")
            .append(timeStr)
            .append(" Class: ")
            .append(tag)
            .append(" > ")
            .append(tips);
        Log.d("wenming", "添加的内容是:" + sb.toString());
        return sb.toString();
    }

}
