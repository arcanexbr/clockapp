package com.arcanex.clockapp;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.media.browse.MediaBrowser;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telecom.ConnectionRequest;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;

import androidx.constraintlayout.widget.Constraints;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;


import com.github.florent37.shapeofview.shapes.PolygonView;
import com.github.florent37.shapeofview.shapes.RoundRectView;


import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.FragmentManager;


import java.util.concurrent.Executor;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;



public class MainActivity extends Activity  {
    static SharedPreferences prefs;
    static InternetConnection internetConnection;
    static LocalConnection localConnection;
    static MyService service;
    static Location homeLocation;
    ServiceConnection sc;
    boolean on = true;

    static String ssid;
    static String wifi_password;

    static boolean ESPConnectedToMQTT;

    static String[] MQTTsubscribes = {"user_ddefdaed/geolocation", "user_ddefdaed/brightest", "user_ddefdaed/color", "user_ddefdaed/color_mode", "user_ddefdaed/message_from_creator", "user_ddefdaed/melodi", "user_ddefdaed/sleep", "user_ddefdaed/on_info", "user_ddefdaed/weather_sens"};
    Document document;




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

        super.onCreate(savedInstanceState);





        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);



        setContentView(R.layout.activity_main);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            startService(new Intent(this, MyService.class));
        }


        ListView main_butons = findViewById(R.id.main_buttons);
        TextView logo = findViewById(R.id.textView2);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (on) {
                    service.internetConnection.sendData("user_ddefdaed/on", "0");
                    on = false;
                } else {
                    service.internetConnection.sendData("user_ddefdaed/on", "1");
                    on = true;
                }
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Управление цветовой палитрой", "Расписание", "Специальные возможности" });
        main_butons.setAdapter(arrayAdapter);
        main_butons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, ColorPickActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        overridePendingTransition(0, 0);
                        break;
                    case 2:

                      startActivity(new Intent(MainActivity.this, SpecialFunctionActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                      overridePendingTransition(0, 0);
                      break;
                    case 1:



                }
            }
        });






        if (getIntent().hasExtra("activity") && getIntent().getStringExtra("activity").equals("Start") && prefs.getBoolean("first_launch", true)) {


            new GuideView.Builder(this)
                    .setTargetView(findViewById(R.id.textView2))
                    .setContentText("Кнопка включения и выключения ночника.")
                    .setDismissType(DismissType.anywhere)
                    .setGuideListener(new GuideListener() {
                        @Override
                        public void onDismiss(View view) {
                            new GuideView.Builder(MainActivity.this)
                                    .setTargetView(findViewById(R.id.button_settings))
                                    .setDismissType(DismissType.anywhere)
                                    .setContentText("Настройки подключения и т.д.")
                                    .setGuideListener(new GuideListener() {
                                        @Override
                                        public void onDismiss(View view) {
                                            new GuideView.Builder(MainActivity.this)
                                                    .setTargetView(findViewById(R.id.main_buttons))
                                                    .setDismissType(DismissType.anywhere)
                                                    .setContentText("Панель управления NIGHTLIGHT")
                                                    .setGuideListener(new GuideListener() {
                                                        @Override
                                                        public void onDismiss(View view) {
                                                            new GuideView.Builder(MainActivity.this)
                                                                    .setTargetView(findViewById(R.id.button_menu))
                                                                    .setContentText("Прежде чем виртуальный я с тобой расстанется и отдаст полный контроль над приложением, загляни сюда.")
                                                                    .setDismissType(DismissType.anywhere)
                                                                    .build().show();
                                                            prefs.edit().putBoolean("first_launch", false).apply();
                                                        }
                                                    })
                                                    .build().show();
                                        }
                                    })
                                    .build().show();
                        }
                    })
                    .build().show();



        } else
        if(prefs.getBoolean("first_launch", true)) {

            startActivity(new Intent(MainActivity.this, StartActivity.class));
        }
            startService(new Intent(this, MyService.class));




            sc = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    Toast.makeText(MainActivity.this, "ServiceConnected", Toast.LENGTH_SHORT).show();
                    service = ((MyService.MyBinder) iBinder).getService();

                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {

                }
            };

            bindService(new Intent(this, MyService.class), sc, BIND_AUTO_CREATE);







        localConnection = new LocalConnection(MainActivity.this);


















        settingsSynchornize();





                findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        overridePendingTransition(0, 0);

                    }

                });
                findViewById(R.id.button_menu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, AboutActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        overridePendingTransition(0, 0);
                    }
                });





    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onResume() {


        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unbindService(sc);
        super.onDestroy();
    }

    public void settingsSynchornize() {


        wifi_password = prefs.getString("wifi_password", "nope");
        ssid = prefs.getString("ssid", "nope");
    }


}
