package com.example.originalcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * edit_memo.xmlに対応した処理
 */
public class MemoEdit extends AppCompatActivity {
    /**
     * TODO（どの様な構成にするかは未確定）
     * 画面上の EditText
     */
    private EditText editText1 = null;

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
        editText1 = findViewById(R.id.top_editor);

        // 初期状態のテキストを挿入
        editText1.setText(getDateInRecord());

        // 画面上部に、メモが紐づく時刻を設定する
        setTopTimeText();

        // "戻る"メニューを設定
        setBackEvent();
    }

    /**
     * 画面上部に、メモが紐づく時刻を設定する
     */
    private void setTopTimeText(){
        TextView textView = findViewById(R.id.top_time_text);
        textView.setText(getDateInRecord());
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
     * TODO 暫定的な処理
     * 保存されたデータ内から、該当するデータを取得
     * @return 取得されたデータ
     */
    private String getDateInRecord(){
        // 前画面で設定された「年月日、時刻、曜日」を取得する
        Intent intent = this.getIntent();
        String date = intent.getStringExtra(Common.DATE);
        String time = intent.getStringExtra(Common.TIME);
        int dayOfWeek = intent.getIntExtra(Common.DAY_OF_WEEK,0);

        // 条件に該当する"メモ"としての内容を取得する
        return Common.getTextInRecord(date,time,dayOfWeek);
    }

    /**
     * onStop()発生時に、編集内容を保存する
     */
    @Override
    protected void onStop() {
        super.onStop();
    }
}
