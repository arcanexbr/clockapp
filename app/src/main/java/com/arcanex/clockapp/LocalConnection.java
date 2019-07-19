package com.arcanex.clockapp;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

import static com.arcanex.clockapp.MainActivity.ssid;
import static com.arcanex.clockapp.MainActivity.wifi_password;

public class LocalConnection extends Connection {
    Activity activity;
    WifiManager wifiManager;

    SearchAPThread searchAPThread = new SearchAPThread();
    List<ScanResult> scanResults;



    LocalConnection(Activity activity) {
        this.activity = activity;
        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Service.WIFI_SERVICE);

    }

    void startSearchAP() {
        SearchAPThread searchAPThread = new SearchAPThread();
        searchAPThread.start();



    }
    void startCallESP() {
        CallESPThread callESPThread = new CallESPThread();
        callESPThread.start();
    }
    class sendAuthDataThread extends Thread {
        @Override
        public void run() {
            boolean recievedAuthData  = false;
            try {
                DatagramSocket udpSocket = new DatagramSocket(1337);
                InetAddress serverAddr = InetAddress.getByName("255.255.255.255");
                byte[] buf = (ssid + "$" + wifi_password + "$").getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 4210);
                while (!recievedAuthData) {
                    udpSocket.send(packet);
                    System.out.println(serverAddr.toString());
                    sleep(2000);
                }

            } catch (Exception a) {
                a.printStackTrace();
            }
        }

    }



    class SearchAPThread extends Thread {
        boolean findAP = false;

        @Override
        public void run() {
            while (!findAP) {
                scanResults = wifiManager.getScanResults();
                for (int i = 0; i < scanResults.size(); i++) {
                    System.out.println(scanResults.get(i).SSID);
                    if (scanResults.get(i).SSID.equals("Nightlight")) {
                        findAP = true;
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "Я нашел есп", Toast.LENGTH_SHORT).show();
                            }
                        });




                    }
                }
                try {
                    sleep(10000);
                } catch (Exception a) {
                    a.printStackTrace();
                }

            }
        }
    }
    class CallESPThread extends Thread {
        @Override
        public void run() {
            boolean getCallBack = false;
            try {
                DatagramSocket udpSocket = new DatagramSocket(1337);
                InetAddress serverAddr = InetAddress.getByName("255.255.255.255");
                byte[] buf = ("call").getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 4210);
                while (!getCallBack) {
                    udpSocket.send(packet);
                    System.out.println(serverAddr.toString());
                }

            } catch (Exception a) {
                a.printStackTrace();
            }
        }
    }



}








