package com.example.animepeak.Functions;

import android.content.Context;
import android.net.ConnectivityManager;

public class Global_func {
    public static boolean IsConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
