package com.example.originalcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ____画面に対応した処理
 */
public class AlarmEdit extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_alarm);

        // 画面上のコントロールの取得
        initViews();
    }

    /**
     * 各コントロールの取得（初期化処理）
     */
    private void initViews(){
        // "戻る"メニューを設定
        setBackEvent();

        // 前画面で設定された「年月日、時刻、曜日」を取得する
        Intent intent = this.getIntent();
        String date = intent.getStringExtra(Common.DATE);
        String time = intent.getStringExtra(Common.TIME);
        int dayOfWeek = intent.getIntExtra(Common.DAY_OF_WEEK,0);
    }

    /**
     * back_from_edit_memo に対して、イベントを設定
     */
    private void setBackEvent(){
        Button button = findViewById(R.id.back_from_edit_memo);
        button.setOnClickListener(v -> {
            // 現画面を終了して、元の画面に戻る
            finish();
        });
    }

    /**
     * onStop()発生時に、編集内容を保存する
     */
    @Override
    protected void onStop() {
        super.onStop();
    }
}
