package com.example.animepeak.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkHelper {
    public static boolean IsConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
