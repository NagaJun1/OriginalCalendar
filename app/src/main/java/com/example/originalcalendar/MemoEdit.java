package com.example.originalcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * edit_memo.xmlに対応した処理
 */
public class MemoEdit extends AppCompatActivity {
    /**
     * メモの編集領域
     */
    private EditText centerMemoText = null;

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
        setContentView(R.layout.edit_memo);

        // 画面上のコントロールの取得
        initViews();
    }

    /**
     * 各コントロールの取得（初期化処理）
     */
    private void initViews(){
        // 前画面の情報を取得
        Intent thisIntent = getIntentData();

        // 画面上部に、メモが紐づく時刻を設定する
        setTopTimeText();

        // "戻る"メニューを設定
        setBackEvent();

        // 中央のメモ編集領域の設定
        setMemoText();

        // アラーム編集画面へ遷移するボタンの処理の追加
        setGoEditAlarm(thisIntent);
    }

    /**
     * 前画面で設定した情報を取得するための Intent
     */
    private Intent getIntentData(){
        Intent intent = this.getIntent();
        strDate = intent.getStringExtra(Common.DATE);
        strTime = intent.getStringExtra(Common.TIME);
        intDayOfWeek = intent.getIntExtra(Common.DAY_OF_WEEK,0);
        return intent;
    }

    /**
     * 画面上部に、メモが紐づく時刻を設定する
     */
    private void setTopTimeText(){
        // top_time_text に時刻を設定
        TextView topTimeText = findViewById(R.id.top_time_text);
        if(strTime != null){
            topTimeText.setText(strTime);
        } else {
            topTimeText.setVisibility(View.INVISIBLE);
        }

        // top_day_text に日付（もしくは曜日）を設定
        TextView topDayText = findViewById(R.id.top_day_text);
        if(strDate != null){
            topDayText.setText(strDate);
        } else if(intDayOfWeek != 0){
            topDayText.setText(Common.ONE_WEEK[intDayOfWeek]);
        } else {
            // 設定できる情報が無い場合は、top_day_text を非表示に設定
            topDayText.setVisibility(View.INVISIBLE);
        }
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
     * 保存されたデータ内から、該当するデータを取得
     */
    private void setMemoText(){
        centerMemoText = findViewById(R.id.center_memo_text);

        // 初期状態のテキストを挿入
        // 条件に該当する"メモ"としての内容を取得する
        centerMemoText.setText(Common.getTextInRecord(strDate,strTime,intDayOfWeek, this));
    }

    /**
     * アラーム編集画面へ遷移するボタンの処理の追加
     */
    private void setGoEditAlarm(Intent thisIntent){
        Button btn = findViewById(R.id.go_edit_alarm);

        // 既にメモ編集画面を開いている状態の場合は、ボタンを非表示に設定する
        if(thisIntent.getBooleanExtra(Common.ALREADY_OPEN_EDIT_ALARM,false)){
            btn.setVisibility(View.INVISIBLE);
        }else {
            // アラーム編集画面への画面遷移処理の追加
            btn.setOnClickListener(v -> {
                Intent newIntent = new Intent(btn.getContext(), AlarmEdit.class);

                // メモ編集画面が存在する場合は、EXIST_MEMOを trueで設定する
                newIntent.putExtra(Common.ALREADY_OPEN_EDIT_MEMO, true);

                // 下記は、既存情報を引き継ぐ必要があるため、設定
                newIntent.putExtra(Common.DATE, strDate);
                newIntent.putExtra(Common.TIME, strTime);
                newIntent.putExtra(Common.DAY_OF_WEEK, intDayOfWeek);
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

        // centerMemoText内のテキストを取得
        String text = centerMemoText.getText().toString();
        if(text == null || 0 == text.length()) {
            Toast.makeText(this,"保存を実行しませんでした",Toast.LENGTH_SHORT).show();
        } else {
            // 取得したテキストに記載があるのであれば、保存する
            Common.writeSaveData(this,strDate,strTime,intDayOfWeek,text);
        }
    }
}
