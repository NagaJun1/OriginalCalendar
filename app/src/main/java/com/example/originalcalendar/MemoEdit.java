package com.example.originalcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * edit_memo.xmlに対応した処理
 */
public class MemoEdit extends AppCompatActivity {
    /**
     * メモの編集領域
     * TODO メモの保存のためにデータを取得する必要があるため、privateな変数として設定
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
        getIntentData();

        // 中央のメモ編集領域の設定
        setMemoText();

        // 画面上部に、メモが紐づく時刻を設定する
        setTopTimeText();

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
     * 画面上部に、メモが紐づく時刻を設定する
     */
    private void setTopTimeText(){
        TextView textView = findViewById(R.id.top_time_text);

        // top_time_text に対しては、前画面で指定された時刻を挿入する
        // 必要な情報だけを設定するため、不要な情報は除外
        String text = null;
        if(strDate != null){
            text = strDate + " ";
        }
        if(strTime != null){
            text += strTime + " ";
        }
        if(intDayOfWeek != 0){
            text += Common.ONE_WEEK[intDayOfWeek];
        }
        // 設定できる情報が無い場合は、top_time_text を非表示に設定
        if(text == null){
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setText(text);
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
        centerMemoText.setText(Common.getTextInRecord(strDate,strTime,intDayOfWeek));
    }

    /**
     * onStop()発生時に、編集内容を保存する
     */
    @Override
    protected void onStop() {
        super.onStop();
    }
}
