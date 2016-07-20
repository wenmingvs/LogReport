package com.wenming.library.crash;

import com.wenming.library.save.ISave;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 自定义的崩溃捕获Handler
 * Created by wenmingvs on 2016/7/4.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();

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
     * @param logSaver 保存的方式
     */
    public void init(ISave logSaver) {
        mSave = logSaver;
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
        String stringBuilder = "↓↓↓↓exception↓↓↓↓\n" +
                writer.toString();
        mSave.writeCrash(thread, ex, TAG, stringBuilder);
        // 如果处理了，让主程序继续运行3秒再退出，保证异步的写操作能及时完成
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


