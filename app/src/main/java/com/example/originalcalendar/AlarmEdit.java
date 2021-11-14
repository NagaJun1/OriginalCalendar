package com.example.originalcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ____画面に対応した処理
 */
public class AlarmEdit extends AppCompatActivity {
    /**
     * 前画面から取得してきた年月日
     */
    private String strDate = null;

    /**
     * 前画面から取得してきた時刻
     */
    private String strTime = null;

    /**
     * 前画面から取得してきた曜日
     */
    private int intDayOfWeek = 0;

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
        // 前画面で設定された「年月日、時刻、曜日」を取得する
        getIntentData();

        // "戻る"メニューを設定
        setBackEvent();
    }

    /**
     * 前画面で設定した情報を取得するための Intent
     */
    private void getIntentData(){
        Intent intent = this.getIntent();
        strDate = intent.getStringExtra(Common.DATE);
        strTime = intent.getStringExtra(Common.TIME);
        intDayOfWeek = intent.getIntExtra(Common.DAY_OF_WEEK,0);
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
