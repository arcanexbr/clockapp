package com.arcanex.clockapp;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.transition.Explode;
import android.transition.Fade;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;

import static com.arcanex.clockapp.MainActivity.internetConnection;

import static com.arcanex.clockapp.MainActivity.prefs;
import static com.arcanex.clockapp.MainActivity.service;
import static com.arcanex.clockapp.MainActivity.ssid;
import static com.arcanex.clockapp.MainActivity.wifi_password;



public class SettingsActivity extends PreferenceActivity {
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        addPreferencesFromResource(R.xml.application_settings);












        findPreference("server").setSummary(prefs.getString("server", "none"));
        findPreference("port").setSummary(prefs.getString("port", "none"));
        findPreference("user").setSummary(prefs.getString("user", "none"));
        findPreference("mqtt_password").setSummary(prefs.getString("mqtt_password", "none"));
        findPreference("autoConnect_timer").setSummary("Текущее значение - " + prefs.getString("autoConnect_timer", "10") + " секунд");
        findPreference("ssid").setSummary(prefs.getString("ssid", "none"));
        findPreference("wifi_password").setSummary(prefs.getString("wifi_password", "none"));










        findPreference("port").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString());
                service.internetConnection.internet_port = o.toString();
                service.internetConnection.connect();
                return true;
            }
        });
        findPreference("server").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString());
                service.internetConnection.internet_server = o.toString();
                service.internetConnection.connect();
                return true;
            }
        });
        findPreference("user").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString());
                service.internetConnection.internet_user = o.toString();
                service.internetConnection.connect();
                return true;
            }
        });
        findPreference("mqtt_password").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString());
                service.internetConnection.internet_password = o.toString();
                service.internetConnection.connect();

                return true;
            }
        });
        findPreference("autoConnect_switch").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                service.internetConnection.internet_autoconnection = (boolean) o;
                if ((boolean) o) {
                    service.internetConnection.connect();
                }
                return true;
            }
        });
        findPreference("autoConnect_timer").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                service.internetConnection.internet_autoconnection_time_in_sec = Integer.parseInt(o.toString());
                preference.setSummary("Текущее значение - " + o.toString() + " секунд");
                return true;
            }
        });
        findPreference("ssid").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                ssid = o.toString();
                preference.setSummary(o.toString());
                return true;
            }
        });
        findPreference("wifi_password").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                wifi_password = o.toString();
                preference.setSummary(o.toString());
                return true;
            }
        });
        findPreference("sendAuthData").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
               final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Service.WIFI_SERVICE);
                System.out.println(wifiManager.getConnectionInfo().getSSID());
                if (wifiManager.getConnectionInfo().getSSID().equals("\"Nightlight\"")) {
                    MainActivity.localConnection.sendAuthData(SettingsActivity.this, new LocalConnection.LocalConnecionListener() {
                        @Override
                        public void onSuccessSend() {
                            Toast.makeText(getApplicationContext(), "Данные отправлены через сеть " + wifiManager.getConnectionInfo().getSSID(), Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onNoCallback() {

                        }
                    });
                }


                return true;
            }
        });


        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {


            }
        };


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
