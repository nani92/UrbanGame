package com.example.nataliajastrzebska.urbangame.createTaskActivites;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nataliajastrzebska.urbangame.CurrentGame;
import com.example.nataliajastrzebska.urbangame.GameTask;
import com.example.nataliajastrzebska.urbangame.R;

import java.util.ArrayList;

public class CreateTaskABCD extends AppCompatActivity {

    EditText etQuestion;
    EditText etAnsA;
    EditText etAnsB;
    EditText etAnsC;
    EditText etAnsD;
    TextView tvAnsA, tvAnsB, tvAnsC,tvAnsD;
    TextView tvChooseCorrect;
    int correctAns = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_abcd);

        initViews();
    }

    private void initViews(){

        etQuestion = (EditText) findViewById(R.id.et_createTaskAbcd_question);
        etAnsA = (EditText) findViewById(R.id.et_createTaskAbcd_ansA);
        etAnsB = (EditText) findViewById(R.id.et_createTaskAbcd_ansB);
        etAnsC = (EditText) findViewById(R.id.et_createTaskAbcd_ansC);
        etAnsD = (EditText) findViewById(R.id.et_createTaskAbcd_ansD);
        tvAnsA = (TextView) findViewById(R.id.tv_createTaskAbcd_ansA);
        tvAnsB = (TextView) findViewById(R.id.tv_createTaskAbcd_ansB);
        tvAnsC = (TextView) findViewById(R.id.tv_createTaskAbcd_ansC);
        tvAnsD = (TextView) findViewById(R.id.tv_createTaskAbcd_ansD);
        tvChooseCorrect = (TextView) findViewById(R.id.tv_chooseCorrectAnswer_abcd);
    }

    public void saveABCD (View v){
        if (correctAns == -1){
            Toast.makeText(this, getString(R.string.task_ABCD_chooseCorrectMsg), Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> anss = new ArrayList<>();
        anss.add(etAnsA.getText().toString());
        anss.add(etAnsB.getText().toString());
        anss.add(etAnsC.getText().toString());
        anss.add(etAnsD.getText().toString());

        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).setGameTask(new GameTask());
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).getGameTask().setTaskType("abcd");
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).getGameTask().setAnswers(anss);
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).getGameTask().setCorrectAnswer(correctAns);
        CurrentGame.getInstance().getGameInformation().getPoints().get((CurrentGame.getInstance().getGameInformation().getPoints().size() - 1)).getGameTask().setQuestion(etQuestion.getText().toString());
        this.finish();
    }

    public void onAnswerDIsCorrect(View v){
        clearAll();
        tvAnsD.setTypeface(null, Typeface.BOLD);
        tvAnsD.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        etAnsD.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        tvChooseCorrect.setText(getString(R.string.task_ABCD_correct_answer_is) + " D");
        correctAns = 3;
    }

    public void onAnswerCIsCorrect(View v){
        clearAll();
        tvAnsC.setTypeface(null, Typeface.BOLD);
        tvAnsC.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        etAnsC.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        tvChooseCorrect.setText(getString(R.string.task_ABCD_correct_answer_is) + " C");
        correctAns = 2;
    }

    public void onAnswerBIsCorrect(View v){
        clearAll();
        tvAnsB.setTypeface(null, Typeface.BOLD);
        tvAnsB.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        etAnsB.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        tvChooseCorrect.setText(getString(R.string.task_ABCD_correct_answer_is) + " B");
        correctAns = 1;
    }

    public void onAnswerAIsCorrect(View v){
        clearAll();
        tvAnsA.setTypeface(null, Typeface.BOLD);
        tvAnsA.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        etAnsA.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        tvChooseCorrect.setText(getString(R.string.task_ABCD_correct_answer_is) + " A");
        correctAns = 0;
    }

    private void clearAll(){
        clearA();
        clearB();
        clearC();
        clearD();
    }

    private void clearA(){
        tvAnsA.setTextColor(etQuestion.getCurrentTextColor());
        tvAnsA.setTypeface(null, Typeface.NORMAL);
        etAnsA.setTextColor(etQuestion.getCurrentTextColor());
    }

    private void clearB(){
        tvAnsB.setTextColor(etQuestion.getCurrentTextColor());
        tvAnsB.setTypeface(null, Typeface.NORMAL);
        etAnsB.setTextColor(etQuestion.getCurrentTextColor());
    }

    private void clearC(){
        tvAnsC.setTextColor(etQuestion.getCurrentTextColor());
        tvAnsC.setTypeface(null, Typeface.NORMAL);
        etAnsC.setTextColor(etQuestion.getCurrentTextColor());
    }

    private void clearD(){
        tvAnsD.setTextColor(etQuestion.getCurrentTextColor());
        tvAnsD.setTypeface(null, Typeface.NORMAL);
        etAnsD.setTextColor(etQuestion.getCurrentTextColor());
    }


}
