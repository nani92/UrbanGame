package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
                Toast.makeText(ctx, "Clicked " + position, Toast.LENGTH_SHORT).show();
                switch (typeList[position]) {
                    case THINKandANSWER:
                        startActivity(new Intent(ctx, CreateTaskThinkAndAnswer.class));
                        break;
                    case AR_FINDandANSWER:
                        startActivity(new Intent(ctx, CreateTaskFindAndAnswer_AugmentScene.class));
                        break;
                    case AR_GAME:
                        startActivity(new Intent(ctx, TaskGameActivity.class));
                    case LOOKandANSWER:
                        startActivity(new Intent(ctx, CreateTaskLookAndAnswer.class));
                        break;
                }
            }
        });
    }


    String [] getTaskList(){
        String [] taskList;
        taskList = new String[] {
                getString(R.string.task_thinkAndAnswer_name),
                getString(R.string.task_lookAndAnswer_name),
                getString(R.string.task_hearAndAnswer_name),
                getString(R.string.task_watchAndAnswer_name),
                getString(R.string.task_game_name)
               // getString(R.string.task_findAndAnswer_name)
        };
        typeList = new TaskType[] {
                TaskType.THINKandANSWER,
                TaskType.LOOKandANSWER,
                TaskType.HEARandANSWER,
                TaskType.WATCHandANSWER,
                //TaskType.AR_FINDandANSWER
                TaskType.AR_GAME
        };
        return  taskList;
    }
}
