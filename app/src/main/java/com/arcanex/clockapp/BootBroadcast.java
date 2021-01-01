package com.arcanex.clockapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

public class BootBroadcast extends BroadcastReceiver {
    Context context;

    public void onReceive(final Context context2, Intent intent) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= 26) {
                    context2.startForegroundService(new Intent(context2, MyService.class));
                }
            }
        });
    }
}
