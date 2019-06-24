package com.arcanex.clockapp;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;


public class MainActivity extends Activity  {
    static SharedPreferences prefs;
    static InternetConnection connection;
    static UpdateUI updateUI;
    static String internet_port;
    static String internet_server;
    static String internet_user;
    static String internet_password;
    static boolean internet_autoconnection;
    static int internet_autoconnection_time_in_sec;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        settingsSynchornize();
        connection = new InternetConnection();
        internetConnect();
        updateUI = new UpdateUI(MainActivity.this);

        findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        findViewById(R.id.button_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });






    }
    public void internetConnect() {

       IMqttActionListener iMqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                updateUI.updateInternetConnection(UpdateUI.INTERNET_CONNECTION_SUCCESFUL);

                findViewById(R.id.internet_connection_already).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateUI.updateInternetConnection(UpdateUI.INTERNET_CONNECTION_PROCESS);
                        internetConnect();
                    }
                });


            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                updateUI.updateInternetConnection(UpdateUI.INTERNET_CONNECTION_FAILED);
                findViewById(R.id.internet_connection_fail).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateUI.updateInternetConnection(UpdateUI.INTERNET_CONNECTION_PROCESS);
                        internetConnect();
                    }
                });


            }
        };
        connection.connect(getApplicationContext(), iMqttActionListener);


    }

    public void settingsSynchornize() {
        internet_port = prefs.getString("port", "10229");
        internet_server = prefs.getString("server", "m24.cloudmqtt.com");
        internet_user = prefs.getString("user", "wztfsknh");
        internet_password = prefs.getString("password", "uazcTDCGNR6S");
        internet_autoconnection = prefs.getBoolean("autoConnect_switch", true);
        internet_autoconnection_time_in_sec = Integer.parseInt(prefs.getString("autoConnect_timer", "10"));
    }
}