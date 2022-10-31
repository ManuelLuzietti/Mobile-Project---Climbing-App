package com.example.climbingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

public class InternetManager {

    private ConnectivityManager.NetworkCallback networkCallback;
    private final Snackbar snackbar;
    private boolean isNetworkConnected;
    private final Activity activity;
    private static final String HOST = "http://10.0.2.2";
    private static final String HOST2 ="http://192.168.1.134";
    public final static String URL = HOST +"/climbingAppWebServer/web_server_for_mobile_project/";

    public  InternetManager(Activity activity,View view){
        this.activity = activity;
        this.isNetworkConnected = false;
        snackbar = Snackbar.make(view, "no internet available", Snackbar.LENGTH_INDEFINITE)
                .setAction("settings", view1 -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                });
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                isNetworkConnected = true;
                snackbar.dismiss();
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                isNetworkConnected = false;
                snackbar.show();
            }
        };


    }

    public void registerNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback);
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                isNetworkConnected = (networkInfo != null && networkInfo.isConnected());
            }
        } else {
            isNetworkConnected = false;
        }
    }

    public void unregisterNetworkCallback(){
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    connectivityManager.unregisterNetworkCallback(networkCallback);
                }
            } else {
                snackbar.dismiss();
            }

    }
    public boolean isNetworkConnected(){
        return isNetworkConnected;
    }
    public Snackbar getSnackbar(){
        return snackbar;
    }
}
