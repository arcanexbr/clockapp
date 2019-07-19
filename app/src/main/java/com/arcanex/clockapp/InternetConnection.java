package com.arcanex.clockapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static com.arcanex.clockapp.MainActivity.ESPConnectedToMQTT;
import static com.arcanex.clockapp.MainActivity.internet_autoconnection;
import static com.arcanex.clockapp.MainActivity.internet_autoconnection_time_in_sec;
import static com.arcanex.clockapp.MainActivity.internet_password;
import static com.arcanex.clockapp.MainActivity.internet_port;
import static com.arcanex.clockapp.MainActivity.internet_server;
import static com.arcanex.clockapp.MainActivity.internet_user;



public class InternetConnection extends Connection {
    public MqttAndroidClient client;
    public IMqttToken token;
    public  MqttConnectOptions options;
    public Context context;
    public IMqttActionListener iMqttActionListener;
    public MqttCallback mqttCallback;
    public Activity activity;

    public InternetConnection (final Activity activity, IMqttActionListener iMqttActionListener, MqttCallback mqttCallback) {
        this.context = activity.getApplicationContext();
        this.iMqttActionListener = iMqttActionListener;
        this.mqttCallback = mqttCallback;
        this.activity = activity;
    }


    public void connect() {


        if (client != null) {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (MqttException a) {
                    a.printStackTrace();
                }
            }
        }





        String serverURL = "tcp://" + internet_server + ":" + internet_port;
        final String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, serverURL , clientId);
        options = new MqttConnectOptions();
        options.setUserName(internet_user);
        options.setPassword(internet_password.toCharArray());

        Connect connect = new Connect();
        connect.start();

    }

 void checkESPInMQTT() {
        checkESPInMQTTThread checkESPInMQTTThread = new checkESPInMQTTThread();
        checkESPInMQTTThread.start();
 }





    class Connect extends Thread {
        @Override
        public void run() {
            try {
                client.setCallback(mqttCallback);
                token = client.connect(options);
                token.setActionCallback(iMqttActionListener);
                while (internet_autoconnection & !client.isConnected()) {
                    sleep(internet_autoconnection_time_in_sec*1000);
                    if (!client.isConnected()) {
                        token = client.connect(options);
                        token.setActionCallback(iMqttActionListener);
                    }
                }



            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    class checkESPInMQTTThread extends Thread {
        @Override
        public void run() {
            int i = 0;
            MqttMessage message = new MqttMessage();
            message.setPayload("hey, esp?".getBytes());
            while (!ESPConnectedToMQTT && i < 3) {
                i++;
                try {
                    client.publish("call_ESP", message);
                } catch (Exception a) {
                    a.printStackTrace();

                }
                try {
                    sleep(2000);
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
            i=0;
            if (!ESPConnectedToMQTT) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Как такое могло произойти?")
                                .setMessage("Дошел слух, что Nighlight не смог подключиться к сети Wi-Fi.\n\n" +
                                        "Введи в настройках верные логин и пароль от своего Wi-Fi и подключись к хот-споту, если не сложно. (А может Nightlight просто выключен...?)")
                                .setCancelable(false)
                                .setPositiveButton("Ок, ща",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                activity.startActivity(new Intent(activity, SettingsActivity.class));
                                            }
                                        })
                        .setNegativeButton("Я сейчас не могу", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

            }
        }
    }



}
