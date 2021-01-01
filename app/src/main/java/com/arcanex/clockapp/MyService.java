package com.arcanex.clockapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
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

import androidx.core.app.ActivityCompat;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.BitmapCompat;
import androidx.preference.PreferenceManager;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static com.arcanex.clockapp.MainActivity.service;


public class MyService extends Service implements MqttCallback, IMqttActionListener {
    Intent intent;
    static SharedPreferences preferences;
    InternetConnection internetConnection;
    LocationHelper locationHelper;
    String cause;
    NotificationManager notificationManager;
    int importance = NotificationManager.IMPORTANCE_HIGH;
    NotificationChannel channel;
    NotificationCompat.Builder builder;


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
        locationHelper = new LocationHelper(getApplicationContext());
        notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("dadayaebat", "Оповещение о состоянии ночника", importance);


            notificationManager.createNotificationChannel(channel);
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         //   statusNotif("Установи разрешения приложения", "Геолокация не может работать без разрешения. Перейди в настройки и дай доступ мне!!!");
        }




        internetConnection = new InternetConnection(getApplicationContext(), this, this);
        internetConnection.connect();















    }
    @SuppressLint("MissingPermission")
    public void statusNotif(String titile, String text){
        String coordinates = new String();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            coordinates = String.valueOf(locationHelper.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).distanceTo(locationHelper.homeLocaton))  + " " + String.valueOf(locationHelper.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).distanceTo(locationHelper.homeLocaton));
        }

        builder  = new NotificationCompat.Builder(getApplicationContext(), "dadayaebat");
                builder.setSmallIcon(R.drawable.ic_stat_n)
                .setContentTitle(titile + " " + coordinates)
                .setContentText(text)

                        .setOngoing(false)
                .setColorized(true)
                .setColor(255)
                        .setTimeoutAfter(600000)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(text))



                .setPriority(2)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Notification notification = builder.build();
        notificationManager.notify(12, notification);
    }
    @SuppressLint("MissingPermission")
    public void statusNotif(String titile, String text, String cause){
        builder  = new NotificationCompat.Builder(getApplicationContext(), "dadayaebat");
        builder.setSmallIcon(R.drawable.ic_stat_n)
                .setContentTitle(titile + " " + String.valueOf(locationHelper.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).distanceTo(locationHelper.homeLocaton)) + " " + String.valueOf(locationHelper.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).distanceTo(locationHelper.homeLocaton)))
                .setContentText(cause + " " + text)

                .setOngoing(false)
                .setColorized(true)
                .setColor(255)
                .setTimeoutAfter(600000)


                .setPriority(2)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Notification notification = builder.build();
        notificationManager.notify(12, notification);
    }



    RemoteViews remoteViews;



    public void makeNotif(){
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("dadayaebat1", "Nightlight Service", importance);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }

        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);


        remoteViews.setOnClickPendingIntent(R.id.notification, PendingIntent.getActivity(getApplicationContext(), 1, new Intent(this, MainActivity.class), Intent.FILL_IN_ACTION));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this,"dadayaebat1")
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

        internetConnection.connect();

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Toast.makeText(getApplicationContext(), topic, Toast.LENGTH_LONG).show();

        if (topic.equals("user_ddefdaed/sleep")) {
            if (new String(message.getPayload()).equals("1")) {
                statusNotif("Ночник переведен в режим сна", "Я пока посплю. Только возвращайся скорее...");
            } else {
                statusNotif("Ночник включен", "Ты близко - а значит, мне снова нужно сиять.");
            }

        } else if (topic.equals("user_ddefdaed/on_info")) {
            if (new String(message.getPayload()).equals("0")) {
                statusNotif("Ночник переведен в режим сна", "I'll be back.");
            } else {
                statusNotif("Ночник включен", "Если меня зажигают, значит это кому-нибудь нужно.");
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }





    @Override
    public void onSuccess(IMqttToken asyncActionToken) {

        internetConnection.sendData("user_ddefdaed/kekandos", "EBAT ONO RABOTAET");
        try {
            asyncActionToken.getClient().subscribe(MainActivity.MQTTsubscribes, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1});

        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

    }

    class MyBinder extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }
    public void settingsSynchornize() {
        internetConnection.internet_port = preferences.getString("port", "9123");
        internetConnection.internet_server = preferences.getString("server", "srv1.clusterfly.ru");
        internetConnection.internet_user = preferences.getString("user", "user_ddefdaed");
        internetConnection.internet_password = preferences.getString("mqtt_password", "pass_5e1138a4");
        internetConnection.internet_autoconnection = preferences.getBoolean("autoConnect_switch", true);
        internetConnection.internet_autoconnection_time_in_sec = Integer.parseInt(preferences.getString("autoConnect_timer", "10"));

    }
    public static class DownloadCancelReceiver extends BroadcastReceiver {
        @Override
    public void onReceive(Context context, Intent intent) {
Toast.makeText(context, "JABA",Toast.LENGTH_LONG).show();
    }

    }
}
