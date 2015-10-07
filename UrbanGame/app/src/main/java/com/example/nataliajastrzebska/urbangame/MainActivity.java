package com.example.nataliajastrzebska.urbangame;

import android.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createGameClicked(View v){
        showChooseCreatingModeDialog();
    }

    void showChooseCreatingModeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ChooseCreatingModeDialog chooseCreatingModeDialog = new ChooseCreatingModeDialog();
        chooseCreatingModeDialog.show(fm, "dialog_choose_creating_mode");
    }

}
