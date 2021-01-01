package com.arcanex.clockapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;




public class InternetConnection extends Connection {
    private MqttAndroidClient client;
    private IMqttToken token;
    private MqttConnectOptions options;
    private Context context;
    private IMqttActionListener iMqttActionListener;
    private MqttCallback mqttCallback;
    private ConnectivityManager connectivityManager;
    private TryConnectThread tryConnectThread;
    static String internet_port;
    static String internet_server;
    static String internet_user;
    static String internet_password;
    static int internet_autoconnection_time_in_sec;
    static boolean internet_autoconnection;

    public InternetConnection (Context context, IMqttActionListener iMqttActionListener, MqttCallback mqttCallback) {
        this.context = context;
        this.iMqttActionListener = iMqttActionListener;
        this.mqttCallback = mqttCallback;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }



    public void connect() {

        if (tryConnectThread == null ||!tryConnectThread.isAlive()) {
            tryConnectThread = new TryConnectThread();
            tryConnectThread.start();
        }


    }
    public void sendData(String topic, String data){
        if (client.isConnected()){
            try {
                client.publish(topic, data.getBytes(), 1, false);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }





    private class TryConnectThread extends Thread {
        @Override
        public void run() {
            if (client != null && client.isConnected()){
                try {
                    client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            do {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected() || (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected() && wifiManager.getConnectionInfo().getSSID() != context.getString(R.string.espWifiName))) {
                    String serverURL = "tcp://" + internet_server + ":" + internet_port;
                    final String clientId = MqttClient.generateClientId();
                    client = new MqttAndroidClient(context, serverURL , clientId);
                    options = new MqttConnectOptions();
                    options.setUserName(internet_user);
                    options.setPassword(internet_password.toCharArray());
                    try {

                        client.setCallback(mqttCallback);
                        token = client.connect(options);
                        token.setActionCallback(iMqttActionListener);
                        sleep(1000*internet_autoconnection_time_in_sec);




                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                } else {
                    try {
                        sleep(1000*internet_autoconnection_time_in_sec);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while ((client == null || !client.isConnected()) && internet_autoconnection);
        }
    }


}