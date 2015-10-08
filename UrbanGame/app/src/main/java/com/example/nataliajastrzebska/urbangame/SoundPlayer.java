package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Przemysław on 2015-10-08.
 */

public class SoundPlayer extends AppCompatActivity {

    private static SoundPlayer instance;

    private SoundPlayer() {
    }

    public void playSound(Context context, Sound track) {
        switch (track) {
            case gun: {
                play(MediaPlayer.create(context, R.raw.gun));
                return;
            }
            case pierd: {
                play(MediaPlayer.create(context, R.raw.pierd));
                return;
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

    public static SoundPlayer getInstance() {
        if (instance == null) {
            instance = new SoundPlayer();
        }
        return instance;
    }


}
