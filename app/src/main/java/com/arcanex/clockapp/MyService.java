package com.arcanex.clockapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.BitmapCompat;
import androidx.preference.PreferenceManager;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;



public class MyService extends Service implements MqttCallback, IMqttActionListener {
    Intent intent;
    static SharedPreferences preferences;
    InternetConnection internetConnection;
    LocationHelper locationHelper;


    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();

    }

    @Override
    public void onCreate() {
        makeNotif();
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        settingsSynchornize();

        internetConnection = new InternetConnection(getApplicationContext(), this, this);
        internetConnection.connect();
        locationHelper = new LocationHelper(getApplicationContext());











    }







    public void makeNotif(){
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("dadayaebat", "name", importance);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setOnClickPendingIntent(R.id.notification, PendingIntent.getActivity(getApplicationContext(), 1, new Intent(this, MainActivity.class), Intent.FILL_IN_ACTION));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this,"dadayaebat")
                .setSmallIcon(R.drawable.ic_cloud_off)
                .setContentTitle("test")
                .setPriority(2)

                .setCustomContentView(remoteViews)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        Notification notification = builder.build();

       startForeground(1, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();


        return START_STICKY;

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("dadayaebat", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this,"dadayaebat")
                .setSmallIcon(R.drawable.micro_usb)
                .setContentText("i'm killed ((onDestroy)")
                .setOngoing(false)
                .setPriority(2)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Notification notification = builder.build();
        notificationManager.notify(11, notification);
        Intent restartIntent = new Intent(this, getClass());
        PendingIntent pi = PendingIntent.getService(this, 1, restartIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setExact(AlarmManager.RTC, System.currentTimeMillis() + 10000, pi);
        super.onDestroy();
    }


    @Override
    public void connectionLost(Throwable cause) {
        Toast.makeText(this, "connectionLost", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Toast.makeText(this, "messageArrived", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }





    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Toast.makeText(this, "MQTT Connection: onSuccess", Toast.LENGTH_SHORT).show();
        internetConnection.sendData("test topic", "lalala");

    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Toast.makeText(this, "MQTT Connection: onFailure", Toast.LENGTH_SHORT).show();
    }

    class MyBinder extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }
    public void settingsSynchornize() {
        internetConnection.internet_port = preferences.getString("port", "10229");
        internetConnection.internet_server = preferences.getString("server", "m24.cloudmqtt.com");
        internetConnection.internet_user = preferences.getString("user", "wztfsknh");
        internetConnection.internet_password = preferences.getString("mqtt_password", "uazcTDCGNR6S");
        internetConnection.internet_autoconnection = preferences.getBoolean("autoConnect_switch", true);
        internetConnection.internet_autoconnection_time_in_sec = Integer.parseInt(preferences.getString("autoConnect_timer", "10"));

    }
}
