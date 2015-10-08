package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Switch switchVibration = (Switch) findViewById(R.id.switch_settings_enabledVibration);
        switchVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                Settings.getInstance().setVibrationEnabled(isChecked);
            }
        });
    }

    public void testSound1(View v){
        SoundPlayer.getInstance().playSound(this, Sound.pierd);
    };

    public void testSound2(View v){
        SoundPlayer.getInstance().playSound(this,Sound.gun);
    };
    public void testVibrate(View v){
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
        vibe.vibrate(150);
    };


}
