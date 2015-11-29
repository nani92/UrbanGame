package com.example.nataliajastrzebska.urbangame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    public void savePointInformation(View v)
    {
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).setHint(hint.getText().toString());
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).setPlot(plot.getText().toString());
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).setName(name.getText().toString());
        Intent i = new Intent(this, ChooseGameTaskTypeActivity.class);
        startActivity(i);
        this.finish();
    }
}
