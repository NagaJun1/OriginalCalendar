package com.example.originalcalendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * アラーム編集画面に対応した処理
 */
public class AlarmEdit extends AppCompatActivity {
    /**
     * 前画面から取得してきた年月日
     */
    private String strDate = null;

    /**
     * 前画面から取得してきた時刻
     */
    private String initTime = null;

    /**
     * 前画面から取得してきた曜日
     */
    private int intDayOfWeek = 0;

    /**
     * center_time_picker
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
        // 前画面で設定された「年月日、時刻、曜日」を取得する
        Intent thisIntent = getIntentData();

        // center_time_pickerの取得と設定
        setCenterClock();

        // "戻る"メニューを設定
        setBackEvent();

        // メモ編集へ遷移するボタンのイベント設定
        setGoEditMemo(thisIntent);
    }

    /**
     * 前画面で設定した情報を取得するための Intent から情報を取得
     */
    private Intent getIntentData(){
        Intent thisIntent = this.getIntent();
        strDate = thisIntent.getStringExtra(Common.DATE);
        initTime = Common.getTimeInIntent(thisIntent);
        intDayOfWeek = thisIntent.getIntExtra(Common.DAY_OF_WEEK,0);
        return thisIntent;
    }

    /**
     * center_time_picker の設定
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setCenterClock(){
        centerClock = findViewById(R.id.center_time_picker);

        // 24時間表示で固定する
        centerClock.setIs24HourView(true);
        centerClock.setHour(Common.getHourInTime(initTime));
        centerClock.setMinute(Common.getMinutesInTime(initTime));
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
     * @param thisIntent この画面の Intent
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setGoEditMemo(Intent thisIntent){
        Button btn = findViewById(R.id.go_edit_memo);

        // メモ編集画面から遷移してきた場合は、ボタンを非表示にする
        if(thisIntent.getBooleanExtra(Common.ALREADY_OPEN_EDIT_MEMO,false)){
            btn.setVisibility(View.INVISIBLE);
        }else {
            // メモ編集画面への遷移処理の実装
            btn.setOnClickListener(v -> startActivity(createMemoEditIntent()));
        }
    }

    /**
     * メモ編集画面 のIntent
     * （メモ編集画面からの復帰時に使用…できるのか？）
     * TODO 要確認
     */
    private Intent memoEditIntent = null;

    /**
     * メモ編集画面の Intentの生成
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private Intent createMemoEditIntent(){
        memoEditIntent = new Intent(this,MemoEdit.class);

        // 現画面の時刻を Intent に保存する

        Common.setTimeInIntent(memoEditIntent, getClockTime());

        // アラーム編集画面が既に開かれた判定を設定する
        memoEditIntent.putExtra(Common.ALREADY_OPEN_EDIT_ALARM,true);

        // 既存情報の引き継ぎ
        memoEditIntent.putExtra(Common.DATE,strDate);
        memoEditIntent.putExtra(Common.DAY_OF_WEEK,intDayOfWeek);
        return memoEditIntent;
    }

    /**
     * 画面復帰時（メモ編集画面から戻ってきたとき？）
     */
    @Override
    protected void onRestart(){
        super.onRestart();

        // TODO メモ編集画面からの復帰時に使用？
        if(memoEditIntent!=null){
        }
    }

    /**
     * onStop()発生時に、編集内容を保存する
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStop() {
        super.onStop();

        // TODO アラーム情報の編集と保存
        System.out.println(getClockTime()+"＜既存のデータ内に同一のデータが存在する場合は、変更");

        // TODO 変更する時刻
        System.out.println(initTime);
    }
}
