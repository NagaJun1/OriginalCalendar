package com.example.plans;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.AnalogClock;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * アラーム停止用画面
 */
public class StopAlarm extends AppCompatActivity {
    /**
     * stop_alarm_clockの取得
     *
     * @return AnalogClock
     */
    private AnalogClock getAlarmStopClock() {
        AnalogClock analogClock = findViewById(R.id.stop_alarm_clock);
        if (analogClock == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " stop_alarm_clock", Toast.LENGTH_LONG).show();
        }
        return analogClock;
    }

    /**
     * アラームの停止
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_alarm);

        // アラーム音開始
        MediaPlayer mediaPlayer = startMusic(this);

        // 画面押下時に、アラームを停止
        getAlarmStopClock().setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
        });
    }

    /**
     * 音楽を再生
     */
    public static MediaPlayer startMusic(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.free_music);
        mediaPlayer.start();
        return mediaPlayer;
    }
}
