package com.arcanex.clockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_about);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,new String[]{"Информация и возможности", "Связь с автором", "Другие проекты", "Поблагодарить"}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        startActivity(new Intent(AboutActivity.this, InformationAndPower.class), ActivityOptions.makeSceneTransitionAnimation(AboutActivity.this).toBundle());
                        break;
                    case 1:
                        startActivity(new Intent(AboutActivity.this, AuthorActivity.class), ActivityOptions.makeSceneTransitionAnimation(AboutActivity.this).toBundle());
                        break;
                    case 2:
                        Toast.makeText(AboutActivity.this, "Nightlight пока единственный проект", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(AboutActivity.this, "Не стоит", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
