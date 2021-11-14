package com.example.originalcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        Intent thisIntent = getIntentData();

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
        strTime = thisIntent.getStringExtra(Common.TIME);
        intDayOfWeek = thisIntent.getIntExtra(Common.DAY_OF_WEEK,0);
        return thisIntent;
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
    private void setGoEditMemo(Intent thisIntent){
        Button btn = findViewById(R.id.go_edit_memo);

        // メモ編集画面から遷移してきた場合は、ボタンを非表示にする
        if(thisIntent.getBooleanExtra(Common.ALREADY_OPEN_EDIT_MEMO,false)){
            btn.setVisibility(View.INVISIBLE);
        }else {
            btn.setOnClickListener(v -> {
                Intent newIntent = new Intent(btn.getContext(),MemoEdit.class);

                // 現画面の時刻を Intent に保存する
                // TODO 現画面で設定された時間を取得する処理を設定する
                newIntent.putExtra(Common.TIME, "時刻取得処理が未設定です。");

                // アラーム編集画面が既に開かれた判定を設定する
                newIntent.putExtra(Common.ALREADY_OPEN_EDIT_ALARM,true);

                // 既存情報の引き継ぎ
                newIntent.putExtra(Common.DATE,strDate);
                newIntent.putExtra(Common.DAY_OF_WEEK,intDayOfWeek);
                startActivity(newIntent);
            });
        }
    }

    /**
     * onStop()発生時に、編集内容を保存する
     */
    @Override
    protected void onStop() {
        super.onStop();
    }
}
