package com.wenming.library.crash;

import android.content.Context;

import com.wenming.library.save.CrashWriter3;
import com.wenming.library.save.ISave;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by wenmingvs on 2016/7/4.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static final String mCrashType = ".txt";
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 设置日志的保存方式
     */
    private ISave mSave;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化,，设置此CrashHandler来响应崩溃事件
     *
     * @param context
     * @param logSaver
     */
    public void init(Context context, ISave logSaver) {
        mContext = context;
        mSave = logSaver;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("↓↓↓↓exception↓↓↓↓\n");
        stringBuilder.append(writer.toString());
        mSave.writeCrash(thread, ex, TAG, stringBuilder.toString());

        if (mSave instanceof CrashWriter3) {
            try {
                thread.sleep(6000);// 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                try {
//                    mSave.closeApp(thread, ex);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Looper.loop();
//            }
//        }).start();


    }
}


