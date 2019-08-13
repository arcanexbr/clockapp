package com.arcanex.clockapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import static android.os.Looper.getMainLooper;


public class BootBroadcast extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, MyService.class));
                }
            }
        });



    }

}
