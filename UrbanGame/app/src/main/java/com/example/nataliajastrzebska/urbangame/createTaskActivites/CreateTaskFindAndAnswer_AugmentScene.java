package com.example.nataliajastrzebska.urbangame.createTaskActivites;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nataliajastrzebska.urbangame.ChooseCreatingModeDialog;
import com.example.nataliajastrzebska.urbangame.R;

public class CreateTaskFindAndAnswer_AugmentScene extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_find_and_answer_augment_scene);
    }

    public void btnAddClicked(View view){
        showChooseCardboardDialog();
    }

    void showChooseCardboardDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ChoosingUseCardboardDialog chooseCardboard = new ChoosingUseCardboardDialog();
        chooseCardboard.show(fm, "dialog_choose_cardboard_use");
    }
}
