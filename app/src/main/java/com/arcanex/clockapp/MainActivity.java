package com.arcanex.clockapp;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.navigation.NavigationView;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_NETWORK_STATE;
import static android.Manifest.permission.CHANGE_WIFI_MULTICAST_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.SYSTEM_ALERT_WINDOW;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MainActivity extends Activity  {
    static SharedPreferences prefs;
    static InternetConnection internetConnection;
    static UpdateUI updateUI;
    static String internet_port;
    static String internet_server;
    static String internet_user;
    static String internet_password;
    static String ssid;
    static String wifi_password;
    static boolean internet_autoconnection;
    static boolean ESPConnectedToMQTT;
    static int internet_autoconnection_time_in_sec;
    static String[] MQTTsubscribes = {"call_ANDROID"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalConnection localConnection = new LocalConnection(MainActivity.this);
        if (checkSelfPermission(ACCESS_WIFI_STATE) != PERMISSION_GRANTED || checkSelfPermission(ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED || checkSelfPermission(ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
        requestPermissions(new String[]{ACCESS_WIFI_STATE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, 0);
        }
        Toast.makeText(getApplicationContext(), localConnection.wifiManager.getScanResults().get(2).SSID, Toast.LENGTH_LONG).show();






        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        settingsSynchornize();
        internetConnection = new InternetConnection(MainActivity.this, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                updateUI.updateInternetConnection(UpdateUI.INTERNET_CONNECTION_SUCCESFUL);
                try {
                    internetConnection.client.subscribe(MQTTsubscribes, new int[]{0});
                } catch (Exception a) {
                    a.printStackTrace();
                }

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                updateUI.updateInternetConnection(UpdateUI.INTERNET_CONNECTION_FAILED);

            }
        }, new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                updateUI.updateInternetConnection(UpdateUI.INTERNET_CONNECTION_PROCESS);
                if (cause!=null) {
                    Toast.makeText(getApplicationContext(), cause.getMessage(), Toast.LENGTH_LONG).show();
                    internetConnection.connect();
                }



            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("messagearrived");
            if(topic.equals("call_ANDROID")){
                System.out.println("callESP");
                ESPConnectedToMQTT = true;
                Toast.makeText(getApplicationContext(), "Nightlight удачно подключился к Wi-Fi", Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        updateUI = new UpdateUI(MainActivity.this);
        internetConnection.connect();
        internetConnection.checkESPInMQTT();
        findViewById(R.id.internet_connection_fail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI.updateInternetConnection(UpdateUI.INTERNET_CONNECTION_PROCESS);
                internetConnection.connect();

            }
        });
        findViewById(R.id.internet_connection_already).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI.updateInternetConnection(UpdateUI.INTERNET_CONNECTION_PROCESS);
                internetConnection.connect();
            }
        });

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

    public void settingsSynchornize() {
        internet_port = prefs.getString("port", "10229");
        internet_server = prefs.getString("server", "m24.cloudmqtt.com");
        internet_user = prefs.getString("user", "wztfsknh");
        internet_password = prefs.getString("password", "uazcTDCGNR6S");
        internet_autoconnection = prefs.getBoolean("autoConnect_switch", true);
        internet_autoconnection_time_in_sec = Integer.parseInt(prefs.getString("autoConnect_timer", "10"));
    }
}
