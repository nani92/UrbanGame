package com.example.nataliajastrzebska.urbangame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SummaryScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_screen);

        String time = getIntent().getExtras().getString("time");
        Long distance = getIntent().getExtras().getLong("distance");


        TextView tv_time = (TextView) findViewById(R.id.tv_summaryScreenTime);
        tv_time.setText(getString(R.string.endGameTime) + " " + time);

        TextView tv_distance = (TextView) findViewById(R.id.tv_summaryScreenDistance);
        tv_distance.setText(getString(R.string.endGameDistance) + " " + String.valueOf(distance));
    }
}
