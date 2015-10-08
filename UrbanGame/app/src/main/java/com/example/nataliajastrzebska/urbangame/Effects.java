package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Przemysław on 2015-10-08.
 */

public class Effects extends AppCompatActivity {

    private static Context context;

    public Effects(Context context) {
        this.context=context;
    }

    public void playSound(Sound track)
    {
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.gun);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
            }
        });
    }


    public void vibrate()
    {
        Vibrator vibe = (Vibrator) getSystemService(context.VIBRATOR_SERVICE) ;
        vibe.vibrate(150);
    }
}
