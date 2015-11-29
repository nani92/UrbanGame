package com.example.nataliajastrzebska.urbangame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GameSettingsActivity extends AppCompatActivity {
    CreatingModeEnum creatingModeEnum;
    Spinner typeOfGame_spinner;
    EditText name;
    CheckBox isRPG, direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        name = (EditText) findViewById(R.id.editText_gameSettingsActivity_gameName);
        isRPG = (CheckBox) findViewById(R.id.checkBox_gameSettingsActivity_enabledRPG);
        direction = (CheckBox) findViewById(R.id.checkBox_gameSettingsActivity_enabledDirection);


       /* setTypeOfGame_spinner();
        setTypeOfGameSpinnerListener();*/

    }



  /*  void setTypeOfGame_spinner() {
        typeOfGame_spinner = (Spinner)findViewById(R.id.spinner_gameSettingsActivity_mode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_gameMode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfGame_spinner.setAdapter(adapter);
    }

    void setTypeOfGameSpinnerListener() {
        typeOfGame_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item by using:
                // parent.getItemAtPosition(pos)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/




    public void onCreateGameClicked(View view) {
        //TODO Put extra with settings
        CurrentGame.getInstance().setGameInformation(new GameInformation());
        CurrentGame.getInstance().getGameInformation().setName(name.getText().toString());
        if (isRPG.isChecked())
            CurrentGame.getInstance().getGameInformation().setIsRPG(1);
        else
            CurrentGame.getInstance().getGameInformation().setIsRPG(0);
        if (direction.isChecked())
            CurrentGame.getInstance().getGameInformation().setShouldShowDirection(1);
        else
            CurrentGame.getInstance().getGameInformation().setShouldShowDirection(0);

        if(getIntent().getStringExtra("Mode").equals("Classic")) {
        Intent i = new Intent(this, CreateClassicGame.class);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this, CreateRemoteGame.class);
           startActivity(i);
        }
        finish();

    }
}
