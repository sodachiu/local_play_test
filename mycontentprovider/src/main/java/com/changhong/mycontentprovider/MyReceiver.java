package com.changhong.mycontentprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "qll_receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");

        String action = intent.getAction();
        if (action.equals("com.changhong.netchecking.start")) {
            Log.i(TAG, "onReceive: 读取到了广播，启动service");

            Intent intent1 = new Intent(context, MyService.class);
            context.startService(intent1);
        }
    }
}
