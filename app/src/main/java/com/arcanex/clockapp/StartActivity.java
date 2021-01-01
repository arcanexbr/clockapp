package com.arcanex.clockapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static java.lang.Thread.sleep;

public class StartActivity extends Activity {
  TextView textView;

  private void makeNotification() {
    NotificationManager notificationManager = getSystemService(NotificationManager.class);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = "name";
      String description = "describ";
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel("dadayaebat", name, importance);
      channel.setDescription(description);
      notificationManager.createNotificationChannel(channel);


    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "dadayaebat")
            .setSmallIcon(R.drawable.micro_usb)
            .setContentTitle("NIGHTLIGHT")
            .setContentText("NIGHTLIGHT успешно подключился к интернету.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);


// notificationId is a unique int for each notification that you must define
    notificationManager.notify(2228133, builder.build());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    startStartPage();


    findViewById(R.id.dalee).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        endStartPage().getAnimation().setAnimationListener(new Animation.AnimationListener() {
          @Override
          public void onAnimationStart(Animation animation) {

          }

          @Override
          public void onAnimationEnd(Animation animation) {
            startPowerPage();
            findViewById(R.id.micro_usb).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                endPowerPage();
                findViewById(R.id.charger).getAnimation().setAnimationListener(new Animation.AnimationListener() {
                  @Override
                  public void onAnimationStart(Animation animation) {

                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {
                    startInternetConnectionPage();

                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {

                  }
                });

              }
            });
            findViewById(R.id.charger).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                endPowerPage();
                findViewById(R.id.charger).getAnimation().setAnimationListener(new Animation.AnimationListener() {
                  @Override
                  public void onAnimationStart(Animation animation) {

                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {
                    startInternetConnectionPage();


                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {

                  }
                });


              }
            });


          }

          @Override
          public void onAnimationRepeat(Animation animation) {

          }
        });

      }


    });
    textView = (TextView) findViewById(R.id.textView4);
    textView.setMovementMethod(new ScrollingMovementMethod());


  }

  public void startStartPage() {
    setContentView(R.layout.activity_start);
    findViewById(R.id.longShadowsFrameLayoutWrapper).startAnimation(AnimationUtils.loadAnimation(this, R.anim.nightlight_animation));
    findViewById(R.id.textView4).startAnimation(AnimationUtils.loadAnimation(this, R.anim.textview_animation));
    findViewById(R.id.dalee).startAnimation(AnimationUtils.loadAnimation(this, R.anim.next_button_show_delay));
  }

  public View endStartPage() {
    findViewById(R.id.longShadowsFrameLayoutWrapper).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.nightlight_out_top));
    findViewById(R.id.textView4).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.textview_out_left));
    findViewById(R.id.dalee).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.abc_fade_out));
    return findViewById(R.id.textView4);
  }

  public void startPowerPage() {
    setContentView(R.layout.activity_start_power);

    findViewById(R.id.micro_usb).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.micro_usb_in_left));
    findViewById(R.id.charger).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.charger_in_right));
    findViewById(R.id.textView5).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.abc_fade_in));
    findViewById(R.id.longShadowsFrameLayoutWrapper).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.abc_fade_in));
    findViewById(R.id.try_power_text).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.abc_fade_in));


  }

  public void endPowerPage() {
    findViewById(R.id.micro_usb).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.micro_usb_out_left));
    findViewById(R.id.charger).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.charger_out_right));
    findViewById(R.id.textView5).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.abc_fade_out));
    findViewById(R.id.longShadowsFrameLayoutWrapper).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.abc_fade_out));
    findViewById(R.id.try_power_text).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.abc_fade_out));

  }

  public void startInternetConnectionPage() {
    setContentView(R.layout.activity_start_internet_connect);
    findViewById(R.id.connect_scheme).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.connect_scheme_in));
    findViewById(R.id.longShadowsFrameLayoutWrapper).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.abc_fade_in));
    findViewById(R.id.wifi_auth_data).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.auth_data_in_left));
    findViewById(R.id.send).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.charger_in_right));
    findViewById(R.id.connect_hot_spot).startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.micro_usb_in_left));
    findViewById(R.id.connect_hot_spot).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager.getConnectionInfo().getSSID().equals("\"Nightlight\"")) {
          Toast.makeText(StartActivity.this, "Ты уже подключилась к хот-споту. Осталось отправить данные.", Toast.LENGTH_LONG).show();
        } else {
          startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
        }
        startActivity(new Intent(StartActivity.this, MainActivity.class).putExtra("activity", "Start"));
        overridePendingTransition(0, 0);
        makeNotification();
      }
    });
    findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager.getConnectionInfo().getSSID().equals("\"Nightlight\"")) {
          EditText ssidEditText = findViewById(R.id.ssidEditText);
          EditText passwordEditText = findViewById(R.id.passwordEditText);
          MainActivity.prefs.edit().putString("ssid", ssidEditText.getText().toString()).apply();
          MainActivity.prefs.edit().putString("wifi_password", passwordEditText.getText().toString()).apply();
          MainActivity.ssid = ssidEditText.getText().toString();
          MainActivity.wifi_password = passwordEditText.getText().toString();
          MainActivity.localConnection.sendAuthData(StartActivity.this, new LocalConnection.LocalConnecionListener() {
            @Override
            public void onSuccessSend() {
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  StartActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                      builder
                              .setMessage("Nightlight подтвердил получение новых данных. Сейчас он пробует подключиться. Если все пройдет успешно - тебе придет соответствующее уведомление. Жди.")
                              .setCancelable(false)
                              .setPositiveButton("Ок",
                                      new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                          dialog.cancel();
                                        }
                                      });


                      AlertDialog alert = builder.create();
                      alert.show();
                    }
                  });
                }
              });

            }

            @Override
            public void onNoCallback() {

            }
          });


        } else {
          Toast.makeText(StartActivity.this, "Сначала подключись к хот-споту", Toast.LENGTH_LONG).show();
          startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
        }
      }
    });
  }
}