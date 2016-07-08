package com.wenming.library.save;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 此类区别于SingleCrash，是将崩溃信息都写在一个文件中
 * Created by wenmingvs on 2016/7/7.
 */
public class CrashAllInOne implements ISave {

    private final static String TAG = "CrashAllInOne";
    /**
     * 日志的保存路径
     */
    private static final String LOG_DIR = "sdcard/aaaLog";
    /**
     * 用于格式化日期,作为日志文件名的一部分，日志的格式为：2017-08-23
     */
    private final static SimpleDateFormat LOG_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());
    /**
     * 用于显示日志，作为日志命名的一部分
     */
    private final static SimpleDateFormat LOG_CREATE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    /**
     * 日志的保存的类型
     */
    private static final String SAVE_TYPE = ".txt";

    /**
     * 时间戳
     */
    private final static String LOG_CREATE_TIME = LOG_CREATE_DATE_FORMAT.format(new Date(System.currentTimeMillis()));
    /**
     * 用于命名崩溃文件
     */
    public final static String LOG_FILE_NAME_EXCEPTION = "CrashLog" + LOG_CREATE_TIME + SAVE_TYPE;

    public final static String LOG_FILE_NAME_MONITOR = "MonitorLog" + LOG_CREATE_TIME + SAVE_TYPE;


    private Context mContext;

    public CrashAllInOne(Context context) {
        this.mContext = context;
    }

    /**
     * 创建文件，并且写入设备的各种参数信息
     *
     * @param file
     */
    @Override
    public void writePhoneInfo(File file, Context context) {
        Map<String, String> infos = new HashMap<String, String>();
        PackageManager pm = context.getPackageManager();
        try {
            file.createNewFile();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        try {
            long timestamp = System.currentTimeMillis();
            String time = LOG_CREATE_DATE_FORMAT.format(new Date());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
    }

    @Override
    public File writeLogToFile(String logFileName, String tag, String tips) {
        RandomAccessFile randomAccessFile = null;
        File logFile = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File logsDir = new File(LOG_DIR);
                if (!logsDir.exists()) {
                    logsDir.mkdirs();
                }
                logFile = new File(logsDir, logFileName);
                if (!logFile.exists()) {
                    writePhoneInfo(logFile, mContext);
                }
                randomAccessFile = new RandomAccessFile(logFile, "rw");
                randomAccessFile.seek(logFile.length());
                randomAccessFile.write(("\r\n" + formatLogMsg(tag, tips)).getBytes());
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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

    /**
     * 用于在每条log前面，增加更多的文本信息，包括时间，线程名字等等
     */
    private static String formatLogMsg(String tag, String tips) {
        final String timeStr = LOG_TIME_FORMAT.format(Calendar.getInstance().getTime());
        final Thread currThread = Thread.currentThread();
        final StringBuilder sb = new StringBuilder(100);
        sb.append("Trd: ").append(currThread.getId()).append(" ").append(currThread.getName()).append(" ").append(timeStr).append(" Class: ").append(tag).append(" > ").append(tips);
        return sb.toString();
    }
}
