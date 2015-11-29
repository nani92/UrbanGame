package com.example.nataliajastrzebska.urbangame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveGameActivity extends AppCompatActivity {

    EditText author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);
        author = (EditText) findViewById(R.id.et_author_name);
    }


    public void saveGameToFile(View v){
        CurrentGame.getInstance().getGameInformation().setAuthor(author.getText().toString());
        GameStorage.getInstance().saveGame(CurrentGame.getInstance().getGameInformation());
        System.out.println(CurrentGame.getInstance().getGameInformation().toString());
        String text = getResources().getString(R.string.game_created);
        Toast.makeText(this,text,Toast.LENGTH_LONG);
        finish();
    }
}
