package com.example.nataliajastrzebska.urbangame;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.nataliajastrzebska.urbangame.laby.laby;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Settings.getInstance().init(sharedPref);
        GameStorage.getInstance().init(this);
    }

    public void createGameClicked(View v) {
        showChooseCreatingModeDialog();
    }

    void showChooseCreatingModeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ChooseCreatingModeDialog chooseCreatingModeDialog = new ChooseCreatingModeDialog();
        chooseCreatingModeDialog.show(fm, "dialog_choose_creating_mode");
    }


    public void aboutClicked(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void playGameClicked(View v) {
        Intent intent = new Intent(this, PlayGameActivity.class);
        startActivity(intent);
    }

    public void settingsClicked(View v) {
       /* Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);*/
        startActivity(new Intent(this, laby.class));
    }

    public void sendNotification(String text) {
        Integer mId = 0;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon_small)
                        .setContentTitle("Urban Game")
                        .setContentText(text);
        Notification note = mBuilder.build();
        if (Settings.getInstance().getVibrationEnabled())
            note.defaults |= Notification.DEFAULT_VIBRATE;
        note.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, note);
    }
}

