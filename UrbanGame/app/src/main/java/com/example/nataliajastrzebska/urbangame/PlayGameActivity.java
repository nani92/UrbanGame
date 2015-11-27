package com.example.nataliajastrzebska.urbangame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class PlayGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_PlayGameActivity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GameStorage.getInstance().loadGamesNames());
        spinner.setAdapter(adapter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void startGameClicked(View v) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_PlayGameActivity);
        String game = (String) spinner.getSelectedItem();
        CurrentGame.getInstance().setGameInformation(GameStorage.getInstance().loadGame(game));
        startActivity(new Intent(this, PlayGameMapScreen.class));
    }
}
