package com.example.nataliajastrzebska.urbangame;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }




}

