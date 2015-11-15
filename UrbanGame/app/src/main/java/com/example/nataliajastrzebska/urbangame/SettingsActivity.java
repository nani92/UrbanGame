package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        //Setting up listener to vibration switch in case of change it will send new value
        //to Settings and it will be put into Shared prederences
        Switch switchVibration = (Switch) findViewById(R.id.switch_settings_enabledVibration);
        switchVibration.setChecked(Settings.getInstance().getVibrationEnabled());
        switchVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.getInstance().setVibrationEnabled(isChecked);
            }
        });


        //Setting up listener to sound switch in case of change it will send new value
        //to Settings and it will be put into Shared prederences
        Switch switchSound = (Switch) findViewById(R.id.switch_settings_enabledSound);
        switchSound.setChecked(Settings.getInstance().getSoundEnabled());
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.getInstance().setSoundEnabled(isChecked);
            }
        });


    }


    //TEST METHODS


    public void testSound2(View v) {
        SoundPlayer.getInstance().playSound(this, Sound.gun);
    }

    public void testVibrate(View v) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(150);

        GameInformation game = new GameInformation();
        XMLPullParser parser = new XMLPullParser();
        InputStream is = getResources().openRawResource(getResources().getIdentifier("raw/example","raw", getPackageName()));


        game = parser.parse(is);
        System.out.println(game.toString());

        XMLSerializer serializer = new XMLSerializer();
        System.out.println(serializer.serialize(game));


        GameStorage.getInstance().saveGame(game);
        System.out.println("Zapisane gry: ");
        System.out.println(GameStorage.getInstance().loadGamesNames().toString());
        System.out.println(GameStorage.getInstance().loadGame("nazwaGry.ginf").toString());



    }
    //END OF TEST METHODS
}
