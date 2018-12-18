package com.changhong.netchecking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class NetCheckService extends Service {

    private static final String TAG = "qll_net_check_service";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 代码逻辑
        Log.i(TAG, "onStartCommand: intent----" + intent);
        Log.i(TAG, "onStartCommand: flags----" + flags);
        Log.i(TAG, "onStartCommand: startId----" + startId);

        //

        return super.onStartCommand(intent, flags, startId);
    }
}
