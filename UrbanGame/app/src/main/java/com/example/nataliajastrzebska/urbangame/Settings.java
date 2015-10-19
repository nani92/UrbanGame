package com.example.nataliajastrzebska.urbangame;


import android.content.SharedPreferences;

/**
 * Created by nataliajastrzebska on 08/10/15.
 */

public class Settings {

    private static Settings mInstance = null;
    private boolean vibrationEnabled;
    private boolean soundEnabled;
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;

    private Settings() {}

    //Initialization of Settings with Shared Preferences and getting
    //saved values from Preferences
    public void init(SharedPreferences sp) {

        this.sp = sp;
        spe = sp.edit();
        soundEnabled = sp.getBoolean(Integer.toString(R.string.preference_sound), false);
        vibrationEnabled = sp.getBoolean(Integer.toString(R.string.preference_vibration), false);
    }

    //Instance getter
    public static Settings getInstance() {
        if (mInstance == null) {
            mInstance = new Settings();
        }
        return mInstance;
    }

    //Checking if Vibration is allowed or it's not
    public boolean getVibrationEnabled() {
        return vibrationEnabled;
    }

    //Checking if Vibration is allowed or it's not
    public boolean getSoundEnabled() {
        return soundEnabled;
    }

    //Vibration stance setter
    public void setVibrationEnabled(boolean inVibration) {
        spe.putBoolean(Integer.toString(R.string.preference_vibration), inVibration);
        spe.commit();
        vibrationEnabled = inVibration;
    }

    //Sound stance setter
    public void setSoundEnabled(boolean inSound) {
        spe.putBoolean(Integer.toString(R.string.preference_sound), inSound);
        spe.commit();
        soundEnabled = inSound;
    }
}