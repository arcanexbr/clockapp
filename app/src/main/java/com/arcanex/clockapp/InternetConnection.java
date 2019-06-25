package com.arcanex.clockapp;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

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

    public InternetConnection (final Context context, IMqttActionListener iMqttActionListener, MqttCallback mqttCallback) {
        this.context = context;
        this.iMqttActionListener = iMqttActionListener;
        this.mqttCallback = mqttCallback;
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



}
