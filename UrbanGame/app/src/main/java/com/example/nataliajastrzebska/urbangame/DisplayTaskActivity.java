package com.example.nataliajastrzebska.urbangame;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DisplayTaskActivity extends AppCompatActivity {


    private GamePoint gamePoint;
    private TaskType taskType;
    private LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);

        int id = getIntent().getExtras().getInt("pointId");
        gamePoint = CurrentGame.getInstance().getGameInformation().getPoints().get(id);
        container = (LinearLayout) findViewById(R.id.ll_displayTask_container);
        setViewBasedOnType();
    }

    private void setViewBasedOnType(){
        taskType = gamePoint.getGameTask().getTaskType();
        switch (gamePoint.getGameTask().getTaskType()){
            case ABCD:
                setAbcdView();
                break;
            case THINKandANSWER:
                setThinkAndAsnwerView();
                break;
        }
    }

    private void setAbcdView(){
        addTextView(gamePoint.getGameTask().getQuestion(), 30);
        for(int i = 0; i <gamePoint.getGameTask().getAnswers().size(); i++) {
            if(i == gamePoint.getGameTask().getCorrectAnswer())
                addButton(gamePoint.getGameTask().getAnswers().get(i)).setOnClickListener(
                        new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                correctAnswer();
                            }
                        } );
            else
                addButton(gamePoint.getGameTask().getAnswers().get(i)).setOnClickListener(
                        new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wrongAnswer();
                            }
                        } );
        }
    }

    private void setThinkAndAsnwerView(){
        addTextView(gamePoint.getGameTask().getQuestion(), 30);
        final EditText et = addEditText(30);
        addButton(getString(R.string.confirmAnswer)).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString().equals(gamePoint.getGameTask().getAnswer()))
                    correctAnswer();
                else
                    wrongAnswer();
            }
        });
    }

    private void addTextView(String text, int textSize){
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(textSize);
        container.addView(textView);
    }

    private Button addButton(String text){
        Button button = new Button(this);
        button.setText(text);
        container.addView(button);
        return button;
    }

    private EditText addEditText(int textSize){
        EditText editText = new EditText(this);
        editText.setTextSize(textSize);
        container.addView(editText);
        return editText;
    }

    private void correctAnswer(){
        Toast.makeText(this, "Good!", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void wrongAnswer(){
        Toast.makeText(this, "Wrong Answer, try again.", Toast.LENGTH_SHORT).show();
    }

}
