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
    CreatingModeEnum creatingModeEnum;
    Spinner typeOfGame_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        setCreatingModeEnum(savedInstanceState);
        setTypeOfGame_spinner();
        setTypeOfGameSpinnerListener();
    }

    void setCreatingModeEnum(Bundle b) {
        creatingModeEnum = (CreatingModeEnum) b.get("mode");
    }

    void setTypeOfGame_spinner() {
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
