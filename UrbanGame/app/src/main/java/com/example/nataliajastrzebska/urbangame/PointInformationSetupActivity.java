package com.example.nataliajastrzebska.urbangame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.MarkerOptions;

public class PointInformationSetupActivity extends AppCompatActivity {

    EditText name,hint,plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_information_setup);
        name = (EditText) findViewById(R.id.et_point_name);
        hint = (EditText) findViewById(R.id.et_point_hint);
        plot = (EditText) findViewById(R.id.et_point_plot);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            else {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }
    }

    public void savePointInformation(View v)
    {

        int pointNumber = CurrentGame.getInstance().getGameInformation().getPoints().size() - 1;
        CurrentGame.getInstance().getGameInformation().getPoints().get(pointNumber).setHint(hint.getText().toString());
        CurrentGame.getInstance().getGameInformation().getPoints().get(pointNumber).setPlot(plot.getText().toString());
        CurrentGame.getInstance().getGameInformation().getPoints().get(pointNumber).setName(name.getText().toString());
        Intent i = new Intent(this, ChooseGameTaskTypeActivity.class);
        startActivityForResult(i, 1);
    }
}
