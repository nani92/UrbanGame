package com.example.nataliajastrzebska.urbangame.createTaskActivites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.nataliajastrzebska.urbangame.CurrentGame;
import com.example.nataliajastrzebska.urbangame.GamePoint;
import com.example.nataliajastrzebska.urbangame.GameTask;
import com.example.nataliajastrzebska.urbangame.R;
import com.example.nataliajastrzebska.urbangame.TaskType;

import java.security.SecureRandom;

public class CreateTaskThinkAndAnswer extends AppCompatActivity {

    CheckBox text,date,number;
    EditText answer,question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_think_and_answer);
        answer = (EditText) findViewById(R.id.et_taskThinkAndAnswer_answer);
        question = (EditText) findViewById(R.id.et_createThinkAndAnswerTask_addQuestion);
        setCheckBoxes();
    }

    void setCheckBoxes(){
        text = (CheckBox) findViewById(R.id.cB_createThinkAndAnswerTask_isAnswerText);
        date = (CheckBox) findViewById(R.id.cB_createThinkAndAnswerTask_isAnswerDate);
        number = (CheckBox) findViewById(R.id.cB_createThinkAndAnswerTask_isAnswerNumber);
        text.setChecked(true);
    }

    void unCheckBoxes(CheckBox cb1, CheckBox cb2) {
        cb1.setChecked(false);
        cb2.setChecked(false);
    }


    public void onCheckBoxClicked (View view){
        boolean isChecked = ((CheckBox) view).isChecked();
        if (!isChecked){
            text.setChecked(true);
            answer.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else{
            switch(view.getId()) {
                case R.id.cB_createThinkAndAnswerTask_isAnswerText:
                    unCheckBoxes(date, number);
                    answer.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case R.id.cB_createThinkAndAnswerTask_isAnswerNumber:
                    unCheckBoxes(text, date);
                    answer.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case R.id.cB_createThinkAndAnswerTask_isAnswerDate:
                    unCheckBoxes(text, number);
                    answer.setInputType(InputType.TYPE_CLASS_DATETIME);
            }
        }
    }

    public void saveTaskThinkAndAnswer (View v){

        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).setGameTask(new GameTask());
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).getGameTask().setTaskType("thinkAndAnswer");
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).getGameTask().setAnswer(answer.getText().toString());
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).getGameTask().setQuestion(question.getText().toString());
        this.finish();
    }


}
