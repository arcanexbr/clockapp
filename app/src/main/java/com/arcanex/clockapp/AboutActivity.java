package com.arcanex.clockapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AboutActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(12);
        setContentView(R.layout.activity_about);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, new String[]{"Информация и возможности", "Связь с автором", "Другие проекты", "Поблагодарить"}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    AboutActivity.this.startActivity(new Intent(AboutActivity.this, InformationAndPower.class), ActivityOptions.makeSceneTransitionAnimation(AboutActivity.this, new Pair[0]).toBundle());
                } else if (i == 1) {
                    AboutActivity.this.startActivity(new Intent(AboutActivity.this, AuthorActivity.class), ActivityOptions.makeSceneTransitionAnimation(AboutActivity.this, new Pair[0]).toBundle());
                } else if (i == 2) {
                    Toast.makeText(AboutActivity.this, "Nightlight пока единственный проект", Toast.LENGTH_SHORT).show();
                } else if (i == 3) {
                    Toast.makeText(AboutActivity.this, "Не стоит",  Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
