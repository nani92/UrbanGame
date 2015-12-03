package com.example.nataliajastrzebska.urbangame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nataliajastrzebska.urbangame.createTaskActivites.CreateTaskABCD;
import com.example.nataliajastrzebska.urbangame.createTaskActivites.CreateTaskFindAndAnswer_AddToScene_Cardboard;
import com.example.nataliajastrzebska.urbangame.createTaskActivites.CreateTaskFindAndAnswer_AugmentScene;
import com.example.nataliajastrzebska.urbangame.createTaskActivites.CreateTaskLookAndAnswer;
import com.example.nataliajastrzebska.urbangame.createTaskActivites.CreateTaskThinkAndAnswer;
import com.example.nataliajastrzebska.urbangame.taskGame.TaskGameActivity;

public class ChooseGameTaskTypeActivity extends AppCompatActivity {

    TaskListAdapter taskListAdapter;
    ListView listView;
    TaskType[] typeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context ctx = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_task_type);
        listView = (ListView)findViewById(R.id.lv_taskList);
        taskListAdapter = new TaskListAdapter(this,getTaskList());

        listView.setAdapter(taskListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (typeList[position]) {
                    case THINKandANSWER:
                        startActivityForResult(new Intent(ctx, CreateTaskThinkAndAnswer.class),1);
                        break;
                    case AR_FINDandANSWER:
                        startActivityForResult(new Intent(ctx, CreateTaskFindAndAnswer_AugmentScene.class),1);
                        break;
                    case AR_GAME:
                        startActivityForResult(new Intent(ctx, TaskGameActivity.class),1);
                        break;
                    case LOOKandANSWER:
                        startActivityForResult(new Intent(ctx, CreateTaskLookAndAnswer.class),1);
                        break;
                    case ABCD:
                        startActivityForResult(new Intent(ctx, CreateTaskABCD.class),1);
                        break;
                }
            }
        });

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


    String [] getTaskList(){
        String [] taskList;
        taskList = new String[] {
                getString(R.string.task_thinkAndAnswer_name),
                getString(R.string.task_lookAndAnswer_name),
                getString(R.string.task_hearAndAnswer_name),
                getString(R.string.task_watchAndAnswer_name),
                getString(R.string.task_game_name),
                getString(R.string.task_abcd_name)
               // getString(R.string.task_findAndAnswer_name)
        };
        typeList = new TaskType[] {
                TaskType.THINKandANSWER,
                TaskType.LOOKandANSWER,
                TaskType.HEARandANSWER,
                TaskType.WATCHandANSWER,
                //TaskType.AR_FINDandANSWER
                TaskType.AR_GAME,
                TaskType.ABCD
        };
        return  taskList;
    }
}
