package com.arcanex.clockapp;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static com.arcanex.clockapp.MainActivity.connection;
import static com.arcanex.clockapp.MainActivity.internet_autoconnection_time_in_sec;
import static com.arcanex.clockapp.MainActivity.internet_password;
import static com.arcanex.clockapp.MainActivity.internet_port;
import static com.arcanex.clockapp.MainActivity.internet_server;
import static com.arcanex.clockapp.MainActivity.internet_user;
import static com.arcanex.clockapp.MainActivity.prefs;
import static com.arcanex.clockapp.MainActivity.updateUI;


public class InternetConnection extends Connection {
    private MqttAndroidClient client;
    public IMqttToken token;
    public void connect(final Context context, final IMqttActionListener iMqttActionListener) {


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
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, serverURL , clientId);


        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(internet_user);
        options.setPassword(internet_password.toCharArray());

        try {
            token = client.connect(options);
            token.setActionCallback(iMqttActionListener);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Toast.makeText(context, cause.getMessage(), Toast.LENGTH_LONG).show();

                    class Reconnction extends Thread {
                        @Override
                        public void run() {
                            while (!connection.client.isConnected()) {
                                connect(context, iMqttActionListener);
                                try {
                                    sleep(internet_autoconnection_time_in_sec * 1000);
                                } catch (InterruptedException a) {
                                    a.printStackTrace();
                                }
                            }
                        }
                    }
                    Reconnction reconnction = new Reconnction();
                    reconnction.start();

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });


        } catch (MqttException e) {
            e.printStackTrace();
        }

    }





    public void disconnect() {
        if (client.isConnected()) {
            try {
                client.disconnect();
            } catch (MqttException a) {
                a.printStackTrace();
            }
        }
    }

}
