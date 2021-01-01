package com.arcanex.clockapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

public class ColorPickActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    TextView seekBarValue;
    Switch switch1;
    Button button;
    Button button1;

    MyService service = MainActivity.service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_pick);
        SeekBar seekBar = findViewById(R.id.speed_seekbar);
        seekBar.setOnSeekBarChangeListener( this);
        seekBarValue = findViewById(R.id.speed_seekbar_value);
        switch1 = findViewById(R.id.switch1);
        button = findViewById(R.id.color_change_button);
        button1 = findViewById(R.id.palette_change_button);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    service.internetConnection.sendData("user_ddefdaed/fixed", "1");
                    service.internetConnection.sendData("user_ddefdaed/color", "255");
                } else {
                    service.internetConnection.sendData("user_ddefdaed/fixed", "0");
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorPickActivity.this, PickPalette.class), ActivityOptions.makeSceneTransitionAnimation(ColorPickActivity.this).toBundle());
                overridePendingTransition(0, 0);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(view.getContext())
                        .setTitle("Выбери цвет")
                        .noSliders()
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .setColorEditTextColor(Color.WHITE)
                        .density(12)



                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                service.internetConnection.sendData("user_ddefdaed/color", String.valueOf(selectedColor));
                            }
                        })
                        .setPositiveButton("Ок", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {


                            }
                        })

                        .build()
                        .show();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        seekBarValue.setText("Скорость переливания " + i + " тик/секунду.");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        service.internetConnection.sendData("user_ddefdaed/speed", String.valueOf(seekBar.getProgress()));
    }
}
