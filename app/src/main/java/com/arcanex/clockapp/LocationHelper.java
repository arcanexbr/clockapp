package com.arcanex.clockapp;

import android.Manifest;
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
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.List;

import static com.arcanex.clockapp.MainActivity.homeLocation;
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
        }
    }


    @Override
    public void onLocationChanged(android.location.Location location) {
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (location.getProvider() == LocationManager.GPS_PROVIDER) {
            if (location.distanceTo(homeLocaton) > 300 && userAtHome && outHomeAccuracyCount > 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel("dadayaebat", "name", importance);
                    notificationManager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "dadayaebat")
                        .setSmallIcon(R.drawable.micro_usb)
                        .setContentText(location.getProvider() + ": " + location.distanceTo(homeLocaton) + "Юзер вышел из дома")
                        .setOngoing(false)
                        .setPriority(2)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                Notification notification = builder.build();
                notificationManager.notify(12, notification);
                Toast.makeText(context, location.getProvider() + ": " + location.distanceTo(homeLocaton) + "Юзер вышел из дома", Toast.LENGTH_SHORT).show();
                userAtHome = false;
                outHomeAccuracyCount = 0;
            } else if (location.distanceTo(homeLocaton) > 300 && userAtHome) {
                outHomeAccuracyCount++;
            } else if (location.distanceTo(homeLocaton) < 300 && !userAtHome) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel("dadayaebat", "name", importance);
                    notificationManager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "dadayaebat")
                        .setSmallIcon(R.drawable.micro_usb)
                        .setContentText(location.getProvider() + ": " + location.distanceTo(homeLocaton) + "Юзер пришел домой")
                        .setOngoing(false)
                        .setPriority(2)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                Notification notification = builder.build();
                notificationManager.notify(12, notification);
                Toast.makeText(context, location.getProvider() + ": " + location.distanceTo(homeLocaton) + "Юзер пришел домой", Toast.LENGTH_SHORT).show();
                userAtHome = true;
            }


        } else {
            if (location.distanceTo(homeLocaton) > 600 && userAtHome && outHomeAccuracyCount > 2) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel("dadayaebat", "name", importance);
                    notificationManager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "dadayaebat")
                        .setSmallIcon(R.drawable.micro_usb)
                        .setContentText(location.getProvider() + ": " + location.distanceTo(homeLocaton) + "Юзер вышел из дома")
                        .setOngoing(false)
                        .setPriority(2)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                Notification notification = builder.build();
                notificationManager.notify(12, notification);
                Toast.makeText(context, location.getProvider() + ": " + location.distanceTo(homeLocaton) + "Юзер вышел из дома", Toast.LENGTH_SHORT).show();
                userAtHome = false;
                outHomeAccuracyCount = 0;
            } else if (location.distanceTo(homeLocaton) > 600 && userAtHome){
                outHomeAccuracyCount++;


            } else if (location.distanceTo(homeLocaton) < 600 && !userAtHome) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel("dadayaebat", "name", importance);
                    notificationManager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "dadayaebat")
                        .setSmallIcon(R.drawable.micro_usb)
                        .setContentText(location.getProvider() + ": " + location.distanceTo(homeLocaton) + "Юзер пришел домой")
                        .setOngoing(false)
                        .setPriority(2)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                Notification notification = builder.build();
                notificationManager.notify(12, notification);
                Toast.makeText(context, location.getProvider() + ": " + location.distanceTo(homeLocaton) + "Юзер пришел домой", Toast.LENGTH_SHORT).show();
                userAtHome = true;
            }



        }

    }

    public void setHomeLocation() {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            if ((locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true))) != null) {
                homeLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
            } else {
                Toast.makeText(context, "Ошибка: геолокация отключена", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(context, locationManager.getBestProvider(new Criteria(), true)  + ": " + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude() + " " + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(), Toast.LENGTH_LONG).show();
        }
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
