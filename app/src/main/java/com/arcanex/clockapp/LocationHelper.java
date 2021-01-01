package com.arcanex.clockapp;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.util.List;

import static com.arcanex.clockapp.MainActivity.homeLocation;
import static com.arcanex.clockapp.MainActivity.service;
import static com.arcanex.clockapp.MyService.preferences;

public class LocationHelper implements LocationListener {
    Context context;
    LocationManager locationManager;
    Location homeLocaton = new Location(LocationManager.PASSIVE_PROVIDER);
    boolean userAtHome = true;
    int outHomeAccuracyCount = 0;

    public LocationHelper(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 100, this);

            homeLocaton.setLongitude(Double.valueOf(preferences.getString("home_long", "30.13908")));
            homeLocaton.setLatitude(Double.valueOf(preferences.getString("home_lat", " 61.03967")));
            userAtHome = true;
            outHomeAccuracyCount = 0;


        }
    }


    @Override
    public void onLocationChanged(android.location.Location location) {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        String provider;
        if (locationManager.getProvider(LocationManager.GPS_PROVIDER).getAccuracy() > locationManager.getProvider(LocationManager.NETWORK_PROVIDER).getAccuracy()) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            provider = LocationManager.GPS_PROVIDER;
        }



        if (locationManager.getLastKnownLocation(provider) != null && locationManager.getLastKnownLocation(provider).distanceTo(homeLocaton) > 300 + location.getAccuracy() && userAtHome && outHomeAccuracyCount > 2) {



            userAtHome = false;
            outHomeAccuracyCount = 0;

            service.internetConnection.sendData("user_ddefdaed/athome", "0");


        } else if (locationManager.getLastKnownLocation(provider) != null && locationManager.getLastKnownLocation(provider).distanceTo(homeLocaton) > 300 + location.getAccuracy()  && userAtHome) {
            outHomeAccuracyCount++;
        } else if (locationManager.getLastKnownLocation(provider) != null && locationManager.getLastKnownLocation(provider).distanceTo(homeLocaton) < 300 + location.getAccuracy() && !userAtHome) {



            userAtHome = true;
            service.internetConnection.sendData("user_ddefdaed/athome", "1");

        }













    }

    public String setHomeAddress() {
        List<Address> addresses = null;
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Ошибка: нет прав на геолокацию для приложения.", Toast.LENGTH_SHORT).show();
        } else {
            if ((locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true))) != null) {
                homeLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
                Geocoder geocoder = new Geocoder(context);

                try {
                    addresses = geocoder.getFromLocation(locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true)).getLatitude(), locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true)).getLongitude(), 1);
                    Toast.makeText(context, addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                    homeLocaton.setLatitude(addresses.get(0).getLatitude());
                    homeLocaton.setLongitude(addresses.get(0).getLongitude());
                    preferences.edit().putString("home_long", String.valueOf(addresses.get(0).getLongitude())).commit();
                    preferences.edit().putString("home_lat", String.valueOf(addresses.get(0).getLatitude())).commit();
                    service.internetConnection.sendData("user_ddefdaed/lon", String.valueOf(addresses.get(0).getLongitude()));
                    service.internetConnection.sendData("user_ddefdaed/lat", String.valueOf(addresses.get(0).getLatitude()));


                } catch (Exception a){
                    a.printStackTrace();
                    return null;
                }
                if (addresses.get(0).getAddressLine(0) != null) {
                    return addresses.get(0).getAddressLine(0);
                } else {
                    return "Ошибка, местоположение не найдено. Поставьте местоположение вручную";
                }

            } else {
                Toast.makeText(context, "Ошибка: проблемы с доступом.", Toast.LENGTH_SHORT).show();
            }

        }
        return null;
    }
    public String setHomeAddress(String address){
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            Toast.makeText(context, addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
            homeLocaton.setLatitude(addresses.get(0).getLatitude());
            homeLocaton.setLongitude(addresses.get(0).getLongitude());
            preferences.edit().putString("home_long", String.valueOf(addresses.get(0).getLongitude())).commit();
            preferences.edit().putString("home_lat", String.valueOf(addresses.get(0).getLatitude())).commit();
            service.internetConnection.sendData("user_ddefdaed/lon", String.valueOf(addresses.get(0).getLongitude()));
            service.internetConnection.sendData("user_ddefdaed/lat", String.valueOf(addresses.get(0).getLatitude()));


        } catch (Exception a){
            a.printStackTrace();
            return null;
        }
        return addresses.get(0).getAddressLine(0);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
