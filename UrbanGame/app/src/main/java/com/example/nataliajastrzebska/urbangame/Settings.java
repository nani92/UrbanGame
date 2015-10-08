package com.example.nataliajastrzebska.urbangame;

/**
 * Created by nataliajastrzebska on 08/10/15.
 */
public class Settings {
    private static Settings mInstance = null;

    private boolean vibrationEnabled;

    private Settings(){
        vibrationEnabled = true;
    }

    public static Settings getInstance(){
        if(mInstance == null)
        {
            mInstance = new Settings();
        }
        return mInstance;
    }

    public boolean getVibrationEnabled() {
        return vibrationEnabled;
    }

    public void setVibrationEnabled(boolean inVibration) {
        vibrationEnabled = inVibration;
    }
}