package com.agh.planner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import org.json.JSONObject;

public class Receiver extends BroadcastReceiver {
    JSONObject weatherForecast;

    public Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                Toast.makeText(context, "WIFI is connected", Toast.LENGTH_LONG).show();
                context.sendBroadcast(new Intent("WIFI_ON"));

            } else {
                // wifi connection was lost
                Toast.makeText(context, "WIFI is lost", Toast.LENGTH_LONG).show();
            }
        }
    }
}

