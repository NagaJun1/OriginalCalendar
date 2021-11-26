package com.example.originalcalendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.originalcalendar.JsonManagement.CurrentProcessingData;

/**
 * アラーム編集画面に対応した処理
 */
public class AlarmEdit extends AppCompatActivity {
    /**
     * id = center_time_picker の TimePicker
     */
    private TimePicker centerClock = null;

    /**
     * 現画面の時刻を"時:分"の文字列として取得
     * @return "時:分"の文字列
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getClockTime(){
        return Common.getStrTime(centerClock.getHour(),centerClock.getMinute());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews(){
        CurrentProcessingData.JsonCurrentProcessData jsonCurrentProcessData
                = CurrentProcessingData.getJSON(this);

        // center_time_pickerの取得と設定
        setCenterClock(jsonCurrentProcessData);

        // "戻る"メニューを設定
        setBackEvent();

        // メモ編集へ遷移するボタンのイベント設定
        setGoEditMemo(jsonCurrentProcessData);
    }

    /**
     * center_time_picker の設定
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setCenterClock(CurrentProcessingData.JsonCurrentProcessData jsonCurrentProcessData){
        centerClock = findViewById(R.id.center_time_picker);

        // 24時間表示で固定する
        centerClock.setIs24HourView(true);

        // center_time_picker の時刻を設定
        centerClock.setHour(Common.getHourInTime(jsonCurrentProcessData.strTime));
        centerClock.setMinute(Common.getMinutesInTime(jsonCurrentProcessData.strTime));
    }

    /**
     * back_from_edit_memo に対して、イベントを設定
     */
    private void setBackEvent(){
        Button button = findViewById(R.id.back_from_edit_alarm);
        button.setOnClickListener(v -> {
            // 現画面を終了して、元の画面に戻る
            finish();
        });
    }

    /**
     * メモ画面へ遷移する処理を追加
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setGoEditMemo(CurrentProcessingData.JsonCurrentProcessData jsonCurrentProcessData){
        Button btn = findViewById(R.id.go_edit_memo);

        // メモ編集画面から遷移してきた場合は、ボタンを非表示にする
        if(jsonCurrentProcessData.alreadyOpenEditMemo){
            btn.setVisibility(View.INVISIBLE);
        }else {
            // メモ編集画面への遷移処理の実装
            btn.setOnClickListener(v -> {
                // アラーム編集画面表示済みフラグをON
                CurrentProcessingData.setOpenEditAlarm(this, true);
                startActivity(new Intent(this, MemoEdit.class));
            });
        }
    }

    /**
     * 画面復帰時（メモ編集画面から戻ってきたとき？）
     */
    @Override
    protected void onRestart(){
        super.onRestart();
    }

    /**
     * onStop()発生時に、編集内容を保存する
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStop() {
        super.onStop();

        // 現行処理管理用の JSON に時刻、曜日を保存
        CurrentProcessingData.setTime(this,getClockTime());

        // 曜日の設定
        CurrentProcessingData.setDayOfWeek(this, 0);

        // TODO アラーム情報の編集と保存
        System.out.println(getClockTime()+"＜既存のデータ内に同一のデータが存在する場合は、変更");

    }
}
