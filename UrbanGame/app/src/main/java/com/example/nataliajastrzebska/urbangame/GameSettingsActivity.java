package com.example.nataliajastrzebska.urbangame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class GameSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Setting up Spinner to choose from the list type of the game (for points/for time)
        Spinner spinner = (Spinner)findViewById(R.id.spinner_gameSettingsActivity_mode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_gameMode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        //Checking if the game is for points or for time
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

    }


    //Checking if Role-playing Game is enabled
    public void RPGEnabled(View view) {

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_gameSettingsActivity_enabledRPG);
        if(checkBox.isChecked()){

        }
        else {

        }

    }


    //Checking if Showing The Direction during the game is allowed
    public void showingDirectionEnabled(View view) {

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_gameSettingsActivity_enabledDirection);
        if(checkBox.isChecked()){

        }
        else {

        }

    }


}
