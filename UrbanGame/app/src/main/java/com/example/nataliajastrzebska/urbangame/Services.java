package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nataliajastrzebska on 19/10/15.
 */
public class Services {
    private static Services mInstance = null;

    private Services() {}


    public static Services getInstance() {
        if (mInstance == null) {
            mInstance = new Services();
        }
        return mInstance;
    }

    public boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
