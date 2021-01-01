package com.arcanex.clockapp;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PickPalette extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_palette);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Радуга 1", "Радуга 2", "Радуга 3", "Фиолетовый и Зеленый", "Полный рандом", "Белый с черным 1", "Белый с черным 2", "Облачные цвета", "Вечериночные цвета", "Красный, Белый, Синий 1", "Красный, Белый, Синий 2", "Лавовые цвета", "Океановы цвета", "Лесные цвета", "Горячие цвета" });

        ListView variants = findViewById(R.id.listview1);
       final MyService service = MainActivity.service;
        variants.setAdapter(arrayAdapter);
        variants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        service.internetConnection.sendData("user_ddefdaed/palette", "0");
                        break;
                    case 1:
                        service.internetConnection.sendData("user_ddefdaed/palette", "1");
                        break;
                    case 2:
                        service.internetConnection.sendData("user_ddefdaed/palette", "2");
                        break;
                    case 3:
                        service.internetConnection.sendData("user_ddefdaed/palette", "3");
                        break;
                    case 4:
                        service.internetConnection.sendData("user_ddefdaed/palette", "4");
                        break;
                    case 5:
                        service.internetConnection.sendData("user_ddefdaed/palette", "5");
                        break;
                    case 6:
                        service.internetConnection.sendData("user_ddefdaed/palette", "6");
                        break;
                    case 7:
                        service.internetConnection.sendData("user_ddefdaed/palette", "7");
                        break;
                    case 8:
                        service.internetConnection.sendData("user_ddefdaed/palette", "8");
                        break;
                    case 9:
                        service.internetConnection.sendData("user_ddefdaed/palette", "9");
                        break;
                    case 10:
                        service.internetConnection.sendData("user_ddefdaed/palette", "10");
                        break;
                    case 11:
                        service.internetConnection.sendData("user_ddefdaed/palette", "11");
                        break;
                    case 12:
                        service.internetConnection.sendData("user_ddefdaed/palette", "12");
                        break;
                    case 13:
                        service.internetConnection.sendData("user_ddefdaed/palette", "13");
                        break;
                    case 14:
                        service.internetConnection.sendData("user_ddefdaed/palette", "14");
                        break;

                }

            }
        });

    }
}