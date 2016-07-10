package com.wenming.library;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by wenmingvs on 2016/7/9.
 */
public class LogService extends Service {

    public static final String TAG = "LogService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        return super.onStartCommand(intent, flags, startId);
    }


}
