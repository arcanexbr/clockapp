package com.arcanex.clockapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.List;

import static com.arcanex.clockapp.MainActivity.prefs;
import static com.arcanex.clockapp.MainActivity.ssid;
import static com.arcanex.clockapp.MainActivity.wifi_password;

public class LocalConnection extends Connection {
    Activity activity;
    WifiManager wifiManager;
    DatagramSocket udpSocket;
    InetAddress address;
    ConnectivityManager connectivityManager;
    LocalConnecionListener localConnecionListener;




    SearchAPThread searchAPThread = new SearchAPThread();
    List<ScanResult> scanResults;



    LocalConnection(Activity activity) {
        try {
            udpSocket = new DatagramSocket(1488);
            udpSocket.setBroadcast(true);


        } catch (Exception a) {
            a.printStackTrace();
        }

        this.activity = activity;
        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Service.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) activity.getSystemService(Service.CONNECTIVITY_SERVICE);





    }
    private void  setWIFINetwork() {
        Network[] networks = connectivityManager.getAllNetworks();
        for (int i = 0; i < networks.length; i++) {
            if (connectivityManager.getNetworkInfo(networks[i]).getType() == ConnectivityManager.TYPE_WIFI) {
                try {
                    networks[i].bindSocket(udpSocket);
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        }

    }


    void sendAuthData(Activity activity, LocalConnecionListener localConnecionListener) {
       sendAuthDataThread thread = new sendAuthDataThread(activity, localConnecionListener);
       thread.start();
    }
    class sendAuthDataThread extends Thread {
        Activity activity;
        LocalConnecionListener localConnecionListener;
        sendAuthDataThread (Activity activity, LocalConnecionListener localConnecionListener) {
            this.activity = activity;
            this.localConnecionListener = localConnecionListener;

        }
        @Override
        public void run() {
            try {
                setWIFINetwork();
                byte[] message = (ssid + "$" + wifi_password + "$").getBytes();
                address = InetAddress.getByName("255.255.255.255");
                DatagramPacket packet = new DatagramPacket(message, message.length, address, 1337);
                byte[] callback = new byte[1];
                DatagramPacket recieve_packet = new DatagramPacket(callback, callback.length);
                udpSocket.send(packet);
                udpSocket.setSoTimeout(2000);
                udpSocket.receive(recieve_packet);
                if (new String(callback).equals("r")) {
                    localConnecionListener.onSuccessSend();
                }


            } catch (SocketTimeoutException timeout) {
                localConnecionListener.onNoCallback();

            } catch (Exception a) {

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
                byte[] buf = ("?").getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 4210);
                while (!getCallBack) {
                    udpSocket.send(packet);
                    sleep(400);
                    byte[] receive_data = new byte[udpSocket.getReceiveBufferSize()];
                    udpSocket.receive(new DatagramPacket(receive_data, udpSocket.getReceiveBufferSize()));
                    System.out.println(receive_data.toString());
                }

            } catch (Exception a) {
                a.printStackTrace();
            }
        }
    }
    interface LocalConnecionListener {
         void onSuccessSend();
         void onNoCallback();
    }



}








