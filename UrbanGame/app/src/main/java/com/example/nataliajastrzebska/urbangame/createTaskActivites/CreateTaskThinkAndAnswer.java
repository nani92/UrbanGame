package com.example.nataliajastrzebska.urbangame.createTaskActivites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.nataliajastrzebska.urbangame.R;

public class CreateTaskThinkAndAnswer extends AppCompatActivity {

    CheckBox text,date,number;
    EditText answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_think_and_answer);
        answer = (EditText) findViewById(R.id.et_taskThinkAndAnswer_answer);
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
