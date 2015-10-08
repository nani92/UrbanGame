package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Przemysław on 2015-10-08.
 */

public class Player extends AppCompatActivity {

    private static Player instance;

    private Player() {
    }

    public void playSound(Context context, Sound track) {
        switch (track) {
            case gun: {
                play(MediaPlayer.create(context, R.raw.gun));
            }
            case pierd: {
                play(MediaPlayer.create(context, R.raw.pierd));
            }

        }
    }

    private void play(MediaPlayer mp) {
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
            }
        });
    }

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }


}
