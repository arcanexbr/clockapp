package com.arcanex.clockapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class UpdateUI {
    Activity activity;
    public UpdateUI (Activity activity) {
        this.activity = activity;
    }
     static final int INTERNET_CONNECTION_FAILED = 1;
     static final int INTERNET_CONNECTION_PROCESS = 2;
     static final int INTERNET_CONNECTION_SUCCESFUL = 3;
     static final int LOCAL_CONNECTION_FAILED = 4;
     static final int LOCAL_CONNECTION_PROCESS = 5;
     static final int LOCAL_CONNECTION_SUCCESFUL = 6;

    public void updateInternetConnection (int status){
        switch (status) {
            case INTERNET_CONNECTION_FAILED:
                activity.findViewById(R.id.progressBar_internet_connection).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.internet_connection_already).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.internet_connection_already).setClickable(false);
                activity.findViewById(R.id.internet_connection_fail).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.internet_connection_fail).setClickable(true);
                break;
            case INTERNET_CONNECTION_PROCESS:
                activity.findViewById(R.id.internet_connection_fail).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.internet_connection_fail).setClickable(false);
                activity.findViewById(R.id.internet_connection_already).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.internet_connection_already).setClickable(false);
                activity.findViewById(R.id.progressBar_internet_connection).setVisibility(View.VISIBLE);
                break;
            case INTERNET_CONNECTION_SUCCESFUL:
                activity.findViewById(R.id.progressBar_internet_connection).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.internet_connection_fail).setClickable(false);
                activity.findViewById(R.id.internet_connection_fail).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.internet_connection_already).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.internet_connection_already).setClickable(true);



        }
    }
}
